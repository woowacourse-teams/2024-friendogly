package com.woowacourse.friendogly.presentation.ui.group.list

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.woowacourse.friendogly.R
import java.time.Duration
import java.time.LocalDateTime

@BindingAdapter("applyParticipable")
fun TextView.bindParticipableType(isParticipable: Boolean) {
    this.text =
        if (isParticipable) {
            context.getString(R.string.group_participate_name)
        } else {
            context.getString(R.string.group_complete_participate_name)
        }
}

@BindingAdapter("groupDateTime")
fun TextView.bindGroupDateTime(dateTime: LocalDateTime?) {
    dateTime ?: return

    val now = LocalDateTime.now()
    val duration = Duration.between(dateTime, now)

    val minutes = duration.toMinutes()
    val hours = duration.toHours()
    val days = duration.toDays()

    val formattedString =
        when {
            days > 0 -> context.getString(R.string.group_list_date_days, days)
            hours > 0 -> context.getString(R.string.group_list_date_hours, hours)
            minutes > 0 -> context.getString(R.string.group_list_date_minutes, minutes)
            else -> context.getString(R.string.group_list_date_now)
        }

    this.text = formattedString
}
