package com.happy.friendogly.presentation.ui.playground

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.happy.friendogly.R
import com.happy.friendogly.databinding.BottomSheetPlaygroundBinding
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.petimage.PetImageActivity
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertActions
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundNavigateActions
import com.happy.friendogly.presentation.ui.playground.adapter.PetDetailInfoAdapter
import com.happy.friendogly.presentation.ui.playground.viewmodel.PlaygroundViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaygroundBottomSheet : BottomSheetDialogFragment() {
    private var _binding: BottomSheetPlaygroundBinding? = null
    private val binding get() = _binding!!
    private var snackbar: Snackbar? = null

    private val adapter by lazy { PetDetailInfoAdapter(viewModel) }
    private val viewModel by viewModels<PlaygroundViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = BottomSheetPlaygroundBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel

        setupObserving()
        setupRecyclerView()
        setupPetDetailInfo()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupObserving() {
        viewModel.petDetailInfo.observe(viewLifecycleOwner) { petDetailInfo ->
            adapter.submitList(petDetailInfo)
        }

        viewModel.alertActions.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is PlaygroundAlertActions.AlertFailToLoadPlaygroundInfoSnackbar ->
                    showSnackbar(resources.getString(R.string.woof_fail_to_load_playground_info))
            }
        }

        viewModel.navigateActions.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is PlaygroundNavigateActions.NavigateToPetImage -> {
                    startActivity(
                        PetImageActivity.getIntent(
                            requireContext(),
                            event.petImageUrl,
                        ),
                    )
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rcvPlayground.adapter = adapter
        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)

        binding.rcvPlayground.addItemDecoration(divider)
    }

    private fun setupPetDetailInfo() {
        val playgroundId = arguments?.getLong(EXTRA_PLAYGROUND_ID) ?: return
        viewModel.loadPetDetailInfo(playgroundId = playgroundId)
    }

    private fun showSnackbar(
        message: String,
        action: Snackbar.() -> Unit = {},
    ) {
        snackbar?.dismiss()
        snackbar =
            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).apply {
                anchorView = requireActivity().findViewById(R.id.bottom_navi)
                action()
            }
        snackbar?.show()
    }

    companion object {
        fun newInstance(playgroundId: Long): PlaygroundBottomSheet {
            return PlaygroundBottomSheet().apply {
                arguments =
                    Bundle().apply {
                        putLong(EXTRA_PLAYGROUND_ID, playgroundId)
                    }
            }
        }

        const val TAG = "PlaygroundBottomSheet"
        const val EXTRA_PLAYGROUND_ID = "playgroundId"
    }
}
