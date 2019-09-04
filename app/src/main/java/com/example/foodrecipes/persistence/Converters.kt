package com.example.foodrecipes.persistence

import androidx.room.TypeConverter
import com.bluelinelabs.logansquare.LoganSquare

class Converters {

    @TypeConverter
    fun fromArrayList(list: List<String>): String = LoganSquare.serialize(list)

    @TypeConverter
    fun fromString(jsonString: String): MutableList<String> {
        return LoganSquare.parseList(jsonString, String::class.java)
    }
}