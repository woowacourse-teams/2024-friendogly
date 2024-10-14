package com.happy.friendogly.presentation.ui.message

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import com.happy.friendogly.R
import com.happy.friendogly.databinding.ActivityMessageBinding
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.message.action.MessageAlertAction
import com.happy.friendogly.presentation.ui.message.action.MessageNavigateAction
import com.happy.friendogly.presentation.ui.message.viewmodel.MessageViewModel
import com.happy.friendogly.presentation.ui.playground.PlaygroundFragment.Companion.EXTRA_MESSAGE_UPDATED
import com.happy.friendogly.presentation.utils.showKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

        setupEditorActionListener()
        setupTextWatcher()
    }

    private fun setupBinding() {
        binding.lifecycleOwner = this
        binding.vm = viewModel
    }

    private fun setupObserving() {
        viewModel.alertAction.observeEvent(this) { event ->
            when (event) {
                is MessageAlertAction.AlertFailToPatchPlaygroundMessage -> {
                    showSnackbar(getString(R.string.fail_to_patch_message))
                }
            }
        }

        viewModel.navigateAction.observeEvent(this) { event ->
            when (event) {
                is MessageNavigateAction.FinishMessageActivity -> {
                    val resultIntent =
                        Intent().apply {
                            putExtra(EXTRA_MESSAGE_UPDATED, event.messageUpdated)
                        }
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
            }
        }
    }

    private fun setupEditorActionListener() {
        binding.etMessage.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.clickConfirmBtn()
                true
            } else {
                false
            }
        }
    }

    private fun setupTextWatcher() {
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
