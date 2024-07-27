package com.happy.friendogly.presentation.ui.group.filter.bottom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.happy.friendogly.databinding.BottomSheetFilterSizeSelectorBinding
import com.happy.friendogly.databinding.BottomSheetGenderFilterSelectorBinding
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.group.filter.GroupFilterEvent
import com.happy.friendogly.presentation.ui.group.filter.GroupFilterViewModel
import com.happy.friendogly.presentation.ui.group.model.groupfilter.GroupFilter

class GroupFilterBottomSheet(
    private val groupFilter: GroupFilter,
    private val currentFilters: List<GroupFilter>,
    private val selectFilters: (filters: List<GroupFilter>) -> Unit,
) : BottomSheetDialogFragment() {
    private var _sizeBinding: BottomSheetFilterSizeSelectorBinding? = null
    private val sizeBinding: BottomSheetFilterSizeSelectorBinding
        get() = _sizeBinding!!

    private var _genderBinding: BottomSheetGenderFilterSelectorBinding? = null
    private val genderBinding: BottomSheetGenderFilterSelectorBinding
        get() = _genderBinding!!

    private val viewModel: GroupFilterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return when (groupFilter) {
            is GroupFilter.SizeFilter -> {
                _sizeBinding =
                    BottomSheetFilterSizeSelectorBinding.inflate(inflater, container, false)
                sizeBinding.root
            }

            is GroupFilter.GenderFilter -> {
                _genderBinding =
                    BottomSheetGenderFilterSelectorBinding.inflate(inflater, container, false)
                genderBinding.root
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initGroupFilter(currentFilters)
        initDataBinding()
        initObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _sizeBinding = null
        _genderBinding = null
    }

    private fun initDataBinding() {
        when (groupFilter) {
            is GroupFilter.SizeFilter -> {
                sizeBinding.vm = viewModel
                sizeBinding.lifecycleOwner = viewLifecycleOwner
            }

            is GroupFilter.GenderFilter -> {
                genderBinding.vm = viewModel
                genderBinding.lifecycleOwner = viewLifecycleOwner
            }
        }
    }

    private fun initObserver() {
        viewModel.groupFilterEvent.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                GroupFilterEvent.CancelSelection -> dismissNow()
                is GroupFilterEvent.SelectGroupFilters -> {
                    selectFilters(event.filters)
                    dismissNow()
                }

                is GroupFilterEvent.SelectParticipation -> {}
            }
        }
    }
}
