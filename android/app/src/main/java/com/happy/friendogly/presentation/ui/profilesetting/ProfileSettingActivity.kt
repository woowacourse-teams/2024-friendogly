package com.happy.friendogly.presentation.ui.profilesetting

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.happy.friendogly.R
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.databinding.ActivityProfileSettingBinding
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.base.BaseMessage
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.MainActivity
import com.happy.friendogly.presentation.ui.profilesetting.bottom.EditProfileImageBottomSheet
import com.happy.friendogly.presentation.ui.profilesetting.model.Profile
import com.happy.friendogly.presentation.utils.customOnFocusChangeListener
import com.happy.friendogly.presentation.utils.hideKeyboard
import com.happy.friendogly.presentation.utils.putSerializable
import com.happy.friendogly.presentation.utils.saveBitmapToFile
import com.happy.friendogly.presentation.utils.toBitmap
import com.happy.friendogly.presentation.utils.toMultipartBody

class ProfileSettingActivity :
    BaseActivity<ActivityProfileSettingBinding>(R.layout.activity_profile_setting) {
    private val viewModel: ProfileSettingViewModel by viewModels {
        ProfileSettingViewModel.factory(
            postMemberUseCase = AppModule.getInstance().postMemberUseCase,
            saveJwtTokenUseCase = AppModule.getInstance().saveJwtTokenUseCase,
        )
    }

    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>
    private lateinit var imageCropLauncher: ActivityResultLauncher<CropImageContractOptions>
    private lateinit var cropImageOptions: CropImageOptions

    override fun initCreateView() {
        initDataBinding()
        initObserve()
        initImageLaunchers()
        initEditText()
    }

    private fun initDataBinding() {
        binding.vm = viewModel
    }

    private fun initObserve() {
        viewModel.message.observeEvent(this) { baseMessage ->
            when (baseMessage) {
                is BaseMessage.Toast -> showToastMessage(message = baseMessage.message)
                is BaseMessage.Snackbar ->
                    showSnackbar(message = baseMessage.message) {
                        anchorView = binding.tvEditBtn
                    }
            }
        }

        viewModel.navigateAction.observeEvent(this) { action ->
            when (action) {
                is ProfileSettingNavigationAction.NavigateToSetProfileImage -> editProfileImageBottomSheet()

                is ProfileSettingNavigationAction.NavigateToHome -> {
                    finishAffinity()
                    startActivity(MainActivity.getIntent(this))
                    finish()
                }
            }
        }
    }

    private fun initImageLaunchers() {
        cropImageOptions =
            CropImageOptions(
                fixAspectRatio = true,
                aspectRatioX = 1,
                aspectRatioY = 1,
                toolbarColor = Color.WHITE,
                toolbarBackButtonColor = Color.BLACK,
                toolbarTintColor = Color.BLACK,
                allowFlipping = false,
                allowRotation = false,
                cropMenuCropButtonTitle = getString(R.string.image_cropper_done),
                imageSourceIncludeCamera = false,
            )

        imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                if (uri == null) return@registerForActivityResult
                val cropOptions = CropImageContractOptions(uri, cropImageOptions)
                imageCropLauncher.launch(cropOptions)
            }

        imageCropLauncher =
            registerForActivityResult(CropImageContract()) { result ->
                if (result.isSuccessful) {
                    val uri = result.uriContent ?: return@registerForActivityResult
                    handleCroppedImage(uri = uri)
                }
            }
    }

    private fun handleCroppedImage(uri: Uri) {
        val bitmap = uri.toBitmap(this)
        viewModel.updateProfileImage(bitmap)
        val file = saveBitmapToFile(this, bitmap)
        val partBody = file.toMultipartBody()
        viewModel.updateProfileFile(partBody)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initEditText() {
        binding.etUserName.customOnFocusChangeListener(this)
        binding.constraintLayoutProfileSetMain.setOnTouchListener { _, _ ->
            hideKeyboard()
            binding.etUserName.clearFocus()
            false
        }
    }

    private fun editProfileImageBottomSheet() {
        val dialog =
            EditProfileImageBottomSheet(
                clickGallery = { imagePickerLauncher.launch("image/*") },
                clickDefaultImage = { viewModel.resetProfileImage() },
            )

        dialog.show(supportFragmentManager, "TAG")
    }

    companion object {
        const val PUT_EXTRA_PROFILE = "PUT_EXTRA_PROFILE"
        const val PUT_EXTRA_ACCESS_TOKEN = "PUT_EXTRA_ACCESS_TOKEN"

        fun getIntent(
            context: Context,
            accessToken: String?,
            profile: Profile?,
        ): Intent {
            return Intent(context, ProfileSettingActivity::class.java).apply {
                putExtra(PUT_EXTRA_ACCESS_TOKEN, accessToken)
                profile ?: return@apply
                putSerializable(PUT_EXTRA_PROFILE, profile, Profile.serializer())
            }
        }
    }
}
