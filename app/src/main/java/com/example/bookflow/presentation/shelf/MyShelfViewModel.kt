package com.example.bookflow.presentation.shelf

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bookflow.data.repository.BookRepository
import com.example.bookflow.di.AppModule
import com.example.bookflow.presentation.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class MyShelfViewModel(
    private val repository: BookRepository,
) : BaseViewModel() {

    private val _state = MutableStateFlow<MyShelfScreenState>(MyShelfScreenState.Initial)
    val state: StateFlow<MyShelfScreenState> = _state.asStateFlow()

    private var observeBooksJob: Job? = null

    init {
        observeSavedBooks()
    }

    private fun observeSavedBooks() {
        observeBooksJob?.cancel()
        observeBooksJob = repository.observeLibraryBooks()
            .onStart { _state.value = MyShelfScreenState.Loading }
            .onEach { books ->
                _state.value = if (books.isEmpty()) {
                    MyShelfScreenState.Empty
                } else {
                    MyShelfScreenState.Content(books)
                }
            }
            .catch { _state.value = MyShelfScreenState.Error }
            .launchIn(viewModelScope)
    }

    fun onScreenEvent(event: MyShelfEvent) {
        when (event) {
            MyShelfEvent.OnRetryClick -> observeSavedBooks()
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                MyShelfViewModel(
                    repository = AppModule.bookRepository
                )
            }
        }
    }
}