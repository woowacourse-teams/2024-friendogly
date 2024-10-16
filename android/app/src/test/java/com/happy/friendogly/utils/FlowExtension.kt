package com.happy.friendogly.utils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlinx.coroutines.withTimeout

fun <T> Flow<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T = runBlocking {
    try {
        withTimeout(timeUnit.toMillis(time)) {
            this@getOrAwaitValue.first()
        }
    } catch (e: TimeoutException) {
        throw TimeoutException("Flow value was never emitted.")
    }
}
