package com.happy.friendogly.presentation.ui.club.filter.bottom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.happy.friendogly.R
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.databinding.BottomSheetParticipationFilterSelectorBinding
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ParticipationFilter
import com.happy.friendogly.presentation.ui.club.filter.ClubFilterEvent
import com.happy.friendogly.presentation.ui.club.filter.ClubFilterViewModel

class ParticipationFilterBottomSheet(
    private val currentParticipationFilter: ParticipationFilter,
    private val selectFilter: (ParticipationFilter) -> Unit,
) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetParticipationFilterSelectorBinding? = null
    val binding: BottomSheetParticipationFilterSelectorBinding
        get() = _binding!!

    private val viewModel: ClubFilterViewModel by viewModels<ClubFilterViewModel> {
        ClubFilterViewModel.factory(
            analyticsHelper = AppModule.getInstance().analyticsHelper,
        )
    }

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
        initParticipation()
        initObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initDataBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel

        binding.radioClubClubParticipation.setOnCheckedChangeListener { _, id ->
            val participationFilter =
                when (id) {
                    R.id.radio_club_club_entire -> ParticipationFilter.ENTIRE
                    R.id.radio_club_club_possible -> ParticipationFilter.POSSIBLE
                    R.id.radio_club_club_recruitment -> ParticipationFilter.RECRUITMENT
                    else -> ParticipationFilter.ENTIRE
                }
            viewModel.updateParticipationFilter(participationFilter)
        }
    }

    private fun initObserver() {
        viewModel.clubFilterEvent.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                ClubFilterEvent.CancelSelection -> dismissNow()
                is ClubFilterEvent.SelectParticipation -> {
                    selectFilter(event.participationFilter)
                    dismissNow()
                }

                is ClubFilterEvent.SelectClubFilters -> {}
            }
        }
    }

    private fun initParticipation() {
        viewModel.updateParticipationFilter(currentParticipationFilter)
        when (currentParticipationFilter) {
            ParticipationFilter.ENTIRE -> binding.radioClubClubEntire.isChecked = true
            ParticipationFilter.POSSIBLE -> binding.radioClubClubPossible.isChecked = true
            ParticipationFilter.RECRUITMENT -> binding.radioClubClubRecruitment.isChecked = true
        }
    }
}
