package com.example.bookflow.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.example.bookflow.data.repository.BookRepository
import com.example.bookflow.ui.navigation.AppDestination
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookDetailsViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val repository = BookRepository()
    private val screenArgs = savedStateHandle.toRoute<AppDestination.BookDetails>()

    private val _state = MutableStateFlow<BookDetailsScreenState>(BookDetailsScreenState.Initial)
    val state: StateFlow<BookDetailsScreenState> = _state.asStateFlow()

    init {
        loadBookDetails()
    }

    private fun loadBookDetails() {
        viewModelScope.launch {
            _state.value = BookDetailsScreenState.Loading

            try {
                val bookDetails = repository.loadBookDetails(
                    bookKey = screenArgs.bookKey,
                )
                _state.value = BookDetailsScreenState.Content(
                    bookDetails = bookDetails,
                    savedToLibrary = false, //todo достаём флаг из базы
                )

            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _state.value = BookDetailsScreenState.Error
            }
        }
    }

    fun onScreenEvent(event: BookDetailsEvent) {
        when (event) {
            BookDetailsEvent.OnSaveToLibraryClick -> onSaveToLibraryClick()
        }
    }

    private fun onSaveToLibraryClick() {
        //TODO
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                BookDetailsViewModel(createSavedStateHandle())
            }
        }
    }
}