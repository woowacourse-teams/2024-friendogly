package com.woowacourse.friendogly.presentation.ui.group.detail.model

enum class DetailViewType {
    RECRUITMENT,
    END_RECRUITMENT,
    MINE;

    companion object {
        fun from(
            isMine: Boolean,
            isParticipable: Boolean,
        ): DetailViewType {
            return if (isMine) MINE
            else if (isParticipable) RECRUITMENT
            else END_RECRUITMENT
        }
    }
}
