package com.happy.friendogly.presentation.ui.club.add.filter

import androidx.fragment.app.activityViewModels
import com.happy.friendogly.R
import com.happy.friendogly.databinding.FragmentClubAddFilterBinding
import com.happy.friendogly.presentation.base.BaseFragment
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.club.add.ClubAddViewModel
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClubAddFilterFragment :
    BaseFragment<FragmentClubAddFilterBinding>(R.layout.fragment_club_add_filter) {
    private val viewModel: ClubAddViewModel by activityViewModels()

    private var balloon: Balloon? = null

    override fun initViewCreated() {
        initDataBinding()
        observe()
        checkAllFilters()
    }

    private fun initDataBinding() {
        binding.vm = viewModel
    }

    private fun observe() {
        viewModel.clubSizeGuideEvent.observeEvent(viewLifecycleOwner) {
            showSizeGuide()
        }
    }

    private fun checkAllFilters() {
        binding.includeAddClubBig.checkboxClubFilter.isChecked = true
        binding.includeAddClubMedium.checkboxClubFilter.isChecked = true
        binding.includeAddClubSmail.checkboxClubFilter.isChecked = true
        binding.includeAddClubNeutralizingFemale.checkboxClubFilter.isChecked = true
        binding.includeAddClubNeutralizingMale.checkboxClubFilter.isChecked = true
        binding.includeAddClubMale.checkboxClubFilter.isChecked = true
        binding.includeAddClubFemale.checkboxClubFilter.isChecked = true
    }

    private fun showSizeGuide() {
        balloon?.dismiss()
        balloon =
            Balloon.Builder(requireContext()).setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP)
                .setText(getString(R.string.dog_size_guide))
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
