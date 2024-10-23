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
import com.happy.friendogly.presentation.dialog.DefaultRedAlertDialog
import com.happy.friendogly.presentation.ui.club.common.MessageHandler
import com.happy.friendogly.presentation.ui.club.common.handleError
import com.happy.friendogly.presentation.ui.club.detail.ClubDetailNavigation
import com.happy.friendogly.presentation.ui.club.detail.model.ClubDetailViewType
import com.happy.friendogly.presentation.ui.otherprofile.bottom.BottomUserReport
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
                ClubMenuEvent.Block -> usersBlockDialog(0L)
                ClubMenuEvent.Report -> reportDialog(0L)

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

        viewModel.clubErrorHandler.error.observeEvent(viewLifecycleOwner) {
            it.handleError(
                sendMessage = { message ->
                    when (message) {
                        is MessageHandler.SendSnackBar -> {
                            makeToast(
                                requireContext().getString(message.messageId),
                            )
                            dismissNow()
                        }

                        is MessageHandler.SendToast -> {
                            makeToast(getString(message.messageId))
                            dismissNow()
                        }
                    }
                },
            )
        }
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

    private fun usersBlockDialog(id: Long) {
        val alertDialogModel =
            AlertDialogModel(
                title = getString(R.string.user_block_title),
                description = getString(R.string.user_block_description),
                positiveContents = getString(R.string.user_block_yes),
                negativeContents = getString(R.string.user_block_no),
            )
        val dialog =
            DefaultRedAlertDialog(
                alertDialogModel = alertDialogModel,
                clickToPositive = { makeToast(getString(R.string.user_block_message)) },
                clickToNegative = {},
            )
        dialog.show(parentFragmentManager, "TAG")
    }

    private fun reportDialog(id: Long) {
        val bottomSheet =
            BottomUserReport(onSaved = { reportType -> makeToast(getString(R.string.club_report_message)) })
        bottomSheet.show(parentFragmentManager, "TAG")
    }

}
