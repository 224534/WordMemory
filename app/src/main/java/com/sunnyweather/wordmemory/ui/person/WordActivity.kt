package com.sunnyweather.wordmemory.ui.person

import android.annotation.SuppressLint
import android.app.ComponentCaller
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toolbar
import android.window.OnBackInvokedDispatcher
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sunnyweather.wordmemory.R
import com.sunnyweather.wordmemory.getResultLauncher
import com.sunnyweather.wordmemory.logic.model.Word

class WordActivity : AppCompatActivity() {

    var bookId = -1L
    val viewModel by lazy {
        ViewModelProvider(this, WordViewModelFactory(bookId)).get(WordViewModel::class.java)
    }

    lateinit var setResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_word)
        val toolBar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.WordToolBar)
        val recyclerView = findViewById<RecyclerView>(R.id.WordRecyclerView)
        val addWord = findViewById<FloatingActionButton>(R.id.addWord)
        val batchImport = findViewById<Button>(R.id.batchImport)
        val searchEdit = findViewById<EditText>(R.id.searchEdit)
        val searchBtn = findViewById<Button>(R.id.search)

        bookId = intent.getLongExtra("book_id", 0)
        if(viewModel.isInitialized == false) {
            viewModel.likeOrNot = intent.getBooleanExtra("like_or_not", false)
            viewModel.book.value!!.name = intent.getStringExtra("book_name") ?: ""
            viewModel.isInitialized = true
        }

        setSupportActionBar(toolBar)
        supportActionBar?.let {
            it.title = viewModel.book.value!!.name
            it.setDisplayHomeAsUpEnabled(true)
        }

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        //val adapter = if(viewModel.likeOrNot == false) WordAdapter(this, viewModel.getWords())
        //            else WordAdapter(this, viewModel.getWords().filter { it.like == true })
        val adapter = WordAdapter(this, viewModel.getWords()) //改成内部筛了
        recyclerView.adapter = adapter

        setResultLauncher = getResultLauncher { data ->
            val word = data.getStringExtra("word")
            val translate = data.getStringExtra("translate")
            val like = data.getBooleanExtra("like", false)
            val id = data.getLongExtra("id", -1L)
            if(word != null && translate != null && id != -1L) {
                viewModel.updateWord(Word(word, translate, like), id)
                //recreate()
            }
        }
        val addResultLauncher = getResultLauncher { data ->
            val word = data.getStringExtra("word")
            val translate = data.getStringExtra("translate")
            val like = data.getBooleanExtra("like_or_not", false)
            if(word != null && translate != null) {
                viewModel.insertWord(Word(word, translate, like))
                //recreate()
            }
        }
        val batchImportResultLauncher = getResultLauncher { data ->
            val input = data.getStringExtra("import_text")
            if(input != null) {
                val inputList = input.lines() //前面已经保证行数一定为偶数
                for(i in 0 until inputList.size step 2) {
                    viewModel.insertWord(Word(inputList[i], inputList[i+1], viewModel.likeOrNot))
                    //奇数行为单词，偶数行为中文翻译，是否收藏与当前所处文件夹有关
                }
            }
        }
        addWord.setOnClickListener {
            val intent = Intent(this, NewWordActivity::class.java)
            intent.putExtra("like_or_not", viewModel.likeOrNot)
            addResultLauncher.launch(intent)
            //startActivityForResult(intent, 2)
        }

        batchImport.setOnClickListener {
            val intent = Intent(this, ImportActivity::class.java)
            batchImportResultLauncher.launch(intent)
        }

        searchBtn.setOnClickListener {
            val search = searchEdit.text.toString()
            val searchResult = if(search.isNotEmpty()) {
                viewModel.getWords().filter  { it.word.contains(search, ignoreCase = true) }.toMutableList()
            }
            else viewModel.getWords()
            adapter.submitList(searchResult)
        }

        viewModel.book.observe(this) { value ->
            adapter.submitList(value.words)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    /*override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        caller: ComponentCaller
    ) {
        super.onActivityResult(requestCode, resultCode, data, caller)
        when(requestCode) {
            1 -> if(resultCode == RESULT_OK && data != null) { //修改
                val word = data.getStringExtra("word")
                val translate = data.getStringExtra("translate")
                val like = data.getBooleanExtra("like", false)
                val id = data.getLongExtra("id", -1L)
                if(word != null && translate != null && id != -1L) {
                    viewModel.updateWord(Word(word, translate, like), id)
                    recreate()
                }
            }
            2 -> if(resultCode == RESULT_OK && data != null) { //新建
                val word = data.getStringExtra("word")
                val translate = data.getStringExtra("translate")
                val like = data.getBooleanExtra("like", false)
                if(word != null && translate != null) {
                    viewModel.insertWord(Word(word, translate, like))
                    recreate()
                }
            }
        }
    }*/

}