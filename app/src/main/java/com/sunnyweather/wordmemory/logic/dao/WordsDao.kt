package com.sunnyweather.wordmemory.logic.dao

import android.content.Context
import androidx.core.content.edit
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sunnyweather.wordmemory.MyApplication
import com.sunnyweather.wordmemory.logic.model.WordPrefer

@Dao
interface WordsDao {

    @Insert
    fun insertWords(words: WordPrefer) : Long

    @Update
    fun updateWords(words: WordPrefer)

    @Delete
    fun deleteWords(words: WordPrefer)

    @Query("select * from WordPrefer")
    suspend fun loadAllWords() : MutableList<WordPrefer>

    @Query("select * from WordPrefer where id == :bookId")
    fun loadWordById(bookId : Long) : WordPrefer
}