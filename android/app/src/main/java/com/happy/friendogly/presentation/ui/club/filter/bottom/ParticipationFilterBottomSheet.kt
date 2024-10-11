package com.happy.friendogly.presentation.ui.club.filter.bottom

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.happy.friendogly.R
import com.happy.friendogly.databinding.BottomSheetParticipationFilterSelectorBinding
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ParticipationFilter
import com.happy.friendogly.presentation.ui.club.filter.ClubFilterEvent
import com.happy.friendogly.presentation.ui.club.filter.ClubFilterViewModel
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ParticipationFilterBottomSheet(
    private val currentParticipationFilter: ParticipationFilter,
    private val selectFilter: (ParticipationFilter) -> Unit,
) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetParticipationFilterSelectorBinding? = null
    val binding: BottomSheetParticipationFilterSelectorBinding
        get() = _binding!!

    private val viewModel: ClubFilterViewModel by viewModels()

    private var balloon : Balloon? = null

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

                ClubFilterEvent.OpenSizeGuide -> showSizeGuide()

                else -> {}
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

    private fun showSizeGuide(){
        balloon?.dismiss()
        balloon =
            Balloon.Builder(requireContext()).setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP)
                .setText(getString(R.string.participation_guide))
                .setTextColorResource(R.color.white)
                .setTextSize(14f).setMarginBottom(10)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR).setArrowSize(10)
                .setArrowPosition(0.5f).setPadding(12).setFocusable(false).setCornerRadius(8f)
                .setBackgroundColorResource(R.color.coral400)
                .setBalloonAnimation(BalloonAnimation.ELASTIC).setLifecycleOwner(viewLifecycleOwner)
                .build()
        balloon?.showAlignTop(binding.btnSizeGuide)
    }
}
