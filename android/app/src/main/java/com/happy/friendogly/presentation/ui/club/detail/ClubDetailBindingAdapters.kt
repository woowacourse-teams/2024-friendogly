package com.happy.friendogly.presentation.ui.club.detail

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.happy.friendogly.R
import com.happy.friendogly.presentation.ui.club.detail.model.ClubDetailViewType

@BindingAdapter("detailViewTypeBackground")
fun TextView.bindDetailViewTypeBackground(clubDetailViewType: ClubDetailViewType?) {
    clubDetailViewType ?: return
    val backgroundTint =
        when (clubDetailViewType) {
            ClubDetailViewType.RECRUITMENT, ClubDetailViewType.MINE, ClubDetailViewType.PARTICIPATED ->
                ContextCompat.getColorStateList(
                    context,
                    R.color.coral400,
                )

            ClubDetailViewType.END_RECRUITMENT ->
                ContextCompat.getColorStateList(
                    context,
                    R.color.gray400,
                )
        }
    this.backgroundTintList = backgroundTint
}

@BindingAdapter("detailViewTypeText")
fun TextView.bindDetailViewTypeText(clubDetailViewType: ClubDetailViewType?) {
    clubDetailViewType ?: return
    val text =
        when (clubDetailViewType) {
            ClubDetailViewType.RECRUITMENT -> context.getString(R.string.club_detail_participate_text)
            ClubDetailViewType.END_RECRUITMENT -> context.getString(R.string.club_detail_un_participate_text)
            ClubDetailViewType.MINE, ClubDetailViewType.PARTICIPATED -> context.getString(R.string.club_detail_mine_text)
        }
    this.text = text
}

@BindingAdapter("detailViewTypeStyle")
fun TextView.bindDetailViewTypeStyle(clubDetailViewType: ClubDetailViewType?) {
    clubDetailViewType ?: return
    val textStyle =
        when (clubDetailViewType) {
            ClubDetailViewType.PARTICIPATED,
            ClubDetailViewType.RECRUITMENT,
            ClubDetailViewType.MINE,
            -> R.style.Theme_AppCompat_TextView_SemiBold_White_Size16
            ClubDetailViewType.END_RECRUITMENT -> R.style.Theme_AppCompat_TextView_SemiBold_Black_Size16
        }
    this.setTextAppearance(textStyle)
}
