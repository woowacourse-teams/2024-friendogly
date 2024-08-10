package com.happy.friendogly.presentation.ui.otherprofile.bottom

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.happy.friendogly.R
import com.happy.friendogly.databinding.BottomSheetUserReportBinding
import com.happy.friendogly.presentation.utils.hideKeyboard
import com.happy.friendogly.presentation.utils.messageTextOnFocusChangeListener
import com.happy.friendogly.presentation.utils.showKeyboard

class BottomUserReport(
    private val onSaved: (reportType: ReportType) -> Unit,
) : BottomSheetDialogFragment() {
    private lateinit var dlg: BottomSheetDialog

    private lateinit var binding: BottomSheetUserReportBinding

    private var reportType: ReportType = ReportType.Init

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dlg = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        setStyle(STYLE_NORMAL, R.style.DialogStyle)

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
    ): View {
        binding = BottomSheetUserReportBinding.inflate(inflater, container, false)
        return binding.root
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
        binding.tvReportSave.setBackgroundResource(R.drawable.rect_gray03_fill_16)
        binding.tvReportSave.isEnabled = false
    }

    private fun initClickListener() {
        binding.tvReportSave.setOnClickListener {
            onSaved.invoke(reportType)
            dismiss()
        }

        binding.ivClose.setOnClickListener {
            dismiss()
        }

        binding.constraintLayoutReportOne.setOnClickListener {
            updateReportType(ReportType.One)
        }
        binding.constraintLayoutReportTwo.setOnClickListener {
            updateReportType(ReportType.Two)
        }
        binding.constraintLayoutReportThree.setOnClickListener {
            updateReportType(ReportType.Three)
        }
        binding.constraintLayoutReportFourth.setOnClickListener {
            updateReportType(ReportType.Fourth)
        }
        binding.constraintLayoutReportFive.setOnClickListener {
            updateReportType(ReportType.Five)
        }

        binding.editTextReport.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                updateReportType(ReportType.Five)
            }
        }
    }

    private fun updateReportType(type: ReportType) {
        when (type) {
            is ReportType.One -> {
                checkReport(binding.checkBoxReportOne)
                hideKeyboard()
                binding.editTextReport.clearFocus()
            }

            is ReportType.Two -> {
                checkReport(binding.checkBoxReportTwo)
                hideKeyboard()
                binding.editTextReport.clearFocus()
            }

            is ReportType.Three -> {
                checkReport(binding.checkBoxReportThree)
                hideKeyboard()
                binding.editTextReport.clearFocus()
            }

            is ReportType.Fourth -> {
                checkReport(binding.checkBoxReportForuth)
                hideKeyboard()
                binding.editTextReport.clearFocus()
            }

            is ReportType.Five -> {
                checkReport(binding.checkBoxReportFive)
                binding.editTextReport.messageTextOnFocusChangeListener(
                    requireContext(),
                    binding.linearLayoutEditTextReport,
                )
                binding.editTextReport.requestFocus()
                showKeyboard(binding.editTextReport)
            }

            is ReportType.Init -> Unit
        }
        reportType = type
    }

    private fun checkReport(selectedCheckBox: CheckBox) {
        binding.tvReportSave.setBackgroundResource(R.drawable.rect_coral04_fill_16)
        binding.tvReportSave.isEnabled = true
        listOf(
            binding.checkBoxReportOne,
            binding.checkBoxReportTwo,
            binding.checkBoxReportThree,
            binding.checkBoxReportForuth,
            binding.checkBoxReportFive,
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
