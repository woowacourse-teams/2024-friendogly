package com.happy.friendogly.firebase.source

import com.happy.friendogly.data.source.MessagingDataSource
import com.happy.friendogly.firebase.messaging.MessagingHelper
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class MessagingDataSourceImpl @Inject constructor(private val messagingHelper: MessagingHelper) : MessagingDataSource {
    override suspend fun getToken(): Result<String> =
        suspendCancellableCoroutine { cancellable ->
            messagingHelper.messaging.token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    if (token == null) cancellable.resume(Result.failure(Exception("No FCM token")))
                    cancellable.resume(Result.success(token))
                } else {
                    cancellable.resume(Result.failure(Exception("No FCM token")))
                }
            }
        }
}
