package com.sunnyweather.wordmemory.ui.person

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sunnyweather.wordmemory.R

class BookDialogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_book_dialog)
        val bookName = intent.getStringExtra("book_name")
        val bookId = intent.getLongExtra("book_id", 0)
        val position = intent.getIntExtra("position", -1)
        val bookEdit = findViewById<EditText>(R.id.bookEdit)
        val cancel = findViewById<Button>(R.id.cancel)
        val finish = findViewById<Button>(R.id.finish)
        bookEdit.setText(bookName)
        cancel.setOnClickListener {
            finish() //直接退出
        }
        finish.setOnClickListener {
            val bookName = bookEdit.text.toString().trim()
            val intent = Intent()
            intent.putExtra("book_name", bookName)
            intent.putExtra("book_id", bookId)
            intent.putExtra("position", position)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}