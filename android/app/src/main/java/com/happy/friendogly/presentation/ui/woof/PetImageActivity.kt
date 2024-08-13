package com.happy.friendogly.presentation.ui.woof

import android.content.Context
import android.content.Intent
import com.happy.friendogly.R
import com.happy.friendogly.databinding.ActivityPetImageBinding
import com.happy.friendogly.presentation.base.BaseActivity

class PetImageActivity : BaseActivity<ActivityPetImageBinding>(R.layout.activity_pet_image) {
    private val petImageUrl: String by lazy {
        intent.getStringExtra(PUT_EXTRA_PET_IMAGE_URL) ?: ""
    }

    override fun initCreateView() {
        binding.petImageUrl = petImageUrl
        binding.ivPetImageClose.setOnClickListener {
            finish()
        }
    }

    companion object {
        const val PUT_EXTRA_PET_IMAGE_URL = "petImageUrl"

        fun getIntent(
            context: Context,
            petImageUrl: String,
        ): Intent {
            return Intent(context, PetImageActivity::class.java).apply {
                putExtra(PUT_EXTRA_PET_IMAGE_URL, petImageUrl)
            }
        }
    }
}
