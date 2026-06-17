package com.example.bookflow.ui.screens.details

import androidx.navigation.NavController

interface IBookDetailsRouter {
    fun navigateBack()
}

class BookDetailsNavRouter(
    private val navController: NavController,
) : IBookDetailsRouter {

    override fun navigateBack() {
        navController.popBackStack()
    }
}