package com.happy.friendogly.presentation.ui.club.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import com.happy.friendogly.R
import com.happy.friendogly.databinding.ActivityClubDetailBinding
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.dialog.PetAddAlertDialog
import com.happy.friendogly.presentation.ui.chatlist.chat.ChatActivity
import com.happy.friendogly.presentation.ui.club.common.ClubChangeStateIntent
import com.happy.friendogly.presentation.ui.club.common.MessageHandler
import com.happy.friendogly.presentation.ui.club.common.bottom.ClubRecruitmentBottomSheet
import com.happy.friendogly.presentation.ui.club.common.handleError
import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ClubFilter
import com.happy.friendogly.presentation.ui.club.detail.adapter.DetailProfileAdapter
import com.happy.friendogly.presentation.ui.club.detail.model.ClubDetailProfileUiModel
import com.happy.friendogly.presentation.ui.club.list.adapter.filter.FilterAdapter
import com.happy.friendogly.presentation.ui.club.menu.ClubMenuBottomSheet
import com.happy.friendogly.presentation.ui.club.modify.ClubModifyActivity
import com.happy.friendogly.presentation.ui.club.modify.ClubModifyUiModel
import com.happy.friendogly.presentation.ui.club.select.PetSelectBottomSheet
import com.happy.friendogly.presentation.ui.otherprofile.OtherProfileActivity
import com.happy.friendogly.presentation.ui.petimage.PetImageActivity
import com.happy.friendogly.presentation.ui.registerpet.RegisterPetActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClubDetailActivity :
    BaseActivity<ActivityClubDetailBinding>(R.layout.activity_club_detail),
    ClubDetailNavigation {
    private lateinit var clubModifyResultLauncher: ActivityResultLauncher<Intent>

    private val viewModel: ClubDetailViewModel by viewModels()

    private val filterAdapter: FilterAdapter by lazy {
        FilterAdapter()
    }
    private val dogAdapter: DetailProfileAdapter by lazy {
        DetailProfileAdapter(this@ClubDetailActivity)
    }
    private val userAdapter: DetailProfileAdapter by lazy {
        DetailProfileAdapter(this@ClubDetailActivity)
    }

    override fun initCreateView() {
        initDataBinding()
        initAdapter()
        initObserver()
        initClub()
        initClubModifyResultLauncher()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadClub(receiveClubId())
    }

    private fun initClubModifyResultLauncher() {
        clubModifyResultLauncher =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val isModify =
                        result.data?.getBooleanExtra(
                            ClubModifyActivity.SUCCESS_MODIFY_STATE,
                            false,
                        )
                            ?: false
                    if (isModify) {
                        putLoadState()
                        viewModel.loadClub(receiveClubId())
                    }
                }
            }
    }

    private fun initDataBinding() {
        binding.vm = viewModel
    }

    private fun initClub() {
        val clubId = receiveClubId()
        if (clubId != FAIL_LOAD_DATA_ID) {
            viewModel.loadClub(clubId)
        } else {
            openFailClubDetailLoad()
        }
    }

    private fun openFailClubDetailLoad() {
        showSnackbar(resources.getString(R.string.club_detail_fail_load)) {
            setAction(resources.getString(R.string.club_detail_fail_button)) {
                finish()
            }
        }
    }

    private fun receiveClubId(): Long {
        return intent.getLongExtra(KEY_CLUB_DETAIL_ID, FAIL_LOAD_DATA_ID)
    }

    private fun initAdapter() {
        binding.rcvClubDetailFilterList.adapter = filterAdapter
        binding.rcvClubDetailDogList.adapter = dogAdapter
        binding.rcvClubDetailUserList.adapter = userAdapter
    }

    private fun initObserver() {
        viewModel.club.observe(this) { club ->
            filterAdapter.submitList(club.filters)
            dogAdapter.submitList(club.petProfiles)
            userAdapter.submitList(club.userProfiles)
        }

        viewModel.clubDetailEvent.observeEvent(this) { event ->
            when (event) {
                is ClubDetailEvent.OpenDogSelector -> openDogSelector(event.filters)

                is ClubDetailEvent.Navigation.NavigateToChat -> openChatRoom(event.chatRoomId)

                ClubDetailEvent.Navigation.NavigateToHome -> finish()

                is ClubDetailEvent.OpenDetailMenu -> {
                    val bottomSheet =
                        ClubMenuBottomSheet(
                            clubId = receiveClubId(),
                            clubDetailViewType = event.clubDetailViewType,
                        )
                    bottomSheet.show(supportFragmentManager, "TAG")
                }

                ClubDetailEvent.Navigation.NavigateToRegisterPet -> openRegisterPetDialog()

                ClubDetailEvent.Navigation.NavigateSelectState -> openSelectState()
                ClubDetailEvent.SaveReLoadState -> putLoadState()
                is ClubDetailEvent.Navigation.NavigateProfileDetail -> navigateToProfile(event.clubDetailProfileUiModel)
            }
        }

        viewModel.clubErrorHandler.error.observeEvent(this@ClubDetailActivity) {
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

    override fun navigateToModify() {
        val clubModifyUiModel = viewModel.makeClubModifyUiModel() ?: return
        val modifyIntent = makeModifyIntent(clubModifyUiModel)
        clubModifyResultLauncher.launch(modifyIntent)
    }

    private fun makeModifyIntent(modifyUiModel: ClubModifyUiModel): Intent {
        return ClubModifyActivity.getIntent(
            this@ClubDetailActivity,
            modifyUiModel,
        )
    }

    private fun openChatRoom(chatRoomId: Long) {
        startActivity(
            ChatActivity.getIntent(
                context = this@ClubDetailActivity,
                chatId = chatRoomId,
            ),
        )
    }

    private fun openSelectState() {
        val bottomSheet =
            ClubRecruitmentBottomSheet { state ->
                viewModel.submitClubStateModify(state)
            }
        bottomSheet.show(supportFragmentManager, "TAG")
    }

    private fun openDogSelector(filters: List<ClubFilter>) {
        val bottomSheet =
            PetSelectBottomSheet(filters = filters) { dogs ->
                viewModel.joinClub(dogs)
            }
        bottomSheet.show(supportFragmentManager, "TAG")
        bottomSheet.setStyle(
            DialogFragment.STYLE_NORMAL,
            R.style.RoundCornerBottomSheetDialogTheme,
        )
    }

    override fun navigateToPrevWithReload() {
        putLoadState()
        finish()
    }

    private fun openRegisterPetDialog() {
        PetAddAlertDialog(
            clickToNegative = {},
            clickToPositive = {
                startActivity(RegisterPetActivity.getIntent(this@ClubDetailActivity, null))
            },
        ).show(supportFragmentManager, "TAG")
    }

    override fun navigateToProfile(clubDetailProfileUiModel: ClubDetailProfileUiModel) {
        if (clubDetailProfileUiModel.isPet) {
            openPetDetail(clubDetailProfileUiModel.imageUrl)
        } else {
            openUserProfile(clubDetailProfileUiModel.id)
        }
    }

    private fun openPetDetail(imageUrl: String?) {
        val petImage = imageUrl ?: return
        startActivity(PetImageActivity.getIntent(this@ClubDetailActivity, petImage))
    }

    private fun openUserProfile(id: Long) {
        startActivity(OtherProfileActivity.getIntent(this, id))
    }

    private fun putLoadState() {
        intent.putExtra(ClubChangeStateIntent.CHANGE_CLUB_STATE, true)
        setResult(Activity.RESULT_OK, intent)
    }

    companion object {
        private const val KEY_CLUB_DETAIL_ID = "clubDetailId"
        const val FAIL_LOAD_DATA_ID = -1L

        fun getIntent(
            context: Context,
            clubId: Long,
        ): Intent {
            return Intent(context, ClubDetailActivity::class.java).apply {
                putExtra(KEY_CLUB_DETAIL_ID, clubId)
            }
        }
    }
}
