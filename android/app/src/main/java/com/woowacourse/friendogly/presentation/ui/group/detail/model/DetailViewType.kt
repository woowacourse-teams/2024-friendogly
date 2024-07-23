package com.woowacourse.friendogly.presentation.ui.group.detail.model

enum class DetailViewType {
    RECRUITMENT,
    END_RECRUITMENT,
    PARTICIPATED,
    MINE;

    companion object {
        fun from(
            isMine: Boolean,
            isMyParticipated: Boolean,
            isParticipable: Boolean,
        ): DetailViewType {
            return if (isMine) MINE
            else if (isMyParticipated) PARTICIPATED
            else if (isParticipable) RECRUITMENT
            else END_RECRUITMENT
        }
    }
}
