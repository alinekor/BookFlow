package com.example.bookflow.presentation.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

abstract class BaseViewModel : ViewModel() {

    private val _error = MutableSharedFlow<Throwable>(extraBufferCapacity = 1)
    val error: SharedFlow<Throwable> = _error.asSharedFlow()

    protected val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(TAG, "Unhandled coroutine exception", throwable)
        setError(throwable)
    }

    protected fun setError(e: Throwable) {
        _error.tryEmit(e)
    }

    protected fun launchCatching(
        dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
        onError: (Throwable) -> Unit = { setError(it) },
        block: suspend CoroutineScope.() -> Unit,
    ): Job {
        return viewModelScope.launch(dispatcher + exceptionHandler) {
            catchError(onError) { block() }
        }
    }

    protected suspend fun catchError(
        onError: (Throwable) -> Unit = { setError(it) },
        block: suspend () -> Unit,
    ) {
        try {
            block()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            Log.e(TAG, "Coroutine exception", e)
            onError(e)
        }
    }

    private companion object {
        const val TAG = "BaseViewModel"
    }
}