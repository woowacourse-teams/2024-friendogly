package com.happy.friendogly.domain.mapper

import com.happy.friendogly.presentation.ui.woof.model.PetDetail
import com.happy.friendogly.presentation.ui.woof.uimodel.PetDetailUiModel

fun PetDetail.toPresentation(): PetDetailUiModel {
    return PetDetailUiModel(
        name = name,
        description = description,
        birthDate = birthDate,
        sizeType = sizeType,
        gender = gender,
        imageUrl = imageUrl,
    )
}

fun List<PetDetail>.toPresentation(): List<PetDetailUiModel> {
    return map { domain ->
        domain.toPresentation()
    }
}
