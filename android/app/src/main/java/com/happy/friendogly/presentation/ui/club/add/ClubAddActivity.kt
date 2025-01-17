package com.happy.friendogly.presentation.ui.club.add

import android.app.Activity
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
import com.happy.friendogly.databinding.ActivityClubAddBinding
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.club.add.adapter.ClubAddAdapter
import com.happy.friendogly.presentation.ui.club.common.ClubChangeStateIntent
import com.happy.friendogly.presentation.ui.club.common.MessageHandler
import com.happy.friendogly.presentation.ui.club.common.handleError
import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ClubFilter
import com.happy.friendogly.presentation.ui.club.select.PetSelectBottomSheet
import com.happy.friendogly.presentation.ui.profilesetting.bottom.EditProfileImageBottomSheet
import com.happy.friendogly.presentation.utils.saveBitmapToFile
import com.happy.friendogly.presentation.utils.toBitmap
import com.happy.friendogly.presentation.utils.toMultipartBody
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody

@AndroidEntryPoint
class ClubAddActivity : BaseActivity<ActivityClubAddBinding>(R.layout.activity_club_add) {
    private val viewModel: ClubAddViewModel by viewModels()

    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>
    private lateinit var imageCropLauncher: ActivityResultLauncher<CropImageContractOptions>
    private lateinit var cropImageOptions: CropImageOptions

    override fun initCreateView() {
        initDataBinding()
        initViewPager()
        initObserver()
        initImageLaunchers()
    }

    private fun initDataBinding() {
        binding.vm = viewModel
    }

    private fun initViewPager() {
        binding.vpClubAdd.adapter = ClubAddAdapter(this@ClubAddActivity)
        binding.vpClubAdd.isUserInputEnabled = false
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

    private fun initObserver() {
        viewModel.clubAddEvent.observeEvent(this) { actionEvent ->
            when (actionEvent) {
                ClubAddEvent.Navigation.NavigateToHome -> finish()

                is ClubAddEvent.Navigation.NavigateToSelectDog -> openDogSelector(actionEvent.filters)
                ClubAddEvent.Navigation.NavigateToSelectClubPoster -> openClubPosterBottomSheet()
                is ClubAddEvent.ChangePage -> {
                    binding.vpClubAdd.setCurrentItem(actionEvent.page, true)
                }

                ClubAddEvent.FailLoadAddress -> showSnackbar(getString(R.string.club_add_information_fail_address))
                ClubAddEvent.Navigation.NavigateToHomeWithAdded -> {
                    putLoadState()
                    finish()
                }
            }
        }

        viewModel.clubErrorHandler.error.observeEvent(this@ClubAddActivity) {
            it.handleError { message ->
                when (message) {
                    is MessageHandler.SendSnackBar -> {
                        showSnackbar(getString(message.messageId)) {
                            setAction(resources.getString(R.string.club_detail_fail_button)) {
                                finish()
                            }
                        }
                    }

                    is MessageHandler.SendToast -> showToastMessage(getString(message.messageId))
                }
            }
        }
    }

    private fun putLoadState() {
        intent.putExtra(ClubChangeStateIntent.CHANGE_CLUB_STATE, true)
        setResult(Activity.RESULT_OK, intent)
    }

    private fun openDogSelector(filters: List<ClubFilter>) {
        val bottomSheet =
            PetSelectBottomSheet(filters = filters) { dogs ->
                viewModel.submitAddClub(
                    dogs = dogs,
                    file = makePartImage(),
                )
            }
        bottomSheet.show(supportFragmentManager, "TAG")
    }

    private fun makePartImage(): MultipartBody.Part? {
        val bitmap = viewModel.clubPoster.value ?: return null
        val file = saveBitmapToFile(this, bitmap)
        return file.toMultipartBody()
    }

    private fun openClubPosterBottomSheet() {
        val dialog =
            EditProfileImageBottomSheet(
                clickGallery = { imagePickerLauncher.launch("image/*") },
                clickDefaultImage = {
                    viewModel.updateClubPoster()
                },
            )

        dialog.show(supportFragmentManager, "TAG")
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, ClubAddActivity::class.java)
        }
    }
}
