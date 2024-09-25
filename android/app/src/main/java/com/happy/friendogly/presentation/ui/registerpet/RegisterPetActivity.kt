package com.happy.friendogly.presentation.ui.registerpet

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.happy.friendogly.R
import com.happy.friendogly.databinding.ActivityRegisterPetBinding
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.dialog.LoadingDialog
import com.happy.friendogly.presentation.ui.registerpet.bottom.EditPetBirthdayBottomSheet
import com.happy.friendogly.presentation.ui.registerpet.bottom.EditPetProfileImageBottomSheet
import com.happy.friendogly.presentation.ui.registerpet.model.PetProfile
import com.happy.friendogly.presentation.utils.customOnFocusChangeListener
import com.happy.friendogly.presentation.utils.hideKeyboard
import com.happy.friendogly.presentation.utils.putSerializable
import com.happy.friendogly.presentation.utils.saveBitmapToFile
import com.happy.friendogly.presentation.utils.toBitmap
import com.happy.friendogly.presentation.utils.toMultipartBody
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class RegisterPetActivity :
    BaseActivity<ActivityRegisterPetBinding>(R.layout.activity_register_pet) {
    private val viewModel: RegisterPetViewModel by viewModels()

    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>
    private lateinit var imageCropLauncher: ActivityResultLauncher<CropImageContractOptions>
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var cropImageOptions: CropImageOptions
    private var cameraUri: Uri? = null

    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(this) }

    override fun initCreateView() {
        initDataBinding()
        initObserve()
        initEditText()
        initImageLaunchers()
    }

    private fun initDataBinding() {
        binding.vm = viewModel

        binding.radioGroupDogSize.setOnCheckedChangeListener { _, id ->
            val petSize =
                when (id) {
                    R.id.rb_small -> PetSize.SMALL
                    R.id.rb_medium -> PetSize.MEDIUM
                    R.id.rb_large -> PetSize.LARGE
                    else -> PetSize.SMALL
                }
            viewModel.updatePetSize(petSize = petSize)
        }

        binding.radioGroupDogGender.setOnCheckedChangeListener { _, id ->
            val petGender =
                when (id) {
                    R.id.rb_male -> PetGender.MAIL
                    R.id.rb_female -> PetGender.FEMALE
                    else -> PetGender.MAIL
                }
            viewModel.updatePetGender(petGender = petGender)
        }
    }

    private fun initObserve() {
        viewModel.navigateAction.observeEvent(this) { action ->
            when (action) {
                is RegisterPetNavigationAction.NavigateToBack -> finish()
                is RegisterPetNavigationAction.NavigateToMyPage -> finish()
                is RegisterPetNavigationAction.NavigateToSetProfileImage -> editProfileImageBottomSheet()
                is RegisterPetNavigationAction.NavigateToSetBirthday ->
                    editBirthdayBottomSheet(action.year, action.month)
            }
        }

        viewModel.message.observeEvent(this) { message ->
            when (message) {
                is RegisterPetMessage.FileSizeExceedMessage -> showToastMessage(getString(R.string.file_size_exceed_message))
                is RegisterPetMessage.ServerErrorMessage -> showToastMessage(getString(R.string.server_error_message))
                is RegisterPetMessage.NoInternetMessage -> showSnackbar(getString(R.string.no_internet_message))
            }
        }

        viewModel.loading.observeEvent(this) { loading ->
            if (loading) {
                showLoadingDialog()
            } else {
                dismissLoadingDialog()
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

        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (!success) return@registerForActivityResult
                val uri = cameraUri ?: return@registerForActivityResult
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
        viewModel.updatePetProfileImage(bitmap)
        val file = saveBitmapToFile(this, bitmap)
        val partBody = file.toMultipartBody()
        viewModel.updatePetProfileFile(partBody)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initEditText() {
        binding.etUserName.customOnFocusChangeListener(this)
        binding.etDescription.customOnFocusChangeListener(this)

        binding.constraintLayoutRegisterDogMain.setOnTouchListener { _, _ ->
            this.hideKeyboard()
            binding.etUserName.clearFocus()
            binding.etDescription.clearFocus()
            false
        }
    }

    private fun editProfileImageBottomSheet() {
        val dialog =
            EditPetProfileImageBottomSheet(
                clickGallery = { imagePickerLauncher.launch("image/*") },
                clickCamera = { getCaptureImage() },
            )

        dialog.show(supportFragmentManager, "tag")
    }

    private fun getCaptureImage() {
        cameraUri = createImageFile()
        cameraUri?.let { cameraLauncher.launch(it) }
    }

    private fun createImageFile(): Uri? {
        val now = SimpleDateFormat("yyMMdd_HHmmss", java.util.Locale.getDefault()).format(Date())
        val contentValues =
            ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "img_$now.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
            }
        return this.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues,
        )
    }

    private fun editBirthdayBottomSheet(
        birthdayYear: Int,
        birthdayMonth: Int,
    ) {
        val dialog =
            EditPetBirthdayBottomSheet(
                birthdayYear = birthdayYear,
                birthdayMonth = birthdayMonth,
                clickSubmit = { year, month -> viewModel.updatePetBirthday(year, month) },
            )

        dialog.show(supportFragmentManager, "tag")
    }

    private fun showLoadingDialog() {
        loadingDialog.show()
    }

    private fun dismissLoadingDialog() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

    companion object {
        const val PUT_EXTRA_PET_PROFILE = "PUT_EXTRA_PET_PROFILE"

        fun getIntent(
            context: Context,
            petProfile: PetProfile?,
        ): Intent {
            return Intent(context, RegisterPetActivity::class.java).apply {
                petProfile ?: return@apply
                putSerializable(PUT_EXTRA_PET_PROFILE, petProfile, PetProfile.serializer())
            }
        }
    }
}
