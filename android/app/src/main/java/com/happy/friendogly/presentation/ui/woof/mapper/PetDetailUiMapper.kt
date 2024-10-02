package com.happy.friendogly.presentation.ui.woof.mapper

import com.happy.friendogly.presentation.ui.woof.model.PlaygroundPetDetail
import com.happy.friendogly.presentation.ui.woof.uimodel.PlaygroundPetDetailUiModel

fun PlaygroundPetDetail.toPresentation(viewType: Int): PlaygroundPetDetailUiModel {
    return PlaygroundPetDetailUiModel(
        memberId = memberId,
        petId = petId,
        name = name,
        birthDate = birthDate,
        sizeType = sizeType,
        gender = gender,
        imageUrl = imageUrl,
        message = message,
        isArrival = isArrival,
        isMine = isMine,
        viewType = viewType,
    )
}
