package com.happy.friendogly.presentation.ui.petdetail

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.happy.friendogly.R
import com.happy.friendogly.databinding.ActivityPetDetailBinding
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.ui.petdetail.adapter.PetDetailAdapter

class PetDetailActivity : BaseActivity<ActivityPetDetailBinding>(R.layout.activity_pet_detail) {
    private val viewModel: PetDetailViewModel by viewModels()

    private val adapter: PetDetailAdapter by lazy { PetDetailAdapter() }

    override fun initCreateView() {
        initDataBinding()
        initObserve()
        initAdapter()
    }

    private fun initDataBinding() {
        binding.vm = viewModel

        binding.vpPetDetail.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.updateCurrentPage(position)
                }
            },
        )
    }

    private fun initAdapter() {
        binding.vpPetDetail.adapter = adapter
    }

    private fun initObserve() {
        viewModel.uiState.observe(this) { uiState ->
            adapter.submitList(uiState.dogs)
            binding.vpPetDetail.setCurrentItem(uiState.dogs.size, false)
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, PetDetailActivity::class.java)
        }
    }
}
