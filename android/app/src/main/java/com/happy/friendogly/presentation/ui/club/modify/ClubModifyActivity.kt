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
import androidx.fragment.app.DialogFragment
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.happy.friendogly.R
import com.happy.friendogly.databinding.ActivityClubModifyBinding
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.club.filter.bottom.ClubFilterBottomSheet
import com.happy.friendogly.presentation.ui.club.modify.bottom.ClubRecruitmentBottomSheet
import com.happy.friendogly.presentation.ui.profilesetting.bottom.EditProfileImageBottomSheet
import com.happy.friendogly.presentation.utils.customOnFocusChangeListener
import com.happy.friendogly.presentation.utils.hideKeyboard
import com.happy.friendogly.presentation.utils.intentSerializable
import com.happy.friendogly.presentation.utils.putSerializable
import com.happy.friendogly.presentation.utils.toBitmap

class ClubModifyActivity :
    BaseActivity<ActivityClubModifyBinding>(R.layout.activity_club_modify) {
    private val viewModel: ClubModifyViewModel by viewModels()

    override fun initCreateView() {
        initDataBinding()
        initEditText()
        initObserver()
        initUiModel()
    }

    private fun initDataBinding() {
        binding.vm = viewModel
    }

    private fun initUiModel() {
        val clubModifyUiModel =
            intent.intentSerializable(CLUB_MODIFY_UI_MODEL, ClubModifyUiModel.serializer())
        clubModifyUiModel?.let {
            viewModel.initUiModel(
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

                ClubModifyEvent.Navigation.NavigateSubmit -> {
                    intent.putExtra(SUCCESS_MODIFY_STATE, true)
                    setResult(RESULT_OK, intent)
                    finish()
                }

                ClubModifyEvent.Navigation.NavigateSelectState -> openSelectState()
                ClubModifyEvent.FailModify -> showSnackbar(getString(R.string.club_modify_fail))
            }
        }
    }

    private fun openSelectState(){
        val bottomSheet =
            ClubRecruitmentBottomSheet(
            ) { state ->
                viewModel.updateClubState(state)
            }
        bottomSheet.show(supportFragmentManager, "TAG")
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
