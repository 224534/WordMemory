package com.sunnyweather.wordmemory.logic.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sunnyweather.wordmemory.logic.model.WordPrefer

interface WordsDao {

    @Insert
    fun insertWords(words: WordPrefer) : Long

    @Update
    fun updateWords(words: WordPrefer)

    @Delete
    fun deleteWords(words: WordPrefer)

    @Query("select * from WordPrefer")
    fun loadAllWords() : List<WordPrefer>

    @Query("select * from WordPrefer where id == :bookId")
    fun loadWordById(bookId : Long) : WordPrefer

}