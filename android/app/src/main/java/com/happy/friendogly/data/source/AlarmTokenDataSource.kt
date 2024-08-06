package com.happy.friendogly.data.source

interface AlarmTokenDataSource {

    suspend fun getToken():Result<String>

    suspend fun saveToken(token:String):Result<Unit>

    suspend fun deleteToken():Result<Unit>
}
