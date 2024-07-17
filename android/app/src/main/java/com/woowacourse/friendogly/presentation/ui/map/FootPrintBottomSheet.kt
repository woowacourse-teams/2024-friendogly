package com.woowacourse.friendogly.presentation.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.woowacourse.friendogly.databinding.BottomSheetMapBinding
import com.woowacourse.friendogly.presentation.utils.bundleParcelable

class FootPrintBottomSheet : BottomSheetDialogFragment() {
    private var _binding: BottomSheetMapBinding? = null
    val binding: BottomSheetMapBinding
        get() = _binding!!

    private val dog: DogUiModel by lazy {
        arguments?.bundleParcelable(
            KEY_DOG,
            DogUiModel::class.java,
        ) ?: error("강아지 정보를 받아오는데 실패하였습니다.")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = BottomSheetMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val KEY_DOG = "dog"

        fun newInstance(dog: DogUiModel): FootPrintBottomSheet {
            return FootPrintBottomSheet().apply {
                arguments =
                    Bundle().apply {
                        putParcelable(KEY_DOG, dog)
                    }
            }
        }
    }
}
