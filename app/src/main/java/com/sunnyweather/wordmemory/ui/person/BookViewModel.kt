package com.sunnyweather.wordmemory.ui.person

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sunnyweather.wordmemory.logic.Repository
import com.sunnyweather.wordmemory.logic.model.WordPrefer

class BookViewModel : ViewModel() {

    val books : LiveData<MutableList<WordPrefer>>
        get() = _books

    private val _books = MutableLiveData<MutableList<WordPrefer>>()

    var isInitialized = false

    var likeOrNot = false

    fun getBooks() : MutableList<WordPrefer> { //每次修改数据库时，book都会同步更新，直接返回即可
        if(_books.value == null) _books.value = Repository.getBooks()
        return _books.value!! //数据库中不可能空
    }

    fun insertBookItem(bookName: String) {
        Repository.insertBookItem(bookName)
        _books.value = Repository.getBooks() //更新
    }

    fun updateBookItem(book: WordPrefer) {
        Repository.updateBookItem(book)
        _books.value = Repository.getBooks()
    }

    fun deleteBookItem(book: WordPrefer) {
        Repository.deleteBookItem(book)
        _books.value = Repository.getBooks()
    }

    fun getBookById(id : Long) = Repository.getBookById(id)

    /*fun getBooks() : List<WordPrefer>{
        val books = Repository.getBooks()
        Log.d("BookViewModel", "$books")
        return books
    }*/

}