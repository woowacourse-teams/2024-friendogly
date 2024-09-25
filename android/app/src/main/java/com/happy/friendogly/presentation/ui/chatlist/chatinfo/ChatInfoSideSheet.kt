package com.happy.friendogly.presentation.ui.chatlist.chatinfo

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.sidesheet.SideSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.happy.friendogly.R
import com.happy.friendogly.databinding.LayoutChatDrawerBinding
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.domain.usecase.GetChatAlarmUseCase
import com.happy.friendogly.firebase.analytics.AnalyticsHelper
import com.happy.friendogly.presentation.ui.chatlist.chat.ChatActivity
import com.happy.friendogly.presentation.ui.chatlist.chat.ChatNavigationAction
import com.happy.friendogly.presentation.ui.permission.AlarmPermission
import com.happy.friendogly.presentation.utils.logGoClubFromChatClicked
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChatInfoSideSheet : BottomSheetDialogFragment() {
    @Inject
    lateinit var getChatAlarmUseCase: GetChatAlarmUseCase

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private var _binding: LayoutChatDrawerBinding? = null
    val binding: LayoutChatDrawerBinding
        get() = requireNotNull(_binding) { "${this::class.java.simpleName} is null" }

    private lateinit var adapter: JoinPeopleAdapter
    private val alarmPermission: AlarmPermission =
        AlarmPermission.from(this, analyticsHelper) { isPermitted ->
            if (!isPermitted) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.chat_setting_alarm_alert),
                    Snackbar.LENGTH_SHORT,
                ).show()
                binding.switchChatSettingAlarm.isChecked = false
            }
        }

    private val viewModel: ChatInfoViewModel by viewModels()

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
        setChatAlarm()
        clickAlarmSetting()
    }

    private fun initChatInfo() {
        val chatId = requireNotNull(arguments?.getLong(EXTRA_CHAT_ROOM_ID, INVALID_ID))
        viewModel.getChatMember(chatRoomId = chatId)
        viewModel.getClubInfo(chatId)
        viewModel.getAlamSetting()
        observeData()
    }

    private fun observeData() {
        viewModel.clubInfo.observe(viewLifecycleOwner) { info ->
            setChatInfo(info)
            binding.btnChatClub.setOnClickListener {
                (requireActivity() as ChatActivity).navigateToClub(info.clubId)
                analyticsHelper.logGoClubFromChatClicked()
            }
        }
        viewModel.joiningPeople.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        lifecycleScope.launch {
            binding.switchChatSettingAlarm.isChecked =
                alarmPermission.hasPermissions() && getChatAlarmUseCase().getOrDefault(true)
        }
    }

    private fun clickAlarmSetting() {
        binding.switchChatSettingAlarm.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                requestNotificationPermission()
            }
            viewModel.saveAlamSetting(isChecked)
        }
    }

    private fun requestNotificationPermission() {
        if (AlarmPermission.isValidPermissionSDK() && !alarmPermission.hasPermissions()) {
            alarmPermission.createAlarmDialog().show(parentFragmentManager, "TAG")
        }
    }

    private fun setChatAlarm() {
        viewModel.alarmSetting.observe(viewLifecycleOwner) { isAlamOn ->
            val hasPermission = alarmPermission.hasPermissions()
            val isValidSDK = AlarmPermission.isValidPermissionSDK()

            with(binding) {
                switchChatSettingAlarm.isChecked =
                    if (isValidSDK) {
                        isAlamOn && hasPermission
                    } else {
                        isAlamOn
                    }
                switchChatSettingAlarm.isChecked =
                    if (isValidSDK) {
                        isAlamOn && hasPermission
                    } else {
                        isAlamOn
                    }
            }
        }
    }

    private fun setChatInfo(info: ChatInfoUiModel) {
        with(binding) {
            info.dogSize.forEach {
                when (it) {
                    SizeType.SMALL -> btnChatDogSmall.isVisible = true
                    SizeType.MEDIUM -> btnChatDogMedium.isVisible = true
                    SizeType.LARGE -> btnChatDogLarge.isVisible = true
                }
            }

            info.dogGender.forEach {
                when (it) {
                    Gender.MALE -> btnChatDogMale.isVisible = true
                    Gender.FEMALE -> btnChatDogFemale.isVisible = true
                    Gender.MALE_NEUTERED -> btnChatDogMaleNeutered.isVisible = true
                    Gender.FEMALE_NEUTERED -> btnChatDogFemaleNeutered.isVisible = true
                }
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
        private const val EXTRA_CHAT_ROOM_ID = "chatRoomId"
        private const val INVALID_ID = -1L

        fun getBundle(chatRoomId: Long): Bundle {
            return Bundle().apply {
                putLong(EXTRA_CHAT_ROOM_ID, chatRoomId)
            }
        }
    }
}
