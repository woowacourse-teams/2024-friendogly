package com.happy.friendogly.presentation.ui.woof.footprint

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.happy.friendogly.R
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.databinding.BottomSheetMarkerBinding

class FootprintBottomSheet : BottomSheetDialogFragment() {
    private var _binding: BottomSheetMarkerBinding? = null
    val binding: BottomSheetMarkerBinding
        get() = _binding!!

    private val viewModel: FootprintViewModel by viewModels<FootprintViewModel> {
        FootprintViewModel.factory(
            footPrintId = footPrintId,
            getFootprintInfoUseCase = AppModule.getInstance().getFootprintInfoUseCase,
        )
    }

    private val footPrintId: Long by lazy {
        arguments?.getLong(KEY_FOOT_PRINT_ID) ?: error("발자국 아이디를 받아오는데 실패하였습니다.")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.marker_information_bottom_sheet_dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = BottomSheetMarkerBinding.inflate(inflater, container, false)
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

        fun newInstance(footPrintId: Long): FootprintBottomSheet {
            return FootprintBottomSheet().apply {
                arguments =
                    Bundle().apply {
                        putLong(KEY_FOOT_PRINT_ID, footPrintId)
                    }
            }
        }
    }
}
