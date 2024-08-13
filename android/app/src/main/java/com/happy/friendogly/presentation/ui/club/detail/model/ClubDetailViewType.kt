package com.happy.friendogly.presentation.ui.club.detail.model

enum class ClubDetailViewType {
    RECRUITMENT,
    END_RECRUITMENT,
    PARTICIPATED,
    MINE,
    ;

    companion object {
        fun from(
            isMine: Boolean,
            isMyParticipated: Boolean,
            canParticipation: Boolean,
        ): ClubDetailViewType {
            return if (isMine) {
                MINE
            } else if (isMyParticipated) {
                PARTICIPATED
            } else if (canParticipation) {
                RECRUITMENT
            } else {
                END_RECRUITMENT
            }
        }
    }
}
