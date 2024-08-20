package com.happy.friendogly.presentation.ui.club.modify.bottom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.happy.friendogly.databinding.BottomSheetClubRecruitmentBinding
import com.happy.friendogly.domain.model.ClubState

class ClubRecruitmentBottomSheet(
    private val clickSubmit: (clubState: ClubState) -> Unit,
) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetClubRecruitmentBinding? = null
    private val binding: BottomSheetClubRecruitmentBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = BottomSheetClubRecruitmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initClickListener() {
        binding.btnClubClose.setOnClickListener {
            clickSubmit(ClubState.CLOSE)
            dismiss()
        }

        binding.btnClubOpen.setOnClickListener {
            clickSubmit(ClubState.OPEN)
            dismiss()
        }
    }
}
