package com.woowacourse.friendogly.presentation.ui.group.modify

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.sidesheet.SideSheetDialog
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.BottomSheetGroupModifyBinding
import com.woowacourse.friendogly.presentation.base.observeEvent
import com.woowacourse.friendogly.presentation.ui.group.detail.model.DetailViewType

class GroupModifyBottomSheet(
    private val detailViewType: DetailViewType,
) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetGroupModifyBinding? = null
    val binding: BottomSheetGroupModifyBinding
        get() = _binding!!

    private val viewModel: GroupModifyViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return SideSheetDialog(requireContext(), R.style.group_detail_side_sheet_dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = BottomSheetGroupModifyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
        viewModel.initDetailViewType(detailViewType)
        viewModel.groupModifyEvent.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                GroupModifyEvent.Block -> {}
                GroupModifyEvent.CancelSelection -> {}

                GroupModifyEvent.Delete -> {
                }

                GroupModifyEvent.Modify -> {
                }

                GroupModifyEvent.Report -> {}
            }
            this.dismissNow()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
