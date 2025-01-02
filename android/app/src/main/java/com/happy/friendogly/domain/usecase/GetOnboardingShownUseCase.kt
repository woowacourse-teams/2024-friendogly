package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.OnboardingRepository
import javax.inject.Inject

class GetOnboardingShownUseCase
    @Inject
    constructor(
        private val repository: OnboardingRepository,
    ) {
        suspend operator fun invoke(): Result<Boolean> = repository.isShown()
    }
