package com.sunnyweather.wordmemory.ui.person

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sunnyweather.wordmemory.R
import com.sunnyweather.wordmemory.getResultLauncher
import com.sunnyweather.wordmemory.makeToast

class BookActivity : AppCompatActivity() {

    val viewModel by lazy {
        ViewModelProvider(this).get(BookViewModel::class.java)
    }

    lateinit var adapter: BookAdapter

    lateinit var setResultLauncher: ActivityResultLauncher<Intent>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_book)
        val recyclerView = findViewById<RecyclerView>(R.id.BookRecyclerView)
        val toolBar = findViewById<Toolbar>(R.id.BookToolBar)
        val addBook = findViewById<FloatingActionButton>(R.id.addBook)
        val searchEdit = findViewById<EditText>(R.id.searchEdit)
        val searchBtn = findViewById<Button>(R.id.search)

        if(viewModel.isInitialized == false) {
            viewModel.likeOrNot = intent.getBooleanExtra("like_or_not", false)
            viewModel.isInitialized = true
        }

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = BookAdapter(this, viewModel.getBooks())
        recyclerView.adapter = adapter

        setSupportActionBar(toolBar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            if(viewModel.likeOrNot == false)
                it.title = "我的单词本"
            else it.title = "我的收藏"
        }

        setResultLauncher = getResultLauncher { data ->
            val bookName = data.getStringExtra("book_name")
            val bookId = data.getLongExtra("book_id", 0)
            val book = viewModel.getBookById(bookId)
            if(bookName != null) {
                book.name = bookName
                viewModel.updateBookItem(book)
            }
        }
        /*val addResultLauncher = getResultLauncher { data ->
            recreate()
        }*/
        addBook.setOnClickListener {
            if(viewModel.likeOrNot == false) {
                val intent = Intent(this, NewBookActivity::class.java)
                //addResultLauncher.launch(intent)
                //startActivityForResult(intent, 2)
                startActivity(intent)
            }
            else {
                "请前往单词本添加".makeToast()
            }
        }

        searchBtn.setOnClickListener {
            val search = searchEdit.text.toString()
            val searchResult = if(search.isNotEmpty()) {
                viewModel.getBooks().filter { it.name.contains(search, ignoreCase = true) }
            }
            else viewModel.getBooks()
            adapter.submitList(searchResult)
        }

        viewModel.books.observe(this, Observer { value ->
            adapter.submitList(value)
        })

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
            1 -> if(resultCode == RESULT_OK && data != null) {
                val bookName = data.getStringExtra("book_name")
                val bookId = data.getLongExtra("book_id", 0)
                val book = viewModel.getBookById(bookId)
                if(bookName != null) {
                    book.name = bookName
                    viewModel.updateBookItem(book)
                    recreate()
                }
            }
            2 -> if(resultCode == RESULT_OK && data != null) recreate()
        }
    }*/

}