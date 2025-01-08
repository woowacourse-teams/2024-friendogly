package com.happy.friendogly.data.source

interface OnboardingDataSource {
    suspend fun isShown(): Result<Boolean>

    suspend fun setShown(isShown: Boolean): Result<Unit>

    suspend fun deleteShown(): Result<Unit>
}
