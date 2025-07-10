package com.sunnyweather.wordmemory.logic.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sunnyweather.wordmemory.logic.model.WordPrefer

@Database(version = 1, entities = [WordPrefer::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun wordsDao() : WordsDao

    companion object {

        private var instance : AppDatabase? = null

        @Synchronized
        fun getDatabase(context: Context) : AppDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_database")
                .build().apply {
                    instance = this
                }
        }

    }

}