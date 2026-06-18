package com.example.bookflow.presentation.shelf

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bookflow.data.repository.BookRepository
import com.example.bookflow.presentation.base.BaseViewModel
import com.example.bookflow.presentation.search.SearchViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyShelfViewModel(
    private val repository: BookRepository,
) : BaseViewModel() {

    private val _state = MutableStateFlow<MyShelfScreenState>(MyShelfScreenState.Initial)
    val state: StateFlow<MyShelfScreenState> = _state.asStateFlow()

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                )
                SearchViewModel(
                    repository = BookRepository(application.applicationContext)
                )
            }
        }
    }
}