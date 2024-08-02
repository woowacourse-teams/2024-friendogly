package com.happy.friendogly.presentation.ui.mylocation

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.happy.friendogly.R
import com.happy.friendogly.domain.model.UserAddress

@SuppressLint("StringFormatInvalid")
@BindingAdapter("myLocation")
fun TextView.bindMyLocation(userAddress: UserAddress?){
    if (userAddress == null) return
    this.text = context.getString(
        R.string.my_location_content,
        userAddress.adminArea,
        userAddress.subLocality,
        userAddress.thoroughfare
    )
}
