package com.happy.friendogly.presentation.base

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity<T : ViewDataBinding>(
    @LayoutRes private val layoutResourceId: Int,
) : AppCompatActivity() {
    private var _binding: T? = null
    val binding get() = requireNotNull(_binding)

    private var toast: Toast? = null
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding =
            DataBindingUtil.setContentView<T>(this, layoutResourceId).apply {
                lifecycleOwner = this@BaseActivity
            }
        initCreateView()
    }

    abstract fun initCreateView()

    fun showToastMessage(message: String) {
        toast?.cancel()
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast?.show()
    }

    fun showSnackbar(
        message: String,
        action: Snackbar.() -> Unit = {},
    ) {
        snackbar?.dismiss()
        snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).apply { action() }
        snackbar?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        toast = null
        snackbar = null
    }
}
