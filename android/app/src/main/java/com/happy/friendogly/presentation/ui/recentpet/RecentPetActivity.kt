package com.happy.friendogly.presentation.ui.recentpet

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.happy.friendogly.R
import com.happy.friendogly.databinding.ActivityRecentPetBinding
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.otherprofile.OtherProfileActivity
import com.happy.friendogly.presentation.ui.petimage.PetImageActivity
import com.happy.friendogly.presentation.ui.recentpet.adapter.RecentPetAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecentPetActivity : BaseActivity<ActivityRecentPetBinding>(R.layout.activity_recent_pet) {
    private val viewModel: RecentPetViewModel by viewModels()

    private val adapter by lazy { RecentPetAdapter(viewModel) }

    override fun initCreateView() {
        initDataBinding()
        initObserve()
    }

    private fun initDataBinding() {
        binding.vm = viewModel
        binding.rvRecentPet.adapter = adapter
    }

    private fun initObserve() {
        viewModel.uiState.observe(this@RecentPetActivity) { uiState ->
            adapter.submitList(uiState.recentPets)
        }

        viewModel.navigateAction.observeEvent(this@RecentPetActivity) { navigateAction ->
            when (navigateAction) {
                is RecentPetNavigationAction.NavigateToBack -> finish()
                is RecentPetNavigationAction.NavigateToOtherProfile -> {
                    val intent =
                        OtherProfileActivity.getIntent(
                            this,
                            id = navigateAction.memberId,
                        )
                    startActivity(intent)
                }

                is RecentPetNavigationAction.NavigateToPetImage -> {
                    val intent = PetImageActivity.getIntent(this, navigateAction.petImageUrl)
                    startActivity(intent)
                }
            }
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, RecentPetActivity::class.java)
        }
    }
}
