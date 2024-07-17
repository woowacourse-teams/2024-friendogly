package com.woowacourse.friendogly.presentation.ui.group.select

import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.FragmentDogSelectBinding
import com.woowacourse.friendogly.presentation.base.BaseFragment
import com.woowacourse.friendogly.presentation.ui.group.select.adapter.DogSelectAdapter

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

    private fun initObserver() {
        viewModel.dogs.observe(viewLifecycleOwner) { dogs ->
            adapter.submitList(dogs)
        }
    }

    private fun initViewPager() {
        val viewPager = binding.vpGroupSelectDogList
        viewPager.offscreenPageLimit = 3
        viewPager.getChildAt(0).overScrollMode = View.OVER_SCROLL_NEVER
        viewPager.adapter = adapter
    }

}
