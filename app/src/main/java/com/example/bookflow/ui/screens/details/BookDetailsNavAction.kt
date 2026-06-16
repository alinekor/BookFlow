package com.example.bookflow.ui.screens.details

sealed class BookDetailsNavAction {
    data object NavigateBack : BookDetailsNavAction()
}