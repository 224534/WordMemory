package com.sunnyweather.wordmemory.ui.person

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sunnyweather.wordmemory.R
import com.sunnyweather.wordmemory.logic.model.Word

class WordActivity : AppCompatActivity() {

    lateinit var viewModel: WordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_word)
        val toolBar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.WordToolBar)
        val recyclerView = findViewById<RecyclerView>(R.id.WordRecyclerView)
        val addWord = findViewById<FloatingActionButton>(R.id.addWord)

        val bookId = intent.getLongExtra("book_id", 0)
        viewModel = ViewModelProvider(this, WordViewModelFactory(bookId)).get(WordViewModel::class.java)
        viewModel.book.name = intent.getStringExtra("book_name") ?: ""

        setSupportActionBar(toolBar)
        supportActionBar?.let {
            it.title = viewModel.book.name
            it.setDisplayHomeAsUpEnabled(true)
        }

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = WordAdapter(this, viewModel.getWords())
        recyclerView.adapter = adapter

        addWord.setOnClickListener {
            val intent = Intent(this, NewWordActivity::class.java)
            startActivityForResult(intent, 2)
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
                val word = intent.getStringExtra("word")
                val translate = intent.getStringExtra("translate")
                val like = intent.getBooleanExtra("like", false)
                if(word != null && translate != null) {
                    viewModel.insertWord(Word(word, translate, like))
                }
            }
            2 -> if(resultCode == RESULT_OK && data != null) {
                val word = intent.getStringExtra("word")
                val translate = intent.getStringExtra("translate")
                if(word != null && translate != null) {
                    viewModel.insertWord(Word(word, translate, false))
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