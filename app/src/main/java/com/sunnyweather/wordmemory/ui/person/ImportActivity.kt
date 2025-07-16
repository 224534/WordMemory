package com.sunnyweather.wordmemory.ui.person

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sunnyweather.wordmemory.R
import com.sunnyweather.wordmemory.makeToast

class ImportActivity : AppCompatActivity() {

    private var deleteAll = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_import)

        val importEdit = findViewById<EditText>(R.id.importEdit)
        val toolbar = findViewById<Toolbar>(R.id.toolBar)
        val finish = findViewById<Button>(R.id.finish)
        val export = findViewById<Button>(R.id.export)
        val topMargin = findViewById<ImageView>(R.id.topMargin)
        val bottomMargin =  findViewById<ImageView>(R.id.bottomMargin)

        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.title = "批量导入"
            it.setDisplayHomeAsUpEnabled(true)
        }
        val resourceId1 = resources.getIdentifier("status_bar_height", "dimen", "android")
        val resourceId2 = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        val statusBarHeight = resources.getDimensionPixelSize(resourceId1)
        val navigationBarHeight = resources.getDimensionPixelSize(resourceId2)
        val layoutParams = topMargin.layoutParams
        layoutParams.height = statusBarHeight - 20
        topMargin.layoutParams = layoutParams
        val params = bottomMargin.layoutParams
        params.height = navigationBarHeight
        bottomMargin.layoutParams = params

        finish.setOnClickListener {
            val input = importEdit.text.toString().trimEnd() //去除尾部空格换行
            input.replace("\r\n", "\n").replace("\r", "\n")
            if(input.count{it == '\n' } % 2 == 0) { //去掉末尾换行后换行为偶数，说明总行数为奇数，输入格式错误
                "输入格式错误，请检查输入格式".makeToast()
            }
            else {
                val intent = Intent()
                intent.putExtra("import_text", input)
                intent.putExtra("delete_all", deleteAll)
                setResult(RESULT_OK, intent)
                finish()
            }
        }

        export.setOnClickListener {
            if(importEdit.text.isNotEmpty()) {
                AlertDialog.Builder(this).apply {
                    setTitle("确定要导入原数据吗")
                    setMessage("这将会覆盖所有已输入的内容")
                    setCancelable(true)
                    setNegativeButton ("取消") { _, _ -> }
                    setPositiveButton ("确定") { _, _ ->
                        supportActionBar?.title = "批量修改"
                        val textNow = intent.getStringExtra("text_now") ?: ""
                        importEdit.setText(textNow)
                        importEdit.setSelection(textNow.length)
                        export.visibility = View.INVISIBLE
                        deleteAll = true //全部导入后就要删去，删去操作应在点击finish后
                    }
                    show()
                }
            }
            else {
                supportActionBar?.title = "批量修改"
                val textNow = intent.getStringExtra("text_now") ?: ""
                importEdit.setText(textNow)
                importEdit.setSelection(textNow.length)
                export.visibility = View.INVISIBLE
                deleteAll = true //全部导入后就要删去，删去操作应在点击finish后
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val importEdit = findViewById<EditText>(R.id.importEdit)
        when(item.itemId) {
            android.R.id.home -> {
                if(importEdit.text.isNotEmpty()) { //有输入才确定
                    AlertDialog.Builder(this).apply {
                        setTitle("确定要退出吗")
                        setMessage("这将会删除所有已输入的内容")
                        setCancelable(true)
                        setPositiveButton("确定") { _, _ ->
                            finish()
                        }
                        setNegativeButton("取消") { _, _ ->}
                        show()
                    }
                }
                else finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}