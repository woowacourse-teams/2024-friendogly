package com.happy.friendogly.presentation.ui.club.menu

import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.happy.friendogly.presentation.ui.club.detail.model.ClubDetailViewType

@BindingAdapter("myParticipation")
fun FrameLayout.bindMyParticipation(clubDetailViewType: ClubDetailViewType) {
    this.isVisible =
        when (clubDetailViewType) {
            ClubDetailViewType.RECRUITMENT,
            ClubDetailViewType.END_RECRUITMENT,
            ClubDetailViewType.NO_AVAILABLE_PET,
            -> false

            ClubDetailViewType.PARTICIPATED,
            ClubDetailViewType.MINE,
            -> true
        }
}

@BindingAdapter("myVisibleMenu")
fun FrameLayout.bindMyVisibleMenu(clubDetailViewType: ClubDetailViewType) {
    this.isVisible = clubDetailViewType == ClubDetailViewType.MINE
}

@BindingAdapter("userVisibleMenu")
fun FrameLayout.bindUserVisibleMenu(clubDetailViewType: ClubDetailViewType) {
    this.isVisible = clubDetailViewType != ClubDetailViewType.MINE
}
