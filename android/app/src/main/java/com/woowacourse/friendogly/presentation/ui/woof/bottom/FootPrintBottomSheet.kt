package com.woowacourse.friendogly.presentation.ui.woof.bottom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.woowacourse.friendogly.databinding.BottomSheetFootPrintBinding
import com.woowacourse.friendogly.presentation.ui.woof.DogUiModel
import com.woowacourse.friendogly.presentation.utils.bundleParcelable

class FootPrintBottomSheet : BottomSheetDialogFragment() {
    private var _binding: BottomSheetFootPrintBinding? = null
    val binding: BottomSheetFootPrintBinding
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
        _binding = BottomSheetFootPrintBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setUpDataBinding()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpDataBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.dog = dog
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
