package com.sunnyweather.wordmemory.ui.person

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sunnyweather.wordmemory.logic.Repository
import com.sunnyweather.wordmemory.logic.model.Word
import com.sunnyweather.wordmemory.logic.model.WordPrefer

class WordViewModel(val bookId: Long) : ViewModel() { //word页面从单词本中进来，则一定找得到book的id，不可能为空

    var isInitialized = false

    val book : LiveData<WordPrefer>
        get() = _book

    private var _book = MutableLiveData<WordPrefer>() //因为所有数据都是先改book的value然后上传数据库，因此value一定不会落后
    private var bookValue: WordPrefer
    init {
        _book.value = Repository.getBookById(bookId)
        bookValue = _book.value!!
    }


    var likeOrNot = false

    fun getWords() = bookValue.words //一个wordActivity建立后，其源book不应该改变，故bookId不变

    fun deleteWord(id: Long) {
        bookValue.words.removeIf { it.id == id }
        Repository.updateBookItem(bookValue)
        _book.value = bookValue
    }

    fun updateWord(word: Word, id : Long) {
        bookValue.words.forEach {
            if(it.id == id) {
                it.word = word.word
                it.translate = word.translate
                it.like = word.like
            }

        }
        Repository.updateBookItem(bookValue)
        _book.value = bookValue
    }

    fun insertWord(word: Word) {
        bookValue.words.add(word)
        Repository.updateBookItem(bookValue)
        _book.value = bookValue
    }

}