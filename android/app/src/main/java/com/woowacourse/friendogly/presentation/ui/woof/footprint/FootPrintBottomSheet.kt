package com.woowacourse.friendogly.presentation.ui.woof.footprint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.woowacourse.friendogly.application.FriendoglyApplication.Companion.remoteFootPrintDataSource
import com.woowacourse.friendogly.data.repository.FootPrintRepositoryImpl
import com.woowacourse.friendogly.databinding.BottomSheetFootPrintBinding

class FootPrintBottomSheet : BottomSheetDialogFragment() {
    private var _binding: BottomSheetFootPrintBinding? = null
    val binding: BottomSheetFootPrintBinding
        get() = _binding!!

    private val viewModel: FootPrintViewModel by viewModels<FootPrintViewModel> {
        FootPrintViewModel.factory(
            footPrintId = footPrintId,
            footPrintRepository =
                FootPrintRepositoryImpl(
                    remoteFootPrintDataSource,
                ),
        )
    }

    private val footPrintId: Long by lazy {
        arguments?.getLong(KEY_FOOT_PRINT_ID) ?: error("발자국 아이디를 받아오는데 실패하였습니다.")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = BottomSheetFootPrintBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initDataBinding()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initDataBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
    }

    companion object {
        private const val KEY_FOOT_PRINT_ID = "footPrintId"

        fun newInstance(footPrintId: Long): FootPrintBottomSheet {
            return FootPrintBottomSheet().apply {
                arguments =
                    Bundle().apply {
                        putLong(KEY_FOOT_PRINT_ID, footPrintId)
                    }
            }
        }
    }
}
