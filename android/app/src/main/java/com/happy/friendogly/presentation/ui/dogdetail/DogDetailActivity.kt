package com.happy.friendogly.presentation.ui.dogdetail

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.happy.friendogly.R
import com.happy.friendogly.databinding.ActivityDogDetailBinding
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.ui.dogdetail.adapter.DogDetailAdapter

class DogDetailActivity : BaseActivity<ActivityDogDetailBinding>(R.layout.activity_dog_detail) {
    private val viewModel: DogDetailViewModel by viewModels()

    private val adapter: DogDetailAdapter by lazy { DogDetailAdapter() }

    override fun initCreateView() {
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

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, DogDetailActivity::class.java)
        }
    }
}
