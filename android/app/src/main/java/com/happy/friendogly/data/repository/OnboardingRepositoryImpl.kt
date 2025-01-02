package com.happy.friendogly.data.repository

import com.happy.friendogly.data.source.OnboardingDataSource
import com.happy.friendogly.domain.repository.OnboardingRepository
import javax.inject.Inject

class OnboardingRepositoryImpl
    @Inject
    constructor(
        private val source: OnboardingDataSource,
    ) : OnboardingRepository {
        override suspend fun isShown(): Result<Boolean> = source.isShown()

        override suspend fun setShown(isShown: Boolean): Result<Unit> = source.setShown(isShown)

        override suspend fun deleteIsShown(): Result<Unit> = source.deleteShown()
    }
