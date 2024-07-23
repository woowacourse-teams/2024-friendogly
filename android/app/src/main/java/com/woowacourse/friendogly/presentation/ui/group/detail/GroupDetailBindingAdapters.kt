package com.woowacourse.friendogly.presentation.ui.group.detail

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.presentation.ui.group.detail.model.DetailViewType

@BindingAdapter("detailViewType")
fun TextView.bindButtonType(detailViewType: DetailViewType?) {
    detailViewType ?: return
    val backgroundTint =
        when (detailViewType) {
            DetailViewType.RECRUITMENT, DetailViewType.MINE, DetailViewType.PARTICIPATED ->
                ContextCompat.getColorStateList(
                    context,
                    R.color.orange05,
                )

            DetailViewType.END_RECRUITMENT -> ContextCompat.getColorStateList(context, R.color.gray05)
        }
    val text =
        when (detailViewType) {
            DetailViewType.RECRUITMENT -> context.getString(R.string.group_detail_participate_text)
            DetailViewType.END_RECRUITMENT -> context.getString(R.string.group_detail_un_participate_text)
            DetailViewType.MINE, DetailViewType.PARTICIPATED -> context.getString(R.string.group_detail_mine_text)
        }
    val textStyle =
        when (detailViewType) {
            DetailViewType.RECRUITMENT, DetailViewType.MINE -> R.style.Theme_AppCompat_TextView_SemiBold_White_Size16
            DetailViewType.END_RECRUITMENT -> R.style.Theme_AppCompat_TextView_SemiBold_Black_Size16
            DetailViewType.PARTICIPATED -> TODO()
        }

    this.backgroundTintList = backgroundTint
    this.text = text
    this.setTextAppearance(textStyle)
}
