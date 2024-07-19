package com.woowacourse.friendogly.presentation.ui.dogdetail

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.FragmentDogDetailBinding
import com.woowacourse.friendogly.presentation.base.BaseFragment
import com.woowacourse.friendogly.presentation.ui.dogdetail.adapter.DogDetailAdapter
import com.woowacourse.friendogly.presentation.ui.mypage.MyPageViewModel.Companion.dogs

class DogDetailFragment : BaseFragment<FragmentDogDetailBinding>(R.layout.fragment_dog_detail) {
    private val viewModel: DogDetailViewModel by viewModels()
    private val args: DogDetailFragmentArgs by navArgs()

    private val adapter: DogDetailAdapter by lazy { DogDetailAdapter() }

    override fun initViewCreated() {
        initDataBinding()
        initObserve()
        initAdapter()
    }

    private fun initDataBinding() {
        binding.vm = viewModel

        binding.vpDogDetail.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.updateCurrentPage(position)
                }
            },
        )
    }

    private fun initAdapter() {
        binding.vpDogDetail.adapter = adapter
    }

    private fun initObserve() {
        viewModel.uiState.observe(this) { uiState ->
            adapter.submitList(uiState.dogs)
            binding.vpDogDetail.setCurrentItem(uiState.dogs.size, false)
        }
    }
}
