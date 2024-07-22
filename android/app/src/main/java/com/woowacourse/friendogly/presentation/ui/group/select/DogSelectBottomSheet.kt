package com.woowacourse.friendogly.presentation.ui.group.select

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.woowacourse.friendogly.databinding.BottomSheetDogSelectorBinding
import com.woowacourse.friendogly.presentation.ui.group.model.groupfilter.GroupFilter
import com.woowacourse.friendogly.presentation.ui.group.select.adapter.DogSelectAdapter
import kotlin.math.abs

class DogSelectBottomSheet(
    filters: List<GroupFilter>,
    private val cancel: () -> Unit,
    private val submit: (List<Long>) -> Unit,
) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetDogSelectorBinding? = null
    val binding: BottomSheetDogSelectorBinding
        get() = _binding!!

    private val viewModel: DogSelectViewModel by viewModels {
        DogSelectViewModel.factory(filters = filters)
    }

    private val adapter: DogSelectAdapter by lazy {
        DogSelectAdapter(viewModel as DogSelectActionHandler)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = BottomSheetDogSelectorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initDataBinding()
        initObserver()
        initViewPager()
    }

    private fun initDataBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {
        viewModel.dogs.observe(viewLifecycleOwner) { dogs ->
            adapter.submitList(dogs)
        }

        viewModel.dogSelectEvent.observe(viewLifecycleOwner) {
            adapter.notifyItemChanged(binding.vpGroupSelectDogList.currentItem)
        }
    }

    private fun initViewPager() {
        val viewPager = binding.vpGroupSelectDogList
        viewPager.offscreenPageLimit = 3
        viewPager.getChildAt(0).overScrollMode = View.OVER_SCROLL_NEVER
        viewPager.adapter = adapter
        initNearViewSize()
    }

    private fun initNearViewSize() {
        val transform = CompositePageTransformer()
        transform.addTransformer(MarginPageTransformer(8))
        transform.addTransformer { view: View, fl: Float ->
            val v = 1 - abs(fl)
            view.scaleY = 0.8f + v * 0.2f
        }
        binding.vpGroupSelectDogList.setPageTransformer(transform)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
