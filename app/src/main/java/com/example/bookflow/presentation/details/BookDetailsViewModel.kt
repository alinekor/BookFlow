package com.example.bookflow.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.example.bookflow.data.repository.BookRepository
import com.example.bookflow.di.AppModule
import com.example.bookflow.presentation.base.BaseViewModel
import com.example.bookflow.ui.navigation.AppDestination
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class BookDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: BookRepository,
) : BaseViewModel() {

    private val screenArgs = savedStateHandle.toRoute<AppDestination.BookDetails>()

    private val _screenState = MutableStateFlow<BookDetailsScreenState>(BookDetailsScreenState.Initial)
    val screenState: StateFlow<BookDetailsScreenState> = _screenState.asStateFlow()

    private var loadBookJob: Job? = null

    init {
        loadBookDetails()
        observeSavedToLibrary()
    }

    private fun loadBookDetails() {
        loadBookJob?.cancel()
        loadBookJob = launchCatching(
            onError = {
                _screenState.value = BookDetailsScreenState.Error
            }
        ) {
            _screenState.value = BookDetailsScreenState.Loading

            val bookKey = screenArgs.bookKey
            val details = repository.loadBookDetails(bookKey)
            val savedToLibrary = repository.observeSavedToLibrary(bookKey).first()

            _screenState.value = BookDetailsScreenState.Content(
                bookDetails = details,
                savedToLibrary = savedToLibrary,
            )
        }
    }

    private fun observeSavedToLibrary() {
        repository.observeSavedToLibrary(screenArgs.bookKey)
            .onEach { savedToLibrary ->
                _screenState.update { currentState ->
                    if (currentState is BookDetailsScreenState.Content) {
                        currentState.copy(savedToLibrary = savedToLibrary)
                    } else {
                        currentState
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onScreenEvent(event: BookDetailsEvent) {
        when (event) {
            BookDetailsEvent.OnRetryClick -> loadBookDetails()
            BookDetailsEvent.OnSaveToLibraryClick -> onSaveToLibraryClick()
        }
    }

    private fun onSaveToLibraryClick() {
        val content = screenState.value as? BookDetailsScreenState.Content ?: return
        val book = content.bookDetails

        launchCatching {
            if (content.savedToLibrary) {
                repository.removeBookFromLibrary(bookKey = book.key)
            } else {
                repository.saveBookToLibrary(book = book)
            }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                BookDetailsViewModel(
                    savedStateHandle = createSavedStateHandle(),
                    repository = AppModule.bookRepository,
                )
            }
        }
    }
}