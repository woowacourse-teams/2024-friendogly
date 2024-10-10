package com.happy.friendogly.presentation.ui.woof.mapper

import com.happy.friendogly.presentation.ui.woof.adapter.PetDetailAdapter
import com.happy.friendogly.presentation.ui.woof.model.PlaygroundInfo
import com.happy.friendogly.presentation.ui.woof.uimodel.PlaygroundInfoUiModel

fun PlaygroundInfo.toPresentation(): PlaygroundInfoUiModel =
    PlaygroundInfoUiModel(
        id = id,
        totalPetCount = totalPetCount,
        arrivedPetCount = arrivedPetCount,
        isParticipating = isParticipating,
        petDetails =
            playgroundPetDetails.mapIndexed { index, petDetail ->
                val viewType =
                    if (index == 0) PetDetailAdapter.FIRST_PET_DETAIL_VIEW_TYPE else PetDetailAdapter.PET_DETAIL_VIEW_TYPE
                petDetail.toPresentation(viewType)
            },
    )
