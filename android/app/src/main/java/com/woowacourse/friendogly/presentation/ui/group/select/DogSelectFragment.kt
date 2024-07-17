package com.woowacourse.friendogly.presentation.ui.group.select

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.FragmentDogSelectBinding
import com.woowacourse.friendogly.presentation.base.BaseFragment
import com.woowacourse.friendogly.presentation.ui.group.select.adapter.DogSelectAdapter
import kotlin.math.abs

class DogSelectFragment : BaseFragment<FragmentDogSelectBinding>(R.layout.fragment_dog_select) {
    private val viewModel: DogSelectViewModel by viewModels()
    private val adapter: DogSelectAdapter by lazy {
        DogSelectAdapter(viewModel as DogSelectActionHandler)
    }

    override fun initViewCreated() {
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
}
