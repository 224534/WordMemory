package com.sunnyweather.wordmemory.logic.model

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sunnyweather.wordmemory.MyApplication
import com.sunnyweather.wordmemory.logic.Repository

@Entity
data class WordPrefer(var name: String, val words: MutableList<Word>) {

    @PrimaryKey(autoGenerate = true)
    var id : Long = 0

}


data class Word(var word: String = "", var translate: String = "", var like: Boolean = false) {

    var id: Long = -1L

}

data class Tip(var tip: String, var color: Int)
