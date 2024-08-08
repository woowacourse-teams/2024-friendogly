package com.happy.friendogly.presentation.ui.club.list

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.happy.friendogly.R
import com.happy.friendogly.domain.model.UserAddress
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.Duration

@BindingAdapter("applyCanParticipation")
fun TextView.bindCanParticipationType(canParticipation: Boolean) {
    this.text =
        if (canParticipation) {
            context.getString(R.string.club_participate_name)
        } else {
            context.getString(R.string.club_complete_participate_name)
        }
}

@BindingAdapter("clubDateTime")
fun TextView.bindClubDateTime(dateTime: LocalDateTime?) {
    dateTime ?: return

    val now = java.time.LocalDateTime.now()
    val duration = Duration.between(dateTime.toJavaLocalDateTime(), now)

    val minutes = duration.toMinutes()
    val hours = duration.toHours()
    val days = duration.toDays()

    val formattedString =
        when {
            days > 0 -> context.getString(R.string.club_list_date_days, days)
            hours > 0 -> context.getString(R.string.club_list_date_hours, hours)
            minutes > 0 -> context.getString(R.string.club_list_date_minutes, minutes)
            else -> context.getString(R.string.club_list_date_now)
        }

    this.text = formattedString
}

@BindingAdapter("address")
fun LinearLayout.bindMyLocation(userAddress: UserAddress?) {
    this.visibility =
        if (userAddress == null) {
            View.GONE
        } else {
            View.VISIBLE
        }
}
