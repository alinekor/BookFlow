package com.example.bookflow.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookDbEntity(
    @PrimaryKey
    val bookKey: String,
    val title: String,
    val description: String?,
    val authors: List<String>,
    val subjects: List<String>,
    val publishYear: Int?,
    val coverId: Int?,
)