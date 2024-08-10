package com.happy.friendogly.presentation.ui.otherprofile.bottom

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

class BottomUserMore(
    val callback: (type: UserMoreType) -> Unit,
) : BottomSheetDialogFragment() {
    private lateinit var dlg: BottomSheetDialog

    private val userDeclare: TextView by lazy { requireView().findViewById(R.id.user_declare_btn) }
    private val report: TextView by lazy { requireView().findViewById(R.id.report_btn) }
    private val close: TextView by lazy { requireView().findViewById(R.id.iv_close) }

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
        return inflater.inflate(R.layout.bottom_sheet_user_more, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initClickListener()
    }

    private fun initClickListener() {
        userDeclare.setOnClickListener {
            callback(UserMoreType.UserDeclare)
            dismiss()
        }
        report.setOnClickListener {
            callback(UserMoreType.Report)
            dismiss()
        }
        close.setOnClickListener {
            dismiss()
        }
    }
}

sealed interface UserMoreType {
    data object UserDeclare : UserMoreType

    data object Report : UserMoreType
}
