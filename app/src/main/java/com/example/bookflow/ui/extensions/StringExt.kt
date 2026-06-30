package com.example.bookflow.ui.extensions

fun String.isValidSearchQuery(minLength: Int = 4): Boolean {
    return trim().length >= minLength
}