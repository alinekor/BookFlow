package com.example.bookflow.ui.utils

fun formatAuthors(authors: List<String>): String? {
    return authors.takeIf { it.isNotEmpty() }?.joinToString()
}