package com.happy.friendogly.presentation.ui.playground.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.happy.friendogly.R
import com.happy.friendogly.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment() {
    private var _binding: FragmentOnboardingBinding? = null
    val binding: FragmentOnboardingBinding
        get() = requireNotNull(_binding) { "${this::class.java.simpleName} is null" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setOnboardingImage()

        setOnboardingClose()
    }

    private fun setOnboardingImage() {
        when (val position = requireNotNull(arguments?.getInt(EXTRA_ONBOARDING_POSITION))) {
            0 -> {
                binding.ivOnboarding.setImageResource(R.drawable.img_onboarding1)
                binding.btnOnboardingClose.isVisible = false
            }

            1 -> {
                binding.ivOnboarding.setImageResource(R.drawable.img_onboarding2)
                binding.btnOnboardingClose.isVisible = false
            }

            2 -> {
                binding.ivOnboarding.setImageResource(R.drawable.img_onboarding3)
                binding.btnOnboardingClose.isVisible = true
            }

            else -> throw IllegalArgumentException("${position}에 해당하는 페이지가 없습니다.")
        }
    }

    private fun setOnboardingClose() {
        binding.btnOnboardingClose.setOnClickListener {
            requireActivity().finish()
        }
    }

    companion object {
        private const val EXTRA_ONBOARDING_POSITION = "onboardingPosition"

        fun getBundle(onboardingPosition: Int): Bundle {
            return Bundle().apply {
                putInt(EXTRA_ONBOARDING_POSITION, onboardingPosition)
            }
        }
    }
}
