package com.happy.friendogly.domain.model

enum class GroupDetailViewType {
    RECRUITMENT,
    END_RECRUITMENT,
    PARTICIPATED,
    MINE,
    ;

    companion object {
        fun from(
            isMine: Boolean,
            isMyParticipated: Boolean,
            isParticipable: Boolean,
        ): GroupDetailViewType {
            return if (isMine) {
                MINE
            } else if (isMyParticipated) {
                PARTICIPATED
            } else if (isParticipable) {
                RECRUITMENT
            } else {
                END_RECRUITMENT
            }
        }
    }
}
