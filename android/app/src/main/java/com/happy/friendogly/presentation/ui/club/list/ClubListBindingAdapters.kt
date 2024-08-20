package com.happy.friendogly.presentation.ui.club.list

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.happy.friendogly.R
import com.happy.friendogly.domain.model.ClubState
import com.happy.friendogly.domain.model.UserAddress
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.Duration

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
fun TextView.bindMyLocation(userAddress: UserAddress?) {
    this.text =
        userAddress?.adminArea ?: context.getString(R.string.club_list_my_location_default)
}

@BindingAdapter("clubStateTextStyle")
fun TextView.bindClubStateTextStyle(clubState: ClubState) {
    val textStyle =
        when (clubState) {
            ClubState.OPEN -> context.getColor(R.color.coral500)
            ClubState.CLOSED, ClubState.FULL -> context.getColor(R.color.gray700)
        }
    this.setTextColor(textStyle)
}

@BindingAdapter("clubStateText")
fun TextView.bindClubStateText(clubState: ClubState){
    this.text = when(clubState){
        ClubState.OPEN -> context.getString(R.string.club_state_open)
        ClubState.CLOSED -> context.getString(R.string.club_state_closed)
        ClubState.FULL -> context.getString(R.string.club_state_full)
    }
}
