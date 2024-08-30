package com.happy.friendogly.data.source

interface MessagingDataSource {
    suspend fun getToken(): Result<String>
}
