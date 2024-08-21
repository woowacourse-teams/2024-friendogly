package com.happy.friendogly.presentation.ui.mylocation

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.happy.friendogly.R
import com.happy.friendogly.domain.model.UserAddress

@SuppressLint("StringFormatInvalid")
@BindingAdapter("myLocation")
fun TextView.bindMyLocation(userAddress: UserAddress?) {
    if (userAddress == null) return
    this.text =
        context.getString(
            R.string.my_location_full_address,
            userAddress.adminArea,
            userAddress.subLocality ?: "",
            userAddress.thoroughfare ?: "",
        )
}

@BindingAdapter("myLocationIcon")
fun ImageView.bindMyLocationIcon(userAddress: UserAddress?){
    this.visibility =
        if (userAddress == null){
            View.GONE
        } else{
            View.VISIBLE
        }
}
