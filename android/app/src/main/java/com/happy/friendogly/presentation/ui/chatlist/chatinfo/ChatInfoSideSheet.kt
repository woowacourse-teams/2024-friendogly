package com.happy.friendogly.presentation.ui.chatlist.chatinfo

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.sidesheet.SideSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.happy.friendogly.R
import com.happy.friendogly.databinding.LayoutChatDrawerBinding
import com.happy.friendogly.presentation.ui.permission.AlarmPermission

class ChatInfoSideSheet : BottomSheetDialogFragment() {
    private var _binding: LayoutChatDrawerBinding? = null
    val binding: LayoutChatDrawerBinding
        get() = requireNotNull(_binding) { "${this::class.java.simpleName} is null" }

    private lateinit var adapter: JoinPeopleAdapter
    private val alarmPermission: AlarmPermission = AlarmPermission(requireActivity())

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

        viewModel.getChatInfo()
        viewModel.chatInfo.observe(viewLifecycleOwner) { info ->
            adapter.submitList(info.people)
            setChatInfo(info)
        }

        clickAlarmSetting()
    }

    private fun clickAlarmSetting() {
        binding.switchChatSettingAlarm.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                requestNotificationPermission()
            }
        }
    }

    private fun requestNotificationPermission() {
        if (AlarmPermission.isValidPermissionSDK()) {
            AlarmPermission(requireActivity()).askNotificationPermission { isPermitted ->
                if (!isPermitted)
                    {
                        Snackbar.make(binding.root, getString(R.string.chat_setting_alarm_alert), Snackbar.LENGTH_SHORT).show()
                    }
            }
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
        }
    }

    private fun initAdapter() {
        adapter = JoinPeopleAdapter()
        binding.rcvChatJoinPeople.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
