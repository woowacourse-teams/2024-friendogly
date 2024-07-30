package com.happy.friendogly.presentation.ui.group.modify

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.net.toUri
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.happy.friendogly.R
import com.happy.friendogly.databinding.ActivityGroupModifyBinding
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.profilesetting.bottom.EditProfileImageBottomSheet
import com.happy.friendogly.presentation.utils.intentSerializable
import com.happy.friendogly.presentation.utils.putSerializable
import com.happy.friendogly.presentation.utils.toBitmap

class GroupModifyActivity :
    BaseActivity<ActivityGroupModifyBinding>(R.layout.activity_group_modify) {

    private val viewModel: GroupModifyViewModel by viewModels()

    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>
    private lateinit var imageCropLauncher: ActivityResultLauncher<CropImageContractOptions>
    private lateinit var cropImageOptions: CropImageOptions

    override fun initCreateView() {
        initDataBinding()
        initObserver()
        initUiModel()
        initImageLaunchers()
    }

    private fun initDataBinding() {
        binding.vm = viewModel
    }

    private fun initUiModel() {
        val groupModifyUiModel =
            intent.intentSerializable(GROUP_MODIFY_UI_MODEL, GroupModifyUiModel.serializer())
        groupModifyUiModel?.let {
            viewModel.initUiModel(
                posterBitmap = groupModifyUiModel.groupPoster?.toUri()?.toBitmap(this),
                groupModifyUiModel = groupModifyUiModel,
            )
        }
    }

    private fun initObserver() {
        viewModel.modifyEvent.observeEvent(this) { event ->
            when (event) {
                GroupModifyEvent.Navigation.NavigatePrev -> finish()
                GroupModifyEvent.Navigation.NavigateToSelectGroupPoster -> openGroupPosterBottomSheet()

                GroupModifyEvent.Navigation.NavigateSubmit -> {
                    // TODO: api prev view
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
        viewModel.updateGroupPoster(bitmap)
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
        private const val GROUP_MODIFY_UI_MODEL = "groupModifyUiModel"
        fun getIntent(
            context: Context,
            groupModifyUiModel: GroupModifyUiModel,
        ): Intent {
            return Intent(context, GroupModifyActivity::class.java).apply {
                putSerializable(
                    GROUP_MODIFY_UI_MODEL,
                    groupModifyUiModel,
                    GroupModifyUiModel.serializer()
                )
            }
        }
    }
}
