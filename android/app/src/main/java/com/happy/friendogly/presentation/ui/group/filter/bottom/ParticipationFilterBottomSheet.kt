package com.happy.friendogly.presentation.ui.group.filter.bottom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.happy.friendogly.databinding.BottomSheetParticipationFilterSelectorBinding
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.group.filter.GroupFilterEvent
import com.happy.friendogly.presentation.ui.group.filter.GroupFilterViewModel
import com.happy.friendogly.presentation.ui.group.model.groupfilter.ParticipationFilter

class ParticipationFilterBottomSheet(
    private val currentParticipationFilter: ParticipationFilter,
    private val selectFilter: (ParticipationFilter) -> Unit,
) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetParticipationFilterSelectorBinding? = null
    val binding: BottomSheetParticipationFilterSelectorBinding
        get() = _binding!!

    private val viewModel: GroupFilterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = BottomSheetParticipationFilterSelectorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initDataBinding()
        initObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initDataBinding() {
        viewModel.initParticipationFilter(currentParticipationFilter)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
    }

    private fun initObserver() {
        viewModel.groupFilterEvent.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                GroupFilterEvent.CancelSelection -> dismissNow()
                is GroupFilterEvent.SelectParticipation -> {
                    selectFilter(event.participationFilter)
                    dismissNow()
                }

                is GroupFilterEvent.SelectGroupFilters -> {}
            }
        }
    }
}
