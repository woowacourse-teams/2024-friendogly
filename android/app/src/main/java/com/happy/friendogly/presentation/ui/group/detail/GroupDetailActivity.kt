package com.happy.friendogly.presentation.ui.group.detail

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import com.happy.friendogly.R
import com.happy.friendogly.databinding.ActivityGroupDetailBinding
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.group.detail.adapter.DetailProfileAdapter
import com.happy.friendogly.presentation.ui.group.list.adapter.filter.FilterAdapter
import com.happy.friendogly.presentation.ui.group.modify.GroupModifyBottomSheet
import com.happy.friendogly.presentation.ui.group.select.DogSelectBottomSheet

class GroupDetailActivity :
    BaseActivity<ActivityGroupDetailBinding>(R.layout.activity_group_detail) {
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
        receiveGroupId()
    }

    private fun initDataBinding() {
        binding.vm = viewModel
    }

    private fun receiveGroupId() {
        val groupId = intent.getLongExtra(KEY_GROUP_DETAIL_ID, FAIL_LOAD_DATA_ID)
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
                is GroupDetailEvent.OpenDogSelector -> {
                    val bottomSheet =
                        DogSelectBottomSheet(filters = event.filters) {
                            viewModel.joinGroup()
                        }
                    bottomSheet.show(supportFragmentManager, "TAG")
                    bottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
                }

                // TODO: delete and go chatActivity
                GroupDetailEvent.Navigation.NavigateToChat -> {
                    finish()
                }

                GroupDetailEvent.Navigation.NavigateToHome -> finish()

                is GroupDetailEvent.OpenDetailMenu -> {
                    val bottomSheet =
                        GroupModifyBottomSheet(event.detailViewType)
                    bottomSheet.show(supportFragmentManager, "TAG")
                }
            }
        }
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
