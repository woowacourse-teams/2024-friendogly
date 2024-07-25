package com.happy.friendogly.presentation.ui.registerdog.bottom

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.NumberPicker
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.happy.friendogly.R
import java.time.LocalDateTime

class EditDogBirthdayBottomSheet(
    private val birthdayYear: Int,
    private val birthdayMonth: Int,
    private val clickSubmit: (year: Int, month: Int) -> Unit,
) :
    BottomSheetDialogFragment() {
    private lateinit var dlg: BottomSheetDialog

    private lateinit var yearNumberPicker: NumberPicker
    private lateinit var monthNumberPicker: NumberPicker
    private lateinit var submit: TextView

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
        return inflater.inflate(R.layout.bottom_sheet_dog_edit_birthday, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        initYearPicker(view)
        initMonthPicker(view)
        initSubmit(view)
    }

    private fun initYearPicker(view: View) {
        val currentYear = LocalDateTime.now().year

        yearNumberPicker = view.findViewById(R.id.yearpicker_datepicker)
        yearNumberPicker.apply {
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            minValue = MIN_YEAR
            maxValue = currentYear
            value = birthdayYear
            displayedValues =
                (MIN_YEAR..currentYear).map { year ->
                    year.toString() + context.getString(R.string.birthday_year)
                }.toTypedArray()
        }
    }

    private fun initMonthPicker(view: View) {
        monthNumberPicker = view.findViewById(R.id.monthpicker_datepicker)
        monthNumberPicker.apply {
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            minValue = MIN_MONTH
            maxValue = MAX_MONTH
            value = birthdayMonth
            displayedValues =
                (MIN_MONTH..MAX_MONTH).map { month ->
                    context.getString(R.string.birthday_month).format(month)
                }.toTypedArray()
        }
    }

    private fun initSubmit(view: View) {
        submit = view.findViewById(R.id.tv_dog_birthday_submit)

        submit.setOnClickListener {
            clickSubmit.invoke(yearNumberPicker.value, monthNumberPicker.value)
            dismiss()
        }
    }

    companion object {
        private const val MIN_YEAR = 1970
        private const val MIN_MONTH = 1
        private const val MAX_MONTH = 12
    }
}
