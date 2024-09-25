package com.happy.friendogly.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.firebase.crashlytics.CrashlyticsHelper
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseViewModel : ViewModel() {
    @Inject
    lateinit var crashlyticsHelper: CrashlyticsHelper

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
