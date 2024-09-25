package com.happy.friendogly.firebase.messaging

import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject

class MessagingHelper @Inject constructor() {
    val messaging: FirebaseMessaging = FirebaseMessaging.getInstance()
}
