package com.happy.friendogly.presentation.ui.message

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import com.happy.friendogly.R
import com.happy.friendogly.databinding.ActivityMessageBinding
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.message.action.MessageNavigateAction
import com.happy.friendogly.presentation.ui.message.viewmodel.MessageViewModel
import com.happy.friendogly.presentation.utils.showKeyboard

class MessageActivity :
    BaseActivity<ActivityMessageBinding>(R.layout.activity_message) {
    private val viewModel by viewModels<MessageViewModel>()

    override fun initCreateView() {
        binding.etMessage.requestFocus()
        showKeyboard(binding.etMessage)

        val message = intent.getStringExtra(PUT_EXTRA_MESSAGE) ?: return
        viewModel.updateMessage(message)

        setupBinding()
        setupObserving()

        binding.etMessage.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    p0: CharSequence?,
                    p1: Int,
                    p2: Int,
                    p3: Int,
                ) {
                }

                override fun onTextChanged(
                    p0: CharSequence?,
                    p1: Int,
                    p2: Int,
                    p3: Int,
                ) {
                    viewModel.updateMessage(p0.toString())
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            },
        )
    }

    private fun setupBinding() {
        binding.lifecycleOwner = this
        binding.vm = viewModel
    }

    private fun setupObserving() {
        viewModel.navigateAction.observeEvent(this) { event ->
            when (event) {
                is MessageNavigateAction.FinishMessageActivity -> finish()
            }
        }
    }

    companion object {
        private const val PUT_EXTRA_MESSAGE = "message"

        fun getIntent(
            context: Context,
            message: String,
        ): Intent {
            return Intent(context, MessageActivity::class.java).apply {
                putExtra(PUT_EXTRA_MESSAGE, message)
            }
        }
    }
}
