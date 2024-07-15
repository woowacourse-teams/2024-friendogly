package com.woowacourse.friendogly.presentation.ui.registerdog.bottom

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
import com.woowacourse.friendogly.R

class EditDogProfileImageBottomSheet(
    private val clickGallery: () -> Unit,
    private val clickCamera: () -> Unit,
) : BottomSheetDialogFragment() {
    private lateinit var dlg: BottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dlg = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dlg.setOnShowListener {
            val bottomSheet = dlg.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
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
        return inflater.inflate(R.layout.bottom_sheet_dog_edit_profile_image, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        val gallery = view.findViewById<TextView>(R.id.btn_gallery_select)
        val camera = view.findViewById<TextView>(R.id.btn_camera_select)
        val close = view.findViewById<TextView>(R.id.close_btn)

        gallery.setOnClickListener {
            clickGallery.invoke()
            dismiss()
        }

        camera.setOnClickListener {
            clickCamera.invoke()
            dismiss()
        }

        close.setOnClickListener {
            dismiss()
        }
    }
}
