package com.happy.friendogly.presentation.ui.chatlist.chatinfo

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.sidesheet.SideSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.happy.friendogly.R
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.databinding.LayoutChatDrawerBinding
import com.happy.friendogly.presentation.ui.chatlist.chat.ChatNavigationAction
import com.happy.friendogly.presentation.ui.permission.AlarmPermission
import kotlinx.coroutines.launch

class ChatInfoSideSheet : BottomSheetDialogFragment() {
    private var _binding: LayoutChatDrawerBinding? = null
    val binding: LayoutChatDrawerBinding
        get() = requireNotNull(_binding) { "${this::class.java.simpleName} is null" }

    private lateinit var adapter: JoinPeopleAdapter
    private val alarmPermission: AlarmPermission =
        AlarmPermission.from(this) { isPermitted ->
            if (!isPermitted) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.chat_setting_alarm_alert),
                    Snackbar.LENGTH_SHORT,
                ).show()
            }
        }

    private val viewModel: ChatInfoViewModel by viewModels {
        ChatInfoViewModel.factory(AppModule.getInstance().getChatMemberUseCase)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = LayoutChatDrawerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return SideSheetDialog(requireContext(), theme)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        initChatInfo()

        clickAlarmSetting()
    }

    private fun initChatInfo() {
        val memberId = requireNotNull(arguments?.getLong(EXTRA_MEMBER_ID, INVALID_ID))
        val chatId = requireNotNull(arguments?.getLong(EXTRA_CHAT_ROOM_ID, INVALID_ID))
        viewModel.getChatMember(myMemberId = memberId, chatRoomId = chatId)
        viewModel.getClubInfo()
        viewModel.clubInfo.observe(viewLifecycleOwner) { info ->
            setChatInfo(info)
        }
        viewModel.joiningPeople.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun clickAlarmSetting() {
        binding.switchChatSettingAlarm.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                requestNotificationPermission()
            }
            lifecycleScope.launch {
                AppModule.getInstance().saveChatAlarmUseCase(isChecked)
            }
        }
    }

    private fun requestNotificationPermission() {
        if (AlarmPermission.isValidPermissionSDK() && !alarmPermission.hasPermissions()) {
            alarmPermission.createAlarmDialog().show(parentFragmentManager, "TAG")
        }
    }

    private fun setChatInfo(info: ChatInfoUiModel) {
        with(binding) {
            info.dogSize.forEach {
                when (it) {
                    DogSize.SMALL -> chbChatDogSmall.isChecked = true
                    DogSize.MEDIUM -> chbChatDogMedium.isChecked = true
                    DogSize.LARGE -> chbChatDogLarge.isChecked = true
                }
            }

            info.dogGender.forEach {
                when (it) {
                    DogGender.MALE -> chbChatDogMale.isChecked = true
                    DogGender.FEMALE -> chbChatDogFemale.isChecked = true
                    DogGender.MALE_NEUTERED -> chbChatDogMaleNeutered.isChecked = true
                    DogGender.FEMALE_NEUTERED -> chbChatDogFemaleNeutered.isChecked = true
                }
            }
            lifecycleScope.launch {
                switchChatSettingAlarm.isChecked =
                    AppModule.getInstance().getChatAlarmUseCase().getOrDefault(true)
            }
        }
    }

    private fun initAdapter() {
        adapter = JoinPeopleAdapter((requireActivity() as ChatNavigationAction))
        binding.rcvChatJoinPeople.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val EXTRA_MEMBER_ID = "myMemberId"
        private const val EXTRA_CHAT_ROOM_ID = "chatRoomId"
        private const val INVALID_ID = -1L

        fun getBundle(
            myMemberId: Long,
            chatRoomId: Long,
        ): Bundle {
            return Bundle().apply {
                putLong(EXTRA_MEMBER_ID, myMemberId)
                putLong(EXTRA_CHAT_ROOM_ID, chatRoomId)
            }
        }
    }
}
