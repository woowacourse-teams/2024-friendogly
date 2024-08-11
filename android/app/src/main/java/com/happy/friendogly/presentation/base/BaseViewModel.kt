package com.happy.friendogly.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.application.di.AppModule
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseViewModel : ViewModel() {
    private val crashlyticsHelper by lazy { AppModule.getInstance().crashlyticsHelper }

    protected fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit,
    ): Job {
        val exceptionHandler =
            CoroutineExceptionHandler { _, throwable ->
                crashlyticsHelper.logError(throwable)
            }
        return viewModelScope.launch(
            context = context + exceptionHandler,
            start = start,
            block = block,
        )
    }
}
