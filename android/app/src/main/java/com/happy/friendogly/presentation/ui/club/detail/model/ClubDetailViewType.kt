package com.happy.friendogly.presentation.ui.club.detail.model

enum class ClubDetailViewType {
    RECRUITMENT,
    END_RECRUITMENT,
    PARTICIPATED,
    MINE,
    NO_AVAILABLE_PET,
    ;

    companion object {
        fun from(
            isMine: Boolean,
            isOpenState: Boolean,
            isMyParticipated: Boolean,
            canParticipation: Boolean,
            isUserPetEmpty: Boolean,
        ): ClubDetailViewType {
            return if (isMine) {
                MINE
            } else if (isMyParticipated) {
                PARTICIPATED
            } else if (canParticipation or isUserPetEmpty) {
                RECRUITMENT
            } else if (!canParticipation && isOpenState) {
                NO_AVAILABLE_PET
            } else {
                END_RECRUITMENT
            }
        }
    }
}
