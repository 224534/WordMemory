package com.sunnyweather.wordmemory.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ViewSwitcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.sunnyweather.wordmemory.R
import com.sunnyweather.wordmemory.getBitmapFromUri
import com.sunnyweather.wordmemory.getResultLauncher
import com.sunnyweather.wordmemory.logic.ImageStoreManager
import com.sunnyweather.wordmemory.makeToast
import com.sunnyweather.wordmemory.ui.person.BookActivity
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    val MODE_MEMORY = 1
    val MODE_RANDOM = 2
    val MODE_LIKE = 4
    //使用二进制数码表示模式状态
    var bookId = -1L

    private val imageName = "header_image"

    @SuppressLint("ClickableViewAccessibility", "InternalInsetResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val toolBar = findViewById<Toolbar>(R.id.toolBar)
        val navigationView = findViewById<NavigationView>(R.id.navView)
        val wordEdit = findViewById<EditText>(R.id.wordEdit)
        val translate = findViewById<TextView>(R.id.translate)
        val last = findViewById<Button>(R.id.last)
        val next = findViewById<Button>(R.id.next)
        val first = findViewById<Button>(R.id.first)
        val finish = findViewById<Button>(R.id.finish)
        val placeLayout = findViewById<LinearLayout>(R.id.placeLayout)
        val topMargin = findViewById<ImageView>(R.id.topMargin)
        val headerView = navigationView.getHeaderView(0)
        val headImage = headerView.findViewById<ImageView>(R.id.headImage)
        val tip = headerView.findViewById<TextView>(R.id.tip)

        setSupportActionBar(toolBar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_home)
        }

        thread { //开启线程加载，节约进入程序的时间
            val headerImageRes = ImageStoreManager.loadImage(this, imageName)
            runOnUiThread {
                if(headerImageRes != null) {
                    Glide.with(this).load(headerImageRes).into(headImage)
                    tip.visibility = View.INVISIBLE //加载到图片就隐藏
                }
                else tip.visibility = View.VISIBLE //否则加载出来
            }
        }
        val headImageResultLauncher = getResultLauncher { data ->
            data.data?.let { uri ->
                Glide.with(this@MainActivity).load(uri).into(headImage)
                thread { //加载完成后，使用线程储存，节约展示图片的时间
                    val bitmap = getBitmapFromUri(uri)!!
                    ImageStoreManager.saveImage(this@MainActivity, bitmap, imageName)
                    tip.visibility = View.INVISIBLE
                }
            }
        }
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.book -> {
                    val intent = Intent(this, BookActivity::class.java)
                    intent.putExtra("like_or_not", false)
                    startActivity(intent)
                }
                R.id.like -> {
                    val intent = Intent(this, BookActivity::class.java)
                    intent.putExtra("like_or_not", true)
                    startActivity(intent)
                }
            }
            true
        }
        headImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            headImageResultLauncher.launch(intent)
        }

        last.visibility = View.INVISIBLE
        next.visibility = View.INVISIBLE
        first.visibility = View.INVISIBLE
        finish.visibility = View.INVISIBLE
        placeLayout.visibility = View.INVISIBLE
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        val statusBarHeight = resources.getDimensionPixelSize(resourceId)
        resources.getDimension(R.dimen.status_bar_height)
        val layoutParams = topMargin.layoutParams
        layoutParams.height = statusBarHeight - 20
        topMargin.layoutParams = layoutParams

        val likeOrNot = intent.getBooleanExtra("like_or_not", false)
        if(likeOrNot) viewModel.MODE = viewModel.MODE xor MODE_LIKE
        bookId = intent.getLongExtra("book_id", -1)
        if(bookId == -1L) {
            //"打开文件失败".makeToast()
            translate.text = "打开一个文件"
        }
        else {

            if(viewModel.isInitialized == false) {
                viewModel.book = viewModel.getBookById(bookId) //book不会变，不需要重建
                viewModel.isInitialized = true
            }

            //下面的是切换模式时会改变的内容

            if(viewModel.title == null) { //title会变，需要重建
                val builder = StringBuilder()
                builder.append(viewModel.book.name)
                if(viewModel.MODE and MODE_LIKE != 0) { //判断是否为收藏
                    builder.append("（收藏）")
                }
                viewModel.title = builder.toString()
            }
            supportActionBar?.title = viewModel.title

            if(viewModel.book.words.isNotEmpty()) {
                setMainText()
                last.visibility = View.VISIBLE
                next.visibility = View.VISIBLE
                first.visibility = View.VISIBLE
                placeLayout.visibility = View.VISIBLE
                wordEdit.isSaveEnabled = true //重建时恢复可保存状态
            }
            else {
                translate.text = "这里需要你的单词"
            }

        }

    }

    fun setMainText() {
        val wordEdit = findViewById<EditText>(R.id.wordEdit)
        val wordTips = findViewById<TextView>(R.id.wordTips)
        val last = findViewById<Button>(R.id.last)
        val next = findViewById<Button>(R.id.next)
        val first = findViewById<Button>(R.id.first)
        val finish = findViewById<Button>(R.id.finish)
        val viewSwitcher = findViewById<ViewSwitcher>(R.id.viewSwitcher)
        val listSize = findViewById<TextView>(R.id.listSize)
        val goTo = findViewById<Button>(R.id.goTo)
        val placeEdit = findViewById<EditText>(R.id.placeEdit)
        val placeText = findViewById<TextView>(R.id.placeText)
        val placeSwitcher = findViewById<ViewSwitcher>(R.id.placeSwitcher)

        setModeText()
        getWordsFinal()
        setItemText(viewModel.positionNow)
        listSize.text = "${viewModel.wordsFinal.size}"

        if(viewModel.MODE and MODE_MEMORY == 0) { //背诵模式
            viewSwitcher.displayedChild = 0 //背诵模式不可见输入
            finish.visibility = View.INVISIBLE //也不应该有完成
            wordTips.visibility = View.INVISIBLE //也不该有提示
        }
        else { //默写模式
            viewSwitcher.displayedChild = 1 //默写模式不可见原文
            finish.visibility = View.VISIBLE //应该有完成
            wordTips.visibility = View.VISIBLE //应该有提示
        }

        first.setOnClickListener {
            saveInputAndTips()
            viewModel.positionNow = 0
            setItemText(viewModel.positionNow)
        }

        last.setOnClickListener {
            if(viewModel.positionNow == 0) { //已经是第一个
                "没有上一个了".makeToast()
            }
            else {
                viewModel.positionNow--
                setItemText(viewModel.positionNow)
            }
            saveInputAndTips()
        }

        next.setOnClickListener {
            if(viewModel.positionNow == viewModel.wordsFinal.size - 1) { //已经是最后一个
                "没有下一个了".makeToast()
            }
            else {
                viewModel.positionNow++
                setItemText(viewModel.positionNow)
            }
            saveInputAndTips()
        }

        finish.setOnClickListener {
            val inputWord = wordEdit.text.toString().trim()
            if(inputWord == viewModel.wordsFinal[viewModel.positionNow].word) {
                wordTips.setTextColor(ContextCompat.getColor(this, R.color.inputRight))
                wordTips.text = "正确！"
                saveInputAndTips(R.color.inputRight)
            }
            else {
                wordTips.setTextColor(ContextCompat.getColor(this, R.color.inputWrong))
                wordTips.text = "正确答案为：${viewModel.wordsFinal[viewModel.positionNow].word}"
                saveInputAndTips(R.color.inputWrong)
            }
        }

        placeText.setOnClickListener {
            placeSwitcher.displayedChild = 0
            placeEdit.setText(placeText.text.toString())
            placeEdit.requestFocus()
            placeEdit.setSelection(placeEdit.text.length)
        }

        goTo.setOnClickListener {
            val placeText = placeEdit.text.toString()
            var place = placeText.toIntOrNull()
            if(place != null) {
                place = place - 1 //适应数组
                if(place >= 0 && place < viewModel.wordsFinal.size) {
                    viewModel.positionNow = place
                    setItemText(viewModel.positionNow)
                }
                else {
                    "请输入正确的位置".makeToast()
                }
            }
            else {
                "请输入正确的位置".makeToast()
            }
        }
    }

    fun setModeText() {
        val modeText = findViewById<TextView>(R.id.modeText)
        val builder = StringBuilder()
        builder.append("当前模式：")
        if(viewModel.MODE and MODE_MEMORY == 0)
            builder.append("背诵")
        else
            builder.append("默写")
        builder.append("/")
        if(viewModel.MODE and MODE_RANDOM == 0)
            builder.append("顺序")
        else
            builder.append("随机")
        modeText.text = builder.toString()
    }

    fun getWordsFinal() {
        val words = if(viewModel.MODE and MODE_LIKE == 0) viewModel.book.words
            else viewModel.book.words.filter { it.like == true }
        viewModel.wordsFinal = if(viewModel.MODE and MODE_RANDOM != 0) { //需要实现随机
            words.shuffled()
        }
        else words
    }


    @SuppressLint("SetTextI18n")
    fun setItemText(position: Int) {
        val placeText = findViewById<TextView>(R.id.placeText)
        val translate = findViewById<TextView>(R.id.translate)
        val placeSwitcher = findViewById<ViewSwitcher>(R.id.placeSwitcher)
        translate.text = viewModel.wordsFinal[position].translate
        placeSwitcher.displayedChild = 1
        placeText.text = "${position + 1}"
        if(viewModel.MODE and MODE_MEMORY == 0) {
            setTextToMemory(position)
        }
        else setTextToWrite(position)
    }

    fun setTextToMemory(position: Int) {
        val word = findViewById<TextView>(R.id.word)
        word.text = viewModel.wordsFinal[position].word
    }

    fun setTextToWrite(position: Int) {
        val wordEdit = findViewById<EditText>(R.id.wordEdit)
        val wordTips = findViewById<TextView>(R.id.wordTips)
        wordEdit.setText(viewModel.inputWords[position])
        wordEdit.setSelection(wordEdit.text.length)
        wordTips.text = viewModel.tips[position].tip
        if(viewModel.tips[position].tip.isNotEmpty()) {
            wordTips.setTextColor(ContextCompat.getColor(this, viewModel.tips[position].color))
        }
    }

    fun saveInputAndTips(color: Int = -1) {
        val wordEdit = findViewById<EditText>(R.id.wordEdit)
        val wordTips = findViewById<TextView>(R.id.wordTips)
        if(viewModel.MODE and MODE_MEMORY != 0) {
            viewModel.inputWords[viewModel.positionNow] = wordEdit.text.toString().trim()
            viewModel.tips[viewModel.positionNow].tip = wordTips.text.toString()
            if(color != -1) viewModel.tips[viewModel.positionNow].color = color
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        if(menu != null) {
            val memoryOrNot = menu.findItem(R.id.memoryOrNot)
            val randomOrNot = menu.findItem(R.id.randomOrNot)
            if(bookId != -1L) {
                if(viewModel.MODE and MODE_MEMORY == 0)
                    memoryOrNot.title = "切换为默写模式"
                else
                    memoryOrNot.title = "切换为背诵模式"
                if(viewModel.MODE and MODE_RANDOM == 0)
                    randomOrNot.title = "切换为随机模式"
                else
                    randomOrNot.title = "切换为顺序模式"
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val wordEdit = findViewById<EditText>(R.id.wordEdit)
        when(item.itemId) {
            android.R.id.home -> {
                val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
                drawerLayout.openDrawer(GravityCompat.START)
            }
            R.id.memoryOrNot -> { //切换是否背诵
                if(bookId != -1L) {
                    wordEdit.isSaveEnabled = false //暂时去除可保存状态
                    viewModel.MODE = viewModel.MODE xor MODE_MEMORY
                    viewModel.clearChangeable()
                    viewModel.clearEdit = true
                    recreate()
                }
            }
            R.id.randomOrNot -> { //切换是否随机
                if(bookId != -1L) {
                    wordEdit.isSaveEnabled = false //暂时去除可保存状态
                    viewModel.MODE = viewModel.MODE xor MODE_RANDOM
                    viewModel.clearChangeable()
                    viewModel.clearEdit = true
                    recreate()
                }
            }
        }
        return true
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers()
        }
        else super.onBackPressed()
    }

    /*override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            val placeSwitcher = findViewById<ViewSwitcher>(R.id.placeSwitcher)
            placeSwitcher.displayedChild = 1
            return true
        }
        return super.onTouchEvent(event)
    }*/
}