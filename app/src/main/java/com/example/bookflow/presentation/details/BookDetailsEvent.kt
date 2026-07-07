package com.example.bookflow.presentation.details

sealed class BookDetailsEvent {
    data object OnRetryClick : BookDetailsEvent()
    data object OnSaveToLibraryClick : BookDetailsEvent()
}