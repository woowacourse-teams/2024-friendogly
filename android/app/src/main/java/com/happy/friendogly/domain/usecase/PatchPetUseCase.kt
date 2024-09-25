package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.ImageUpdateType
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.domain.repository.PetRepository
import kotlinx.datetime.LocalDate
import okhttp3.MultipartBody
import javax.inject.Inject

class PatchPetUseCase @Inject constructor(private val repository: PetRepository) {
    suspend operator fun invoke(
        petId: Long,
        name: String,
        description: String,
        birthDate: LocalDate,
        sizeType: SizeType,
        gender: Gender,
        file: MultipartBody.Part?,
        imageUpdateType: ImageUpdateType,
    ): DomainResult<Unit, DataError.Network> =
        repository.patchPet(
            petId = petId,
            name = name,
            description = description,
            birthDate = birthDate,
            sizeType = sizeType,
            gender = gender,
            file = file,
            imageUpdateType = imageUpdateType,
        )
}
