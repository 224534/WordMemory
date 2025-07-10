package com.sunnyweather.wordmemory.ui.person

import androidx.lifecycle.ViewModel
import com.sunnyweather.wordmemory.logic.Repository
import com.sunnyweather.wordmemory.logic.model.Word
import com.sunnyweather.wordmemory.logic.model.WordPrefer

class WordViewModel(val bookId: Long) : ViewModel() {

    var book = Repository.getBookById(bookId)

    fun getWords() = Repository.getBookById(bookId).words //一个wordActivity建立后，其源book不应该改变，故bookId不变

    fun deleteWord(index: Int) {
        book.words.removeAt(index)
        Repository.updateBookItem(book)
    }

    fun updateWord(word: Word, position: Int) {
        book.words[position] = word
        Repository.updateBookItem(book)
    }

    fun insertWord(word: Word) {
        book.words.add(word)
        Repository.updateBookItem(book)
    }

}