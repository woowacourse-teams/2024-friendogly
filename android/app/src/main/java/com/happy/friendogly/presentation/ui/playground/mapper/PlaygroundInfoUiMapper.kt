package com.happy.friendogly.presentation.ui.playground.mapper

import com.happy.friendogly.presentation.ui.playground.adapter.PetDetailAdapter
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundInfo
import com.happy.friendogly.presentation.ui.playground.uimodel.PlaygroundInfoUiModel

fun PlaygroundInfo.toPresentation(): PlaygroundInfoUiModel =
    PlaygroundInfoUiModel(
        id = id,
        totalPetCount = totalPetCount,
        arrivedPetCount = arrivedPetCount,
        isParticipating = isParticipating,
        petDetails =
            playgroundPetDetails.map { petDetail ->
                val viewType =
                    if (petDetail.isMine) PetDetailAdapter.MY_PET_DETAIL_VIEW_TYPE else PetDetailAdapter.PET_DETAIL_VIEW_TYPE
                petDetail.toPresentation(viewType)
            },
    )
