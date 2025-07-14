package com.sunnyweather.wordmemory.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sunnyweather.wordmemory.logic.Repository
import com.sunnyweather.wordmemory.logic.model.Tip
import com.sunnyweather.wordmemory.logic.model.Word
import com.sunnyweather.wordmemory.logic.model.WordPrefer

class MainViewModel : ViewModel() {


    var isInitialized = false

    lateinit var book: WordPrefer

    lateinit var wordsFinal: List<Word>

    var MODE = 0

    var clearEdit = false //设定清除输入框中的内容

    val inputWords by lazy {
        MutableList<String>(book.words.size){""} //一个mainActivity只会有一个book，往大了设置准没错
    }

    val tips by lazy {
        MutableList<Tip>(book.words.size){ Tip("", 0) } //一个mainActivity只会有一个book，往大了设置准没错
    }

    var positionNow = 0

    var title : String? = null

    fun getBookById(bookId: Long) = Repository.getBookById(bookId)

    fun clearChangeable() {
        if(isInitialized == true) { //一旦initialize，就一定会有book，也就一定有inputWords
            positionNow = 0
            title = null
            //wordsFinal会在set时重建，不需要清空
            inputWords.replaceAll{""}
            tips.replaceAll{ Tip("", 0) }
        }
    }

}