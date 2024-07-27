package com.happy.friendogly.presentation.ui.group.filter.bottom

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.happy.friendogly.databinding.BottomSheetAllFilterSelectorBinding
import com.happy.friendogly.presentation.ui.group.filter.GroupFilterViewModel
import com.happy.friendogly.presentation.ui.group.model.groupfilter.ParticipationFilter

class AllFilterBottomSheet(
    private val currentParticipationFilter: ParticipationFilter,
    selectFilter: (ParticipationFilter) -> Unit,
    close: () -> Unit,
):BottomSheetDialogFragment(){
    private var _binding: BottomSheetAllFilterSelectorBinding? = null
    val binding: BottomSheetAllFilterSelectorBinding
        get() = _binding!!

    private val viewModel: GroupFilterViewModel by viewModels()

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
    ): View {
        _binding = BottomSheetAllFilterSelectorBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initDataBinding(){
        viewModel.initParticipationFilter(currentParticipationFilter)
    }
}
