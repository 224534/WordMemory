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

class WordDialogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_word_dialog)
        val wordEdit = findViewById<EditText>(R.id.wordEdit)
        val translateEdit = findViewById<EditText>(R.id.translateEdit)
        val cancel = findViewById<Button>(R.id.cancel)
        val finish = findViewById<Button>(R.id.finish)
        val word = intent.getStringExtra("word")
        val translate = intent.getStringExtra("translate")
        val like = intent.getBooleanExtra("like", false)
        val id = intent.getLongExtra("id", -1L)
        wordEdit.setText(word)
        translateEdit.setText(translate)
        cancel.setOnClickListener {
            finish()
        }
        finish.setOnClickListener {
            val word = wordEdit.text.toString().trim()
            val translate = translateEdit.text.toString().trim()
            val intent = Intent()
            intent.putExtra("word", word)
            intent.putExtra("like", like)
            intent.putExtra("translate", translate)
            intent.putExtra("id", id)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}