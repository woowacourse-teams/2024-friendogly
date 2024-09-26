package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.Club
import com.happy.friendogly.domain.model.ClubAddress
import com.happy.friendogly.domain.model.ClubFilterCondition
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.domain.repository.ClubRepository
import javax.inject.Inject

class GetSearchingClubsUseCase
    @Inject
    constructor(
        private val repository: ClubRepository,
    ) {
        suspend operator fun invoke(
            filterCondition: ClubFilterCondition,
            address: ClubAddress,
            genderParams: List<Gender>,
            sizeParams: List<SizeType>,
        ): Result<List<Club>> =
            repository.getSearchingClubs(
                filterCondition = filterCondition,
                address = address,
                genderParams = genderParams,
                sizeParams = sizeParams,
            )
    }
