package com.happy.friendogly.presentation.ui.group.detail

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.happy.friendogly.R
import com.happy.friendogly.presentation.ui.group.detail.model.GroupDetailViewType

@BindingAdapter("detailViewTypeBackground")
fun TextView.bindDetailViewTypeBackground(groupDetailViewType: GroupDetailViewType?) {
    groupDetailViewType ?: return
    val backgroundTint =
        when (groupDetailViewType) {
            GroupDetailViewType.RECRUITMENT, GroupDetailViewType.MINE, GroupDetailViewType.PARTICIPATED ->
                ContextCompat.getColorStateList(
                    context,
                    R.color.coral400,
                )

            GroupDetailViewType.END_RECRUITMENT -> ContextCompat.getColorStateList(
                context,
                R.color.gray400
            )
        }
    this.backgroundTintList = backgroundTint
}

@BindingAdapter("detailViewTypeText")
fun TextView.bindDetailViewTypeText(groupDetailViewType: GroupDetailViewType?) {
    groupDetailViewType ?: return
    val text =
        when (groupDetailViewType) {
            GroupDetailViewType.RECRUITMENT -> context.getString(R.string.group_detail_participate_text)
            GroupDetailViewType.END_RECRUITMENT -> context.getString(R.string.group_detail_un_participate_text)
            GroupDetailViewType.MINE, GroupDetailViewType.PARTICIPATED -> context.getString(R.string.group_detail_mine_text)
        }
    this.text = text
}

@BindingAdapter("detailViewTypeStyle")
fun TextView.bindDetailViewTypeStyle(groupDetailViewType: GroupDetailViewType?) {
    groupDetailViewType ?: return
    val textStyle =
        when (groupDetailViewType) {
            GroupDetailViewType.PARTICIPATED, GroupDetailViewType.RECRUITMENT, GroupDetailViewType.MINE -> R.style.Theme_AppCompat_TextView_SemiBold_White_Size16
            GroupDetailViewType.END_RECRUITMENT -> R.style.Theme_AppCompat_TextView_SemiBold_Black_Size16
        }
    this.setTextAppearance(textStyle)
}
