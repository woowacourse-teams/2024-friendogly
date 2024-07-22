package com.woowacourse.friendogly.presentation.ui.group.detail

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.ActivityGroupDetailBinding
import com.woowacourse.friendogly.presentation.base.BaseActivity
import com.woowacourse.friendogly.presentation.base.observeEvent
import com.woowacourse.friendogly.presentation.ui.group.select.DogSelectBottomSheet

class GroupDetailActivity :
    BaseActivity<ActivityGroupDetailBinding>(R.layout.activity_group_detail) {
    private val viewModel: GroupDetailViewModel by viewModels()

    override fun initCreateView() {
        initDataBinding()
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
            showSnackbar(resources.getString(R.string.group_detail_fail_load)){
                setAction(resources.getString(R.string.group_detail_fail_button)){
                    finish()
                }
            }
        }
    }

    private fun initObserver(){
        viewModel.groupDetailEvent.observeEvent(this){ event ->
            when(event){
                is GroupDetailEvent.OpenDogSelector -> {
                    val bottomSheet = DogSelectBottomSheet(filters = event.filters){
                        viewModel.joinGroup()
                    }
                    bottomSheet.show(supportFragmentManager,"TAG")
                }

                GroupDetailEvent.JoinGroup -> {
                    //TODO: delete and go chatActivity
                    finish()
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
                putExtra(KEY_GROUP_DETAIL_ID,groupId)
            }
        }
    }
}
