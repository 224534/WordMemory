package com.sunnyweather.wordmemory.logic

import com.sunnyweather.wordmemory.MyApplication
import com.sunnyweather.wordmemory.logic.dao.AppDatabase
import com.sunnyweather.wordmemory.logic.model.Word
import com.sunnyweather.wordmemory.logic.model.WordPrefer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

object Repository {

    val wordsDao = AppDatabase.getDatabase(MyApplication.context).wordsDao()

    fun getBooks(): MutableList<WordPrefer> {
        synchronized(Any()) {
            return runBlocking(Dispatchers.IO) {
                wordsDao.loadAllWords()
            }
        }
    }

    fun insertBookItem(bookName: String) {
        synchronized(Any()) {
            return runBlocking(Dispatchers.IO) {
                wordsDao.insertWords(WordPrefer(bookName, mutableListOf()))
            }
        }
        /*thread {
            wordsDao.insertWords(WordPrefer(bookName, mutableListOf()))
        }*/
    }
    //新建时什么都没有，直接传入名字即可

    fun updateBookItem(book: WordPrefer) {
        synchronized(Any()) {
            return runBlocking(Dispatchers.IO) {
                for(word in book.words) { //只有储存的数据
                    if(word.id == -1L) { //并且还没有键值
                        word.id = MyApplication.primaryKey + 1 //才需要键值
                        MyApplication.primaryKey++
                    }
                }
                wordsDao.updateWords(book)
            }
        }
        /*thread {
            for(word in book.words) { //只有储存的数据
                if(word.id == -1L) { //并且还没有键值
                    word.id = MyApplication.primaryKey + 1 //才需要键值
                    MyApplication.primaryKey++
                }
            }
            wordsDao.updateWords(book)
        }*/
    }

    fun deleteBookItem(book: WordPrefer) {
        synchronized(Any()) {
            return runBlocking(Dispatchers.IO) {
                wordsDao.deleteWords(book)
            }
        }
        /*thread {
            wordsDao.deleteWords(book)
        }*/
    }

    fun getBookById(bookId: Long) : WordPrefer {
        synchronized(Any()) {
            return runBlocking(Dispatchers.IO) {
                wordsDao.loadWordById(bookId)
            }
        }
    }

}