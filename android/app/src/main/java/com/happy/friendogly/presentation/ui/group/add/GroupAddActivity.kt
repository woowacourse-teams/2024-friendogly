package com.happy.friendogly.presentation.ui.group.add

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.happy.friendogly.R
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.databinding.ActivityGroupAddBinding
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.group.add.adapter.GroupAddAdapter
import com.happy.friendogly.presentation.ui.group.model.groupfilter.GroupFilter
import com.happy.friendogly.presentation.ui.group.select.DogSelectBottomSheet
import com.happy.friendogly.presentation.ui.profilesetting.bottom.EditProfileImageBottomSheet
import com.happy.friendogly.presentation.utils.saveBitmapToFile
import com.happy.friendogly.presentation.utils.toBitmap
import com.happy.friendogly.presentation.utils.toMultipartBody
import okhttp3.MultipartBody

class GroupAddActivity : BaseActivity<ActivityGroupAddBinding>(R.layout.activity_group_add) {
    private val viewModel: GroupAddViewModel by viewModels<GroupAddViewModel> {
        GroupAddViewModel.factory(
            getAddressUseCase = AppModule.getInstance().getAddressUseCase,
            postClubUseCase = AppModule.getInstance().postClubUseCase,
        )
    }

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

                is GroupAddEvent.Navigation.NavigateToSelectDog -> openDogSelector(actionEvent.filters)
                GroupAddEvent.Navigation.NavigateToSelectGroupPoster -> openGroupPosterBottomSheet()
                is GroupAddEvent.ChangePage -> {
                    binding.vpGroupAdd.setCurrentItem(actionEvent.page, true)
                }

                GroupAddEvent.FailLoadAddress -> showSnackbar(getString(R.string.group_add_information_fail_address))
                GroupAddEvent.FailAddGroup -> showSnackbar(getString(R.string.group_add_fail))
            }
        }
    }

    private fun openDogSelector(filters: List<GroupFilter>) {
        val bottomSheet =
            DogSelectBottomSheet(filters = filters) { dogs->
                viewModel.submitAddGroup(
                    dogs = dogs,
                    file = makePartImage(),
                )
            }
        bottomSheet.show(supportFragmentManager, "TAG")
        bottomSheet.setStyle(
            DialogFragment.STYLE_NORMAL,
            R.style.RoundCornerBottomSheetDialogTheme,
        )
    }

    private fun makePartImage(): MultipartBody.Part? {
        val bitmap = viewModel.groupPoster.value ?: return null
        val file = saveBitmapToFile(this, bitmap)
        return file.toMultipartBody()
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
