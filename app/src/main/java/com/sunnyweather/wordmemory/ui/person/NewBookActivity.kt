package com.sunnyweather.wordmemory.ui.person

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.sunnyweather.wordmemory.R
import com.sunnyweather.wordmemory.logic.Repository

class NewBookActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_book_dialog)
        val bookEdit = findViewById<EditText>(R.id.bookEdit)
        val cancel = findViewById<Button>(R.id.cancel)
        val finish = findViewById<Button>(R.id.finish)
        cancel.setOnClickListener {
            finish() //直接退出
        }
        finish.setOnClickListener {
            val bookName = bookEdit.text.toString()
            Repository.insertBookItem(bookName)
            finish()
        }
    }
}