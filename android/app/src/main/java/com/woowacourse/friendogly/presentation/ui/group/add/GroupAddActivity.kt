package com.woowacourse.friendogly.presentation.ui.group.add

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
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.ActivityGroupAddBinding
import com.woowacourse.friendogly.presentation.base.BaseActivity
import com.woowacourse.friendogly.presentation.base.observeEvent
import com.woowacourse.friendogly.presentation.ui.group.add.adapter.GroupAddAdapter
import com.woowacourse.friendogly.presentation.ui.profilesetting.bottom.EditProfileImageBottomSheet
import com.woowacourse.friendogly.presentation.utils.toBitmap

class GroupAddActivity : BaseActivity<ActivityGroupAddBinding>(R.layout.activity_group_add) {
    private val viewModel: GroupAddViewModel by viewModels()

    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>
    private lateinit var imageCropLauncher: ActivityResultLauncher<CropImageContractOptions>
    private lateinit var cropImageOptions: CropImageOptions

    override fun initCreateView() {
        initDataBinding()
        initViewPager()
        initObserver()
        initImageLaunchers()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initDataBinding() {
        binding.vm = viewModel
        binding.actionHandler = viewModel
    }

    private fun initViewPager() {
        binding.vpGroupAdd.adapter = GroupAddAdapter(this@GroupAddActivity)
        binding.vpGroupAdd.isUserInputEnabled = false
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
        viewModel.updateGroupPoster(bitmap)
    }

    private fun initObserver() {
        viewModel.groupAddEvent.observeEvent(this) { actionEvent ->
            when (actionEvent) {
                GroupAddEvent.Navigation.NavigateToHome -> finish()

                GroupAddEvent.Navigation.NavigateToSelectDog -> TODO()
                GroupAddEvent.Navigation.NavigateToSelectGroupPoster -> openGroupPosterBottomSheet()
                is GroupAddEvent.ChangePage -> {
                    binding.vpGroupAdd.setCurrentItem(actionEvent.page, true)
                }
            }
        }
    }

    private fun openGroupPosterBottomSheet() {
        val dialog =
            EditProfileImageBottomSheet(
                clickGallery = { imagePickerLauncher.launch("image/*") },
                clickDefaultImage = { viewModel.updateGroupPoster() },
            )

        dialog.show(supportFragmentManager, "TAG")
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, GroupAddActivity::class.java)
        }
    }
}
