package com.sunnyweather.wordmemory.ui.person

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sunnyweather.wordmemory.R
import com.sunnyweather.wordmemory.makeToast

class ImportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_import)

        val importEdit = findViewById<EditText>(R.id.importEdit)
        val toolbar = findViewById<Toolbar>(R.id.toolBar)
        val finish = findViewById<Button>(R.id.finish)

        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.title = "批量导入"
            it.setDisplayHomeAsUpEnabled(true)
        }

        finish.setOnClickListener {
            val input = importEdit.text.toString().trimEnd() //去除尾部空格换行
            input.replace("\r\n", "\n").replace("\r", "\n")
            if(input.count{it == '\n' } % 2 == 0) { //去掉末尾换行后换行为偶数，说明总行数为奇数，输入格式错误
                "输入格式错误，请检查输入格式".makeToast()
            }
            else {
                val intent = Intent()
                intent.putExtra("import_text", input)
                setResult(RESULT_OK, intent)
                finish()
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}