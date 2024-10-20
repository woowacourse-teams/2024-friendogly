package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.domain.repository.RecentPetsRepository
import kotlinx.datetime.LocalDate
import javax.inject.Inject

class InsertRecentPetUseCase
    @Inject
    constructor(
        val repository: RecentPetsRepository,
    ) {
        suspend operator fun invoke(
            memberId: Long,
            petId: Long,
            name: String,
            imgUrl: String,
            birthday: LocalDate,
            gender: Gender,
            sizeType: SizeType,
        ): DomainResult<Unit, DataError.Local> =
            repository.insertRecentPet(
                memberId = memberId,
                petId = petId,
                imgUrl = imgUrl,
                name = name,
                birthday = birthday,
                gender = gender,
                sizeType = sizeType,
            )
    }
