package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.Pet
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.domain.repository.PetRepository
import kotlinx.datetime.LocalDate
import okhttp3.MultipartBody
import javax.inject.Inject

class PostPetUseCase
    @Inject
    constructor(
        private val repository: PetRepository,
    ) {
        suspend operator fun invoke(
            name: String,
            description: String,
            birthday: LocalDate,
            sizeType: SizeType,
            gender: Gender,
            file: MultipartBody.Part?,
        ): DomainResult<Pet, DataError.Network> =
            repository.postPet(
                name = name,
                description = description,
                birthday = birthday,
                sizeType = sizeType,
                gender = gender,
                file = file,
            )
    }
