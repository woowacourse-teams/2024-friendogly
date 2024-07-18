package com.woowacourse.friendogly.presentation.ui.chatlist

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.presentation.ui.chatlist.uimodel.ChatDateTime
import java.time.format.DateTimeFormatter

private const val MAX_MESSAGE_COUNT = 999

@BindingAdapter("chatDateTime")
fun TextView.dateTimeString(dateTime: ChatDateTime) {
    val timeFormatter = DateTimeFormatter.ofPattern(context.getString(R.string.chat_time))
    val dateFormatter = DateTimeFormatter.ofPattern(context.getString(R.string.chat_date))

    this.text =
        when (dateTime) {
            is ChatDateTime.Today -> dateTime.value.format(timeFormatter)
            is ChatDateTime.Yesterday -> context.getString(R.string.chat_yesterday)
            is ChatDateTime.NotRecent -> dateTime.value.format(dateFormatter)
        }
}

@BindingAdapter("unreadMessageCount")
fun TextView.unreadMessage(count: Int) {
    this.text =
        if (count >= MAX_MESSAGE_COUNT) {
            context.getString(R.string.chat_unread_max_count)
        } else {
            count.toString()
        }
}
