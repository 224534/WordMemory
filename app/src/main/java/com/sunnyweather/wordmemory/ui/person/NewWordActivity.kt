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

class NewWordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_word_dialog)
        val wordEdit = findViewById<EditText>(R.id.wordEdit)
        val translateEdit = findViewById<EditText>(R.id.translateEdit)
        val cancel = findViewById<Button>(R.id.cancel)
        val finish = findViewById<Button>(R.id.finish)
        cancel.setOnClickListener {
            finish()
        }
        finish.setOnClickListener {
            val word = wordEdit.text.toString()
            val translate = translateEdit.text.toString()
            val intent = Intent()
            intent.putExtra("word", word)
            intent.putExtra("translate", translate)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}