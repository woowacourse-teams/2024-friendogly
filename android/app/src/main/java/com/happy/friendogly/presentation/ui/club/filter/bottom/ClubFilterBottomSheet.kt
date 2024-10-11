package com.happy.friendogly.presentation.ui.club.filter.bottom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.happy.friendogly.R
import com.happy.friendogly.databinding.BottomSheetFilterSizeSelectorBinding
import com.happy.friendogly.databinding.BottomSheetGenderFilterSelectorBinding
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ClubFilter
import com.happy.friendogly.presentation.ui.club.filter.ClubFilterEvent
import com.happy.friendogly.presentation.ui.club.filter.ClubFilterViewModel
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClubFilterBottomSheet(
    private val clubFilterType: ClubFilter,
    private val currentFilters: List<ClubFilter>,
    private val selectFilters: (filters: List<ClubFilter>) -> Unit,
) : BottomSheetDialogFragment() {
    private var _sizeBinding: BottomSheetFilterSizeSelectorBinding? = null
    private val sizeBinding: BottomSheetFilterSizeSelectorBinding
        get() = _sizeBinding!!

    private var _genderBinding: BottomSheetGenderFilterSelectorBinding? = null
    private val genderBinding: BottomSheetGenderFilterSelectorBinding
        get() = _genderBinding!!

    private val viewModel: ClubFilterViewModel by viewModels<ClubFilterViewModel>()

    private var balloon : Balloon? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return when (clubFilterType) {
            is ClubFilter.SizeFilter -> {
                _sizeBinding =
                    BottomSheetFilterSizeSelectorBinding.inflate(inflater, container, false)
                sizeBinding.root
            }

            is ClubFilter.GenderFilter -> {
                _genderBinding =
                    BottomSheetGenderFilterSelectorBinding.inflate(inflater, container, false)
                genderBinding.root
            }
        }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initClubFilter(currentFilters)
        initDataBinding()
        initCurrentFilter()
        initObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _sizeBinding = null
        _genderBinding = null
    }

    private fun initDataBinding() {
        when (clubFilterType) {
            is ClubFilter.SizeFilter -> {
                sizeBinding.vm = viewModel
                sizeBinding.lifecycleOwner = viewLifecycleOwner
            }

            is ClubFilter.GenderFilter -> {
                genderBinding.vm = viewModel
                genderBinding.lifecycleOwner = viewLifecycleOwner
            }
        }
    }

    private fun initObserver() {
        viewModel.clubFilterEvent.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                ClubFilterEvent.CancelSelection -> dismissNow()
                is ClubFilterEvent.SelectClubFilters -> {
                    selectFilters(event.filters)
                    dismissNow()
                }

                is ClubFilterEvent.SelectParticipation -> {}
                ClubFilterEvent.OpenSizeGuide -> showSizeGuide()
            }
        }
    }

    private fun initCurrentFilter() {
        currentFilters.forEach { filter ->
            when (clubFilterType) {
                is ClubFilter.SizeFilter -> initSizeFilter(filter)

                is ClubFilter.GenderFilter -> initGenderFilter(filter)
            }
        }
    }

    private fun initSizeFilter(filter: ClubFilter) {
        with(sizeBinding) {
            when (filter) {
                ClubFilter.SizeFilter.BigDog ->
                    includeFilterBig.checkboxClubFilter.isChecked = true

                ClubFilter.SizeFilter.MediumDog ->
                    includeFilterMedium.checkboxClubFilter.isChecked = true

                ClubFilter.SizeFilter.SmallDog ->
                    includeFilterSmall.checkboxClubFilter.isChecked = true

                else -> {}
            }
        }
    }

    private fun initGenderFilter(filter: ClubFilter) {
        with(genderBinding) {
            when (filter) {
                ClubFilter.GenderFilter.Female ->
                    includeFilterFemale.checkboxClubFilter.isChecked = true

                ClubFilter.GenderFilter.Male ->
                    includeFilterMale.checkboxClubFilter.isChecked = true

                ClubFilter.GenderFilter.NeutralizingFemale ->
                    includeFilterNeutralizingFemale.checkboxClubFilter.isChecked = true

                ClubFilter.GenderFilter.NeutralizingMale ->
                    includeFilterNeutralizingMale.checkboxClubFilter.isChecked = true

                else -> {}
            }
        }
    }

    private fun showSizeGuide(){
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
        balloon?.showAlignTop(sizeBinding.btnSizeGuide)
    }
}
