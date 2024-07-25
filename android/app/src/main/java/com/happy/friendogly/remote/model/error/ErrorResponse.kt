package com.happy.friendogly.remote.model.error

interface ErrorResponse {
    val success: Boolean
    val status: Int
    val reason: String
    val timeStamp: String
    val path: String
}
