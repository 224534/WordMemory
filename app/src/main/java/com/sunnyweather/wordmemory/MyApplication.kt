package com.sunnyweather.wordmemory

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.room.PrimaryKey
import com.sunnyweather.wordmemory.logic.Repository
import kotlin.math.max

class MyApplication : Application() {

    @SuppressLint("StaticFieldLeak")
    companion object {
        lateinit var context: Context
        var primaryKey: Long = 0
        var isInitialized = false
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        val books = Repository.getBooks()
        for(book in books) {
            for (word in book.words) {
                primaryKey = max(word.id, primaryKey) //取最大值与自身比较
            }
        }
    }
}