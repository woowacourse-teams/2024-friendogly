package com.happy.friendogly.presentation.ui.club.menu

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.sidesheet.SideSheetDialog
import com.happy.friendogly.R
import com.happy.friendogly.databinding.BottomSheetClubMenuBinding
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.dialog.AlertDialogModel
import com.happy.friendogly.presentation.dialog.DefaultCoralAlertDialog
import com.happy.friendogly.presentation.ui.club.common.observeClubError
import com.happy.friendogly.presentation.ui.club.detail.ClubDetailNavigation
import com.happy.friendogly.presentation.ui.club.detail.model.ClubDetailViewType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClubMenuBottomSheet(
    private val clubId: Long,
    private val clubDetailViewType: ClubDetailViewType,
) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetClubMenuBinding? = null
    val binding: BottomSheetClubMenuBinding
        get() = _binding!!

    private var toast: Toast? = null

    private val viewModel: ClubMenuViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return SideSheetDialog(requireContext(), R.style.club_detail_side_sheet_dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = BottomSheetClubMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
        viewModel.initDetailViewType(clubDetailViewType)
        initObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initObserver() {
        viewModel.clubMenuEvent.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                ClubMenuEvent.Block -> {}
                ClubMenuEvent.Report -> {}

                ClubMenuEvent.CancelSelection -> dismissNow()

                ClubMenuEvent.Delete -> openDeleteDialog()

                ClubMenuEvent.Modify -> {
                    (activity as ClubDetailNavigation).navigateToModify()
                    dismissNow()
                }

                ClubMenuEvent.Navigation.NavigateToPrev -> {
                    (activity as ClubDetailNavigation).navigateToPrevWithReload()
                    dismissNow()
                }
            }
        }

        viewModel.clubErrorHandler.observeClubError(
            owner = viewLifecycleOwner,
            sendSnackBar = { messageId ->
                makeToast(
                    requireContext().getString(messageId),
                )
                dismissNow()
            },
            sendToast = { messageId ->
                makeToast(
                    requireContext().getString(messageId),
                )
                dismissNow()
            },
        )
    }

    private fun makeToast(message: String) {
        toast?.cancel()
        toast =
            Toast.makeText(
                requireContext(),
                message,
                Toast.LENGTH_SHORT,
            )
        toast?.show()
    }

    private fun openDeleteDialog() {
        val dialog =
            DefaultCoralAlertDialog(
                alertDialogModel =
                    AlertDialogModel(
                        title = requireContext().getString(R.string.club_detail_delete_title),
                        description = null,
                        negativeContents = requireContext().getString(R.string.dialog_negative_default),
                        positiveContents = requireContext().getString(R.string.dialog_positive_default),
                    ),
                clickToNegative = { },
                clickToPositive = {
                    viewModel.withdrawClub(clubId)
                },
            )
        dialog.show(parentFragmentManager, tag)
    }
}
