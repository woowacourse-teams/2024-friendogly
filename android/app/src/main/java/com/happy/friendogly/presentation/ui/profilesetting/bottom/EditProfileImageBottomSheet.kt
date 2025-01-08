package com.happy.friendogly.presentation.ui.profilesetting.bottom

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.happy.friendogly.R

class EditProfileImageBottomSheet(
    private val clickGallery: () -> Unit,
    private val clickDefaultImage: () -> Unit,
) : BottomSheetDialogFragment() {
    private lateinit var dlg: BottomSheetDialog

    private lateinit var gallery: TextView
    private lateinit var defaultImage: TextView
    private lateinit var close: TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dlg = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dlg.setOnShowListener {
            val bottomSheet =
                dlg.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            bottomSheet.setBackgroundResource(android.R.color.transparent)

            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.isDraggable = true
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dlg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_edit_profile_image, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initFindVIewById(view)
        initClickListener()
    }

    private fun initFindVIewById(view: View) {
        gallery = view.findViewById(R.id.btn_gallery_select)
        defaultImage = view.findViewById(R.id.btn_default_image_set)
        close = view.findViewById(R.id.close_btn)
    }

    private fun initClickListener() {
        gallery.setOnClickListener {
            clickGallery.invoke()
            dismiss()
        }

        defaultImage.setOnClickListener {
            clickDefaultImage.invoke()
            dismiss()
        }

        close.setOnClickListener {
            dismiss()
        }
    }
}
