package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.OnboardingRepository
import javax.inject.Inject

class SetOnboardingShownUseCase
    @Inject
    constructor(
        private val repository: OnboardingRepository,
    ) {
        suspend operator fun invoke(isShown: Boolean): Result<Unit> = repository.setShown(isShown)
    }
