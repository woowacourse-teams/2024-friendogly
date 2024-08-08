package com.happy.friendogly.presentation.ui.club.modify

import android.annotation.SuppressLint
import android.app.Activity
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
import com.happy.friendogly.databinding.ActivityClubModifyBinding
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.profilesetting.bottom.EditProfileImageBottomSheet
import com.happy.friendogly.presentation.utils.customOnFocusChangeListener
import com.happy.friendogly.presentation.utils.hideKeyboard
import com.happy.friendogly.presentation.utils.intentSerializable
import com.happy.friendogly.presentation.utils.putSerializable
import com.happy.friendogly.presentation.utils.toBitmap

class ClubModifyActivity :
    BaseActivity<ActivityClubModifyBinding>(R.layout.activity_club_modify) {
    private val viewModel: ClubModifyViewModel by viewModels()

    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>
    private lateinit var imageCropLauncher: ActivityResultLauncher<CropImageContractOptions>
    private lateinit var cropImageOptions: CropImageOptions

    override fun initCreateView() {
        initDataBinding()
        initEditText()
        initObserver()
        initUiModel()
        initImageLaunchers()
    }

    private fun initDataBinding() {
        binding.vm = viewModel
    }

    private fun initUiModel() {
        val clubModifyUiModel =
            intent.intentSerializable(CLUB_MODIFY_UI_MODEL, ClubModifyUiModel.serializer())
        clubModifyUiModel?.let {
            viewModel.initUiModel(
                posterBitmap = clubModifyUiModel.clubPoster?.toUri()?.toBitmap(this),
                clubModifyUiModel = clubModifyUiModel,
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initEditText() {
        binding.etClubContent.customOnFocusChangeListener(this)
        binding.etClubSubject.customOnFocusChangeListener(this)
        binding.linearLayoutClubModify.setOnTouchListener { _, _ ->
            hideKeyboard()
            binding.etClubSubject.clearFocus()
            binding.etClubContent.clearFocus()
            false
        }
    }

    private fun initObserver() {
        viewModel.modifyEvent.observeEvent(this) { event ->
            when (event) {
                ClubModifyEvent.Navigation.NavigatePrev -> finish()
                ClubModifyEvent.Navigation.NavigateToSelectClubPoster -> openClubPosterBottomSheet()

                ClubModifyEvent.Navigation.NavigateSubmit -> {
                    intent.putExtra(SUCCESS_MODIFY_STATE, true)
                    setResult(Activity.RESULT_OK, intent)
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
        viewModel.updateClubPoster(bitmap)
    }

    private fun openClubPosterBottomSheet() {
        val dialog =
            EditProfileImageBottomSheet(
                clickGallery = { imagePickerLauncher.launch("image/*") },
                clickDefaultImage = { viewModel.updateClubPoster() },
            )

        dialog.show(supportFragmentManager, "TAG")
    }

    companion object {
        private const val CLUB_MODIFY_UI_MODEL = "clubModifyUiModel"
        const val SUCCESS_MODIFY_STATE = "successModify"

        fun getIntent(
            context: Context,
            clubModifyUiModel: ClubModifyUiModel,
        ): Intent {
            return Intent(context, ClubModifyActivity::class.java).apply {
                putSerializable(
                    CLUB_MODIFY_UI_MODEL,
                    clubModifyUiModel,
                    ClubModifyUiModel.serializer(),
                )
            }
        }
    }
}
