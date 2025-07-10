package com.sunnyweather.wordmemory.ui.person

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sunnyweather.wordmemory.R

class BookActivity : AppCompatActivity() {

    val viewModel by lazy {
        ViewModelProvider(this).get(BookViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_book)
        val recyclerView = findViewById<RecyclerView>(R.id.BookRecyclerView)
        val toolBar = findViewById<Toolbar>(R.id.BookToolBar)
        val addBook = findViewById<FloatingActionButton>(R.id.addBook)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = BookAdapter(this, viewModel.getBooks())
        recyclerView.adapter = adapter

        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        addBook.setOnClickListener {
            val intent = Intent(this, NewBookActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(
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
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
        when(item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
    }
}