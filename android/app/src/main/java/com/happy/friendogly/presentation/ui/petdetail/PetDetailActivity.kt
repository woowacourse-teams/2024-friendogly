package com.happy.friendogly.presentation.ui.petdetail

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.happy.friendogly.R
import com.happy.friendogly.databinding.ActivityPetDetailBinding
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.petdetail.adapter.PetDetailAdapter
import com.happy.friendogly.presentation.utils.putSerializable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
            adapter.submitList(uiState.petsDetail.data)
            binding.vpPetDetail.setCurrentItem(uiState.startPage, false)
        }

        viewModel.navigateAction.observeEvent(this) { action ->
            when (action) {
                is PetProfileNavigationAction.NavigateToBack -> finish()
            }
        }
    }

    companion object {
        const val PUT_EXTRA_CURRENT_PAGE = "PUT_EXTRA_CURRENT_PAGE"
        const val PUT_EXTRA_PETS_DETAIL = "PUT_EXTRA_PETS_DETAIL"

        fun getIntent(
            context: Context,
            currentPage: Int,
            petsDetail: PetsDetail,
        ): Intent {
            return Intent(context, PetDetailActivity::class.java)
                .apply {
                    putSerializable(
                        PUT_EXTRA_PETS_DETAIL,
                        petsDetail,
                        PetsDetail.serializer(),
                    )
                    putExtra(PUT_EXTRA_CURRENT_PAGE, currentPage)
                }
        }
    }
}
