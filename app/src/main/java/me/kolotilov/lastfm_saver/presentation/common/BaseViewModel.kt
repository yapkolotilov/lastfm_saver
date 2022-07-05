package me.kolotilov.lastfm_saver.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import me.kolotilov.lastfm_saver.utils.Logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Base viewmodel implementation.
 */
abstract class BaseViewModel : ViewModel(), KoinComponent {

    private val _errorFormatter: ErrorFormatter by inject()

    /**
     * Message to show.
     */
    val message: Flow<String> get() = _messageFlow

    private val _messageFlow = MutableSharedFlow<String>()
    private val _logger = Logger(this)

    open fun onAttached() = Unit

    protected fun showMessage(message: Any?) {
        viewModelScope.launch {
            _messageFlow.emit(message.toString())
        }
    }

    protected suspend fun <T> runHandling(body: suspend () -> T): Result<T> {
        return runCatching {
            body()
        }.onFailure {
            _logger.tag("NETWORK_ERROR").error(it)
            val message = _errorFormatter.format(it)
            _messageFlow.emit(message)
        }
    }
}