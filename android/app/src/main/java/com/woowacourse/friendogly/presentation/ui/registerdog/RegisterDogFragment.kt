package com.woowacourse.friendogly.presentation.ui.registerdog

import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.FragmentRegisterDogBinding
import com.woowacourse.friendogly.presentation.base.BaseFragment
import com.woowacourse.friendogly.presentation.base.observeEvent
import com.woowacourse.friendogly.presentation.ui.registerdog.bottom.EditDogBirthdayBottomSheet
import com.woowacourse.friendogly.presentation.ui.registerdog.bottom.EditDogProfileImageBottomSheet
import com.woowacourse.friendogly.presentation.utils.customOnFocusChangeListener
import com.woowacourse.friendogly.presentation.utils.hideKeyboard
import com.woowacourse.friendogly.presentation.utils.saveBitmapToFile
import com.woowacourse.friendogly.presentation.utils.toBitmap
import com.woowacourse.friendogly.presentation.utils.toMultipartBody
import java.text.SimpleDateFormat
import java.util.Date

class RegisterDogFragment :
    BaseFragment<FragmentRegisterDogBinding>(R.layout.fragment_register_dog) {
    private val viewModel: RegisterDogViewModel by viewModels()
    private val navController by lazy { findNavController() }

    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>
    private lateinit var imageCropLauncher: ActivityResultLauncher<CropImageContractOptions>
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var cropImageOptions: CropImageOptions
    private var cameraUri: Uri? = null

    override fun initViewCreated() {
        initDataBinding()
        initObserve()
        initEditText()
        initImageLaunchers()
    }

    private fun initDataBinding() {
        binding.vm = viewModel

        binding.radioGroupDogSize.setOnCheckedChangeListener { _, id ->
            val dogSize =
                when (id) {
                    R.id.rb_small -> DogSize.SMALL
                    R.id.rb_medium -> DogSize.MEDIUM
                    R.id.rb_large -> DogSize.LARGE
                    else -> DogSize.SMALL
                }
            viewModel.updateDogSize(dogSize = dogSize)
        }

        binding.radioGroupDogGender.setOnCheckedChangeListener { _, id ->
            val dogGender =
                when (id) {
                    R.id.rb_male -> DogGender.MAIL
                    R.id.rb_female -> DogGender.FEMALE
                    else -> DogGender.MAIL
                }
            viewModel.updateDogGender(dogGender = dogGender)
        }
    }

    private fun initObserve() {
        viewModel.navigateAction.observeEvent(this) { action ->
            when (action) {
                is RegisterDogNavigationAction.NavigateToBack -> navController.popBackStack()
                is RegisterDogNavigationAction.NavigateToMyPage -> navController.popBackStack()
                is RegisterDogNavigationAction.NavigateToSetProfileImage -> editProfileImageBottomSheet()
                is RegisterDogNavigationAction.NavigateToSetBirthday ->
                    editBirthdayBottomSheet(action.year, action.month)
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
        val bitmap = uri.toBitmap(requireContext())
        viewModel.updateDogProfileImage(bitmap)
        val file = saveBitmapToFile(requireContext(), bitmap)
        val partBody = file.toMultipartBody()
        viewModel.updateDogProfileFile(partBody)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initEditText() {
        binding.etUserName.customOnFocusChangeListener(requireContext())
        binding.etDescription.customOnFocusChangeListener(requireContext())

        binding.constraintLayoutRegisterDogMain.setOnTouchListener { _, _ ->
            requireActivity().hideKeyboard()
            binding.etUserName.clearFocus()
            binding.etDescription.clearFocus()
            false
        }
    }

    private fun editProfileImageBottomSheet() {
        val dialog =
            EditDogProfileImageBottomSheet(
                clickGallery = { imagePickerLauncher.launch("image/*") },
                clickCamera = { getCaptureImage() },
            )

        dialog.show(parentFragmentManager, TAG)
    }

    private fun getCaptureImage() {
        cameraUri = createImageFile()
        cameraLauncher.launch(cameraUri)
    }

    private fun createImageFile(): Uri? {
        val now = SimpleDateFormat("yyMMdd_HHmmss", java.util.Locale.getDefault()).format(Date())
        val contentValues =
            ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "img_$now.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
            }
        return requireContext().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues,
        )
    }

    private fun editBirthdayBottomSheet(
        birthdayYear: Int,
        birthdayMonth: Int,
    ) {
        val dialog =
            EditDogBirthdayBottomSheet(
                birthdayYear = birthdayYear,
                birthdayMonth = birthdayMonth,
                clickSubmit = { year, month -> viewModel.updateDogBirthday(year, month) },
            )

        dialog.show(parentFragmentManager, TAG)
    }

    companion object {
        private const val TAG = "RegisterDogFragment"
    }
}
