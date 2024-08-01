package com.happy.friendogly.presentation.ui.group.menu

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.sidesheet.SideSheetDialog
import com.happy.friendogly.R
import com.happy.friendogly.databinding.BottomSheetGroupMenuBinding
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.dialog.AlertDialogModel
import com.happy.friendogly.presentation.dialog.DefaultCoralAlertDialog
import com.happy.friendogly.presentation.ui.group.detail.GroupDetailNavigation
import com.happy.friendogly.presentation.ui.group.detail.model.DetailViewType

class GroupMenuBottomSheet(
    private val detailViewType: DetailViewType,
) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetGroupMenuBinding? = null
    val binding: BottomSheetGroupMenuBinding
        get() = _binding!!

    private val viewModel: GroupMenuViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return SideSheetDialog(requireContext(), R.style.group_detail_side_sheet_dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = BottomSheetGroupMenuBinding.inflate(inflater, container, false)
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
        initObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initObserver() {
        viewModel.groupMenuEvent.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                GroupMenuEvent.Block -> {}
                GroupMenuEvent.Report -> {}

                GroupMenuEvent.CancelSelection -> dismissNow()

                GroupMenuEvent.Delete -> openDeleteDialog()

                GroupMenuEvent.Modify -> {
                    (activity as GroupDetailNavigation).navigateToModify()
                    dismissNow()
                }

                GroupMenuEvent.Navigation.NavigateToPrev -> {
                    (activity as GroupDetailNavigation).navigateToPrev()
                    dismissNow()
                }
            }
        }
    }

    private fun openDeleteDialog() {
        val dialog =
            DefaultCoralAlertDialog(
                alertDialogModel =
                    AlertDialogModel(
                        title = requireContext().getString(R.string.group_detail_delete_title),
                        description = null,
                        negativeContents = requireContext().getString(R.string.dialog_negative_default),
                        positiveContents = requireContext().getString(R.string.dialog_positive_default),
                    ),
                clickToNegative = { },
                clickToPositive = {
                    viewModel.withdrawGroup()
                },
            )
        dialog.show(parentFragmentManager, tag)
    }
}
