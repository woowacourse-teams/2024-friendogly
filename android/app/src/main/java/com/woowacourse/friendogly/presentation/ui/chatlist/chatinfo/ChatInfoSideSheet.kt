package com.woowacourse.friendogly.presentation.ui.chatlist.chatinfo

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.sidesheet.SideSheetDialog
import com.woowacourse.friendogly.databinding.LayoutChatDrawerBinding

class ChatInfoSideSheet : BottomSheetDialogFragment() {

    private var _binding: LayoutChatDrawerBinding? = null
    val binding: LayoutChatDrawerBinding
        get() = requireNotNull(_binding) { "${this::class.java.simpleName} is null" }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
