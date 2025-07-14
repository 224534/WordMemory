package com.sunnyweather.wordmemory.logic.dao

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sunnyweather.wordmemory.logic.model.Word

@ProvidedTypeConverter
class Converters {

    @TypeConverter
    fun fromWordList(words: List<Word>) : String {
        return Gson().toJson(words)
    }

    @TypeConverter
    fun toWordList(value: String) : List<Word> {
        val type = object : TypeToken<List<Word>>() {}.type
        return Gson().fromJson<List<Word>>(value, type)
    }

}