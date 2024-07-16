package com.woowacourse.friendogly.presentation.ui.chatlist

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.presentation.utils.bindGlide1000
import com.woowacourse.friendogly.presentation.utils.bundleParcelable

class WoofBottomSheet : BottomSheetDialogFragment() {

    private lateinit var dlg: BottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dlg = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dlg.setOnShowListener {
            val bottomSheet =
                dlg.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout

            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.isDraggable = false
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dlg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_woof, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setBackgroundTransparent()

        val info = this.arguments?.bundleParcelable(EXTRA_DOG_INFO_ID, WoofDogUiModel::class.java)
            ?: error("dog info가 잘못 들어옴")

        dlg.findViewById<TextView>(R.id.tv_woof_dog_name)?.text = info.name
        dlg.findViewById<TextView>(R.id.tv_woof_dog_age)?.text = "${info.age} 살"
        dlg.findViewById<TextView>(R.id.tv_woof_dog_gender)?.text = info.gender
        dlg.findViewById<TextView>(R.id.tv_woof_dog_size)?.text = info.size
        dlg.findViewById<TextView>(R.id.tv_woof_dog_desc)?.text = info.description
        Glide.with(requireContext())
            .load(info.imageUrl)
            .into(dlg.findViewById<ImageView>(R.id.iv_woof_dog)!!)

    }

    private fun setBackgroundTransparent() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    companion object {
        private const val EXTRA_DOG_INFO_ID = "dogInfo"

        fun getBundle(dog: WoofDogUiModel): Bundle {
            return Bundle().apply { this.putParcelable(EXTRA_DOG_INFO_ID, dog) }
        }
    }


}
