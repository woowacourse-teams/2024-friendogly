package com.happy.friendogly.presentation.ui.playground.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.happy.friendogly.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {
    lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initOnboardingPager()
    }

    private fun initOnboardingPager() {
        binding.vpDescExplain.adapter = OnboardingAdapter(this)
        TabLayoutMediator(binding.tlDescIndicator, binding.vpDescExplain) { tab, position ->
        }.attach()
    }
}
