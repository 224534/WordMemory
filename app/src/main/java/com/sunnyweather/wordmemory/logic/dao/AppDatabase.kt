package com.sunnyweather.wordmemory.logic.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sunnyweather.wordmemory.logic.model.WordPrefer

@Database(version = 1, entities = [WordPrefer::class], exportSchema = false)
@TypeConverters(Converters::class)
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
                .addTypeConverter(Converters())
                .fallbackToDestructiveMigration()
                .build().apply {
                    instance = this
                }
        }

    }

}