package com.sunnyweather.wordmemory.logic

import com.sunnyweather.wordmemory.MyApplication
import com.sunnyweather.wordmemory.logic.dao.AppDatabase
import com.sunnyweather.wordmemory.logic.model.Word
import com.sunnyweather.wordmemory.logic.model.WordPrefer

object Repository {

    val wordsDao = AppDatabase.getDatabase(MyApplication.context).wordsDao()

    fun getBooks() = wordsDao.loadAllWords()

    fun insertBookItem(bookName: String) = wordsDao.insertWords(WordPrefer(bookName, mutableListOf()))
    //新建时什么都没有，直接传入名字即可

    fun updateBookItem(book: WordPrefer) = wordsDao.updateWords(book)

    fun deleteBookItem(book: WordPrefer) = wordsDao.deleteWords(book)

    fun getBookById(bookId: Long) = wordsDao.loadWordById(bookId)

}