package com.happy.friendogly.domain.repository

interface OnboardingRepository {
    suspend fun isShown(): Result<Boolean>

    suspend fun setShown(isShown: Boolean): Result<Unit>

    suspend fun deleteIsShown(): Result<Unit>
}
