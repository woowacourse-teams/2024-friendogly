package com.happy.friendogly.presentation.ui.group.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import com.happy.friendogly.R
import com.happy.friendogly.databinding.ActivityGroupDetailBinding
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.group.detail.adapter.DetailProfileAdapter
import com.happy.friendogly.presentation.ui.group.list.adapter.filter.FilterAdapter
import com.happy.friendogly.presentation.ui.group.menu.GroupMenuBottomSheet
import com.happy.friendogly.presentation.ui.group.model.groupfilter.GroupFilter
import com.happy.friendogly.presentation.ui.group.modify.GroupModifyActivity
import com.happy.friendogly.presentation.ui.group.modify.GroupModifyUiModel
import com.happy.friendogly.presentation.ui.group.select.DogSelectBottomSheet
import com.happy.friendogly.presentation.ui.otherprofile.OtherProfileActivity

class GroupDetailActivity :
    BaseActivity<ActivityGroupDetailBinding>(R.layout.activity_group_detail),
    GroupDetailNavigation {
    private lateinit var groupModifyResultLauncher: ActivityResultLauncher<Intent>

    private val viewModel: GroupDetailViewModel by viewModels()
    private val filterAdapter: FilterAdapter by lazy {
        FilterAdapter()
    }
    private val dogAdapter: DetailProfileAdapter by lazy {
        DetailProfileAdapter()
    }
    private val userAdapter: DetailProfileAdapter by lazy {
        DetailProfileAdapter()
    }

    override fun initCreateView() {
        initDataBinding()
        initAdapter()
        initObserver()
        initGroup()
        initGroupModifyResultLauncher()
    }

    private fun initGroupModifyResultLauncher() {
        groupModifyResultLauncher =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val isModify =
                        result.data?.getBooleanExtra(
                            GroupModifyActivity.SUCCESS_MODIFY_STATE,
                            false,
                        )
                            ?: false
                    if (isModify) {
                        viewModel.loadGroup(receiveGroupId())
                    }
                }
            }
    }

    private fun initDataBinding() {
        binding.vm = viewModel
    }

    private fun initGroup() {
        val groupId = receiveGroupId()
        if (groupId != FAIL_LOAD_DATA_ID) {
            viewModel.loadGroup(groupId)
        } else {
            showSnackbar(resources.getString(R.string.group_detail_fail_load)) {
                setAction(resources.getString(R.string.group_detail_fail_button)) {
                    finish()
                }
            }
        }
    }

    private fun receiveGroupId(): Long {
        return intent.getLongExtra(KEY_GROUP_DETAIL_ID, FAIL_LOAD_DATA_ID)
    }

    private fun initAdapter() {
        binding.rcvGroupDetailFilterList.adapter = filterAdapter
        binding.rcvGroupDetailDogList.adapter = dogAdapter
        binding.rcvGroupDetailUserList.adapter = userAdapter
    }

    private fun initObserver() {
        viewModel.group.observe(this) { group ->
            filterAdapter.submitList(group.filters)
            dogAdapter.submitList(group.dogProfiles)
            userAdapter.submitList(group.userProfiles)
        }

        viewModel.groupDetailEvent.observeEvent(this) { event ->
            when (event) {
                is GroupDetailEvent.OpenDogSelector -> openDogSelector(event.filters)

                // TODO: delete and go chatActivity
                GroupDetailEvent.Navigation.NavigateToChat -> {
                    finish()
                }

                GroupDetailEvent.Navigation.NavigateToHome -> finish()

                is GroupDetailEvent.OpenDetailMenu -> {
                    val bottomSheet =
                        GroupMenuBottomSheet(event.detailViewType)
                    bottomSheet.show(supportFragmentManager, "TAG")
                }

                is GroupDetailEvent.Navigation.NavigateToProfile ->
                    startActivity(OtherProfileActivity.getIntent(this, event.id))
            }
        }
    }

    override fun navigateToModify() {
        val groupModifyUiModel = viewModel.makeGroupModifyUiModel() ?: return
        val modifyIntent = makeModifyIntent(groupModifyUiModel)
        groupModifyResultLauncher.launch(modifyIntent)
    }

    private fun makeModifyIntent(modifyUiModel: GroupModifyUiModel): Intent {
        return GroupModifyActivity.getIntent(
            this@GroupDetailActivity,
            modifyUiModel,
        )
    }

    private fun openDogSelector(filters: List<GroupFilter>) {
        val bottomSheet =
            DogSelectBottomSheet(filters = filters) {
                viewModel.joinGroup()
            }
        bottomSheet.show(supportFragmentManager, "TAG")
        bottomSheet.setStyle(
            DialogFragment.STYLE_NORMAL,
            R.style.RoundCornerBottomSheetDialogTheme,
        )
    }

    override fun navigateToPrev() {
        finish()
    }

    companion object {
        private const val KEY_GROUP_DETAIL_ID = "groupDetailId"
        private const val FAIL_LOAD_DATA_ID = -1L

        fun getIntent(
            context: Context,
            groupId: Long,
        ): Intent {
            return Intent(context, GroupDetailActivity::class.java).apply {
                putExtra(KEY_GROUP_DETAIL_ID, groupId)
            }
        }
    }
}
