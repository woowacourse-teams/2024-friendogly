package com.happy.friendogly.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseViewModel : ViewModel() {
    private val _message: MutableLiveData<Event<BaseMessage>> = MutableLiveData()
    val message: LiveData<Event<BaseMessage>> get() = _message

    protected fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit,
    ): Job {
        val exceptionHandler =
            CoroutineExceptionHandler { _, throwable ->
                sendErrorMessage(throwable = throwable)
            }
        return viewModelScope.launch(
            context = context + exceptionHandler,
            start = start,
            block = block,
        )
    }

    fun sendErrorMessage(
        throwable: Throwable,
        type: MessageType = MessageType.TOAST,
    ) {
        sendErrorMessage(message = throwable.message.toString(), type = type)
    }

    fun sendErrorMessage(
        message: String?,
        type: MessageType = MessageType.TOAST,
    ) {
        launch {
            when (type) {
                MessageType.TOAST -> _message.emit(BaseMessage.Toast(message.orEmpty()))
                MessageType.SNACKBAR -> _message.emit(BaseMessage.Snackbar(message.orEmpty()))
            }
        }
    }
}
