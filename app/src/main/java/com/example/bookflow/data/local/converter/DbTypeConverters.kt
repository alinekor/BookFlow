package com.example.bookflow.data.local.converter

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object DbTypeConverters {

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Json.Default.encodeToString(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return Json.Default.decodeFromString(value)
    }
}