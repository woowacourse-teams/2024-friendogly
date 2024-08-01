package com.happy.friendogly.presentation.ui.otherprofile.bottom

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.happy.friendogly.R
import com.happy.friendogly.presentation.utils.hideKeyboard
import com.happy.friendogly.presentation.utils.messageTextOnFocusChangeListener
import com.happy.friendogly.presentation.utils.showKeyboard

class BottomUserReport(
    private val onSaved: (reportType: ReportType) -> Unit,
) : BottomSheetDialogFragment() {
    private lateinit var dlg: BottomSheetDialog

    private var reportType: ReportType = ReportType.Init

    private val reportOne by lazy { requireView().findViewById<ConstraintLayout>(R.id.constraintLayout_report_one) }
    private val reportOneCheckBox by lazy { requireView().findViewById<CheckBox>(R.id.checkBox_report_one) }

    private val reportTwo by lazy { requireView().findViewById<ConstraintLayout>(R.id.constraintLayout_report_two) }
    private val reportTwoCheckBox by lazy { requireView().findViewById<CheckBox>(R.id.checkBox_report_two) }

    private val reportThree by lazy { requireView().findViewById<ConstraintLayout>(R.id.constraintLayout_report_three) }
    private val reportThreeCheckBox by lazy { requireView().findViewById<CheckBox>(R.id.checkBox_report_three) }

    private val reportFourth by lazy { requireView().findViewById<ConstraintLayout>(R.id.constraintLayout_report_fourth) }
    private val reportFourthCheckBox by lazy { requireView().findViewById<CheckBox>(R.id.checkBox_report_foruth) }

    private val reportFive by lazy { requireView().findViewById<ConstraintLayout>(R.id.constraintLayout_report_five) }
    private val reportFiveCheckBox by lazy { requireView().findViewById<CheckBox>(R.id.checkBox_report_five) }

    private val editTextReport by lazy { requireView().findViewById<EditText>(R.id.edit_text_report) }
    private val linearLayoutEditTextReport by lazy { requireView().findViewById<LinearLayout>(R.id.linear_layout_edit_text_report) }

    private val saveBtn by lazy { requireView().findViewById<TextView>(R.id.tv_report_save) }
    private val closeBtn by lazy { requireView().findViewById<ImageView>(R.id.iv_close) }

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
        return inflater.inflate(R.layout.bottom_sheet_user_report, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initViewCreated()
        initClickListener()
    }

    private fun initViewCreated() {
        saveBtn.setBackgroundResource(R.drawable.rect_gray03_fill_16)
        saveBtn.isEnabled = false
    }

    private fun initClickListener() {
        saveBtn.setOnClickListener {
            onSaved.invoke(reportType)
            dismiss()
        }

        closeBtn.setOnClickListener {
            dismiss()
        }

        reportOne.setOnClickListener {
            updateReportType(ReportType.One)
        }
        reportTwo.setOnClickListener {
            updateReportType(ReportType.Two)
        }
        reportThree.setOnClickListener {
            updateReportType(ReportType.Three)
        }
        reportFourth.setOnClickListener {
            updateReportType(ReportType.Fourth)
        }
        reportFive.setOnClickListener {
            updateReportType(ReportType.Five)
        }
    }

    private fun updateReportType(type: ReportType) {
        when (type) {
            is ReportType.One -> {
                checkReport(reportOneCheckBox)
                requireActivity().hideKeyboard()
                editTextReport.clearFocus()
            }

            is ReportType.Two -> {
                checkReport(reportTwoCheckBox)
                requireActivity().hideKeyboard()
                editTextReport.clearFocus()
            }

            is ReportType.Three -> {
                checkReport(reportThreeCheckBox)
                requireActivity().hideKeyboard()
                editTextReport.clearFocus()
            }

            is ReportType.Fourth -> {
                checkReport(reportFourthCheckBox)
                requireActivity().hideKeyboard()
                editTextReport.clearFocus()
            }

            is ReportType.Five -> {
                checkReport(reportFiveCheckBox)
                editTextReport.messageTextOnFocusChangeListener(
                    requireContext(),
                    linearLayoutEditTextReport,
                )
                requireActivity().showKeyboard(editTextReport)
                editTextReport.requestFocus()
            }

            is ReportType.Init -> Unit
        }
        reportType = type
    }

    private fun checkReport(selectedCheckBox: CheckBox) {
        saveBtn.setBackgroundResource(R.drawable.rect_coral04_fill_16)
        saveBtn.isEnabled = true
        listOf(
            reportOneCheckBox,
            reportTwoCheckBox,
            reportThreeCheckBox,
            reportFourthCheckBox,
            reportFiveCheckBox,
        ).forEach { it.isChecked = false }

        selectedCheckBox.isChecked = true
    }
}

sealed interface ReportType {
    data object Init : ReportType

    data object One : ReportType

    data object Two : ReportType

    data object Three : ReportType

    data object Fourth : ReportType

    data object Five : ReportType
}
