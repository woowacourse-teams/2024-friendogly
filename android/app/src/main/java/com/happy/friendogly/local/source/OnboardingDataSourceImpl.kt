package com.happy.friendogly.local.source

import com.happy.friendogly.data.source.OnboardingDataSource
import com.happy.friendogly.local.di.OnboardingModule
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class OnboardingDataSourceImpl
    @Inject
    constructor(
        private val onboardingModule: OnboardingModule,
    ) : OnboardingDataSource {
        override suspend fun isShown(): Result<Boolean> =
            runCatching {
                onboardingModule.isShown.first()
            }

        override suspend fun setShown(isShown: Boolean): Result<Unit> =
            runCatching {
                onboardingModule.setShown(isShown)
            }

        override suspend fun deleteShown(): Result<Unit> =
            runCatching {
                onboardingModule.deleteIsShown()
            }
    }
