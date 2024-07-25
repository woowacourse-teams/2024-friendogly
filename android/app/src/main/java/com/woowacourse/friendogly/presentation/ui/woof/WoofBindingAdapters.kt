package com.woowacourse.friendogly.presentation.ui.woof

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.woowacourse.friendogly.R

@BindingAdapter("petName", "dateOfVisit")
fun TextView.bindFootPrintWalking(
    petName: String?,
    dateOfVisit: String?,
) {
    if (petName != null && dateOfVisit != null) {
        val spannableString =
            SpannableString(
                String.format(
                    resources.getString(R.string.woof_dog_walking),
                    petName,
                    dateOfVisit,
                ),
            )
        val petNameLength = petName.length
        val dateOfVisitLength = dateOfVisit.length
        spannableString.apply {
            setSpan(
                ForegroundColorSpan(resources.getColor(R.color.orange07)),
                0,
                petNameLength,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )

            setSpan(
                ForegroundColorSpan(resources.getColor(R.color.orange05)),
                petNameLength + 2,
                petNameLength + dateOfVisitLength + 2,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
        }

        text = spannableString
    }
}
