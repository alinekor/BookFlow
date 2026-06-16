package com.example.bookflow.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.example.bookflow.data.model.BookDetails
import com.example.bookflow.data.repository.BookRepository
import com.example.bookflow.ui.navigation.AppDestination
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BookDetailsViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val repository = BookRepository()
    private val screenArgs = savedStateHandle.toRoute<AppDestination.BookDetails>()

    private val bookDetails = MutableStateFlow<Result<BookDetails>?>(null)

    val state: StateFlow<BookDetailsScreenState> =
        combine(
            bookDetails,
            repository.observeSavedToLibrary(
                bookKey = screenArgs.bookKey
            ),
        ) { bookDetailsResult, savedToLibrary ->
            when {
                bookDetailsResult == null -> BookDetailsScreenState.Loading

                bookDetailsResult.isFailure -> BookDetailsScreenState.Error

                else -> BookDetailsScreenState.Content(
                    bookDetails = bookDetailsResult.getOrThrow(),
                    savedToLibrary = savedToLibrary,
                )
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = BookDetailsScreenState.Initial,
            )

    private var loadBookJob: Job? = null

    init {
        loadBookDetails()
    }

    private fun loadBookDetails() {
        loadBookJob?.cancel()
        loadBookJob = viewModelScope.launch {
            bookDetails.value = null

            bookDetails.value = runCatching {
                repository.loadBookDetails(
                    bookKey = screenArgs.bookKey,
                )
            }
        }
    }

    fun onScreenEvent(event: BookDetailsEvent) {
        when (event) {
            BookDetailsEvent.OnRetryClick -> loadBookDetails()
            BookDetailsEvent.OnSaveToLibraryClick -> onSaveToLibraryClick()
        }
    }

    private fun onSaveToLibraryClick() {
        val content = state.value as? BookDetailsScreenState.Content ?: return
        val book = content.bookDetails

        viewModelScope.launch {
            try {
                if (content.savedToLibrary) {
                    repository.removeBookFromLibrary(bookKey = book.key)
                } else {
                    repository.saveBookToLibrary(book = book)
                }
            } catch (e: Throwable) {
                if (e is CancellationException) throw e
            }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                BookDetailsViewModel(createSavedStateHandle())
            }
        }
    }
}