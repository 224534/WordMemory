package com.sunnyweather.wordmemory.ui.person

import androidx.lifecycle.ViewModel
import com.sunnyweather.wordmemory.logic.Repository
import com.sunnyweather.wordmemory.logic.Repository.wordsDao
import com.sunnyweather.wordmemory.logic.dao.WordsDao
import com.sunnyweather.wordmemory.logic.model.WordPrefer

class BookViewModel : ViewModel() {

    fun getBooks() = Repository.getBooks()

    fun insertBookItem(bookName: String) = Repository.insertBookItem(bookName)

    fun updateBookItem(book: WordPrefer) = Repository.updateBookItem(book)

    fun deleteBookItem(book: WordPrefer) = Repository.deleteBookItem(book)

    fun getBookById(id : Long) = Repository.getBookById(id)

}