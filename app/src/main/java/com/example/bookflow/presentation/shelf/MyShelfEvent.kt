package com.example.bookflow.presentation.shelf

sealed class MyShelfEvent {
    data object OnRetryClick : MyShelfEvent()
}