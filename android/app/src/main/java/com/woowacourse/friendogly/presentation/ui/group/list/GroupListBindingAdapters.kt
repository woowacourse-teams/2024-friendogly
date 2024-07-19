package com.woowacourse.friendogly.presentation.ui.group.list

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.woowacourse.friendogly.R

@BindingAdapter("applyParticipable")
fun TextView.bindParticipableType(isParticipable: Boolean) {
    this.text =
        if (isParticipable) {
            context.getString(R.string.group_participate_name)
        } else {
            context.getString(R.string.group_complete_participate_name)
        }
}
