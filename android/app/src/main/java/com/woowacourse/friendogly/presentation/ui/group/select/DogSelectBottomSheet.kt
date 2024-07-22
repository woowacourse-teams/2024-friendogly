package com.woowacourse.friendogly.presentation.ui.group.select

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.BottomSheetDogSelectorBinding
import com.woowacourse.friendogly.presentation.base.observeEvent
import com.woowacourse.friendogly.presentation.ui.group.model.groupfilter.GroupFilter
import com.woowacourse.friendogly.presentation.ui.group.select.adapter.DogSelectAdapter
import kotlin.math.abs

class DogSelectBottomSheet(
    private val filters: List<GroupFilter>,
    private val submit: (List<Long>) -> Unit,
) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetDogSelectorBinding? = null
    val binding: BottomSheetDogSelectorBinding
        get() = _binding!!

    private lateinit var dlg: BottomSheetDialog
    private var toast: Toast? = null

    private val viewModel: DogSelectViewModel by viewModels()

    private val adapter: DogSelectAdapter by lazy {
        DogSelectAdapter(viewModel as DogSelectActionHandler)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dlg = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dlg.setOnShowListener {
            val bottomSheet =
                dlg.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout

            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.isDraggable = false
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dlg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = BottomSheetDogSelectorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initDataBinding()
        initObserver()
        initViewPager()
    }

    private fun initDataBinding() {
        viewModel.loadFilters(filters = filters)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
    }

    @SuppressLint("NotifyDataSetChanged", "StringFormatInvalid")
    private fun initObserver() {
        viewModel.dogs.observe(viewLifecycleOwner) { dogs ->
            adapter.submitList(dogs)
        }

        viewModel.dogSelectEvent.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                DogSelectEvent.CancelSelection -> this.dismissNow()
                DogSelectEvent.SelectDog -> adapter.notifyItemChanged(binding.vpGroupSelectDogList.currentItem)
                is DogSelectEvent.PreventSelection -> {
                    toast?.cancel()
                    toast =
                        Toast.makeText(
                            requireContext(),
                            requireContext().getString(
                                R.string.dog_select_prevent_message,
                                event.dogName,
                            ),
                            Toast.LENGTH_SHORT,
                        )
                    toast?.show()
                }

                is DogSelectEvent.SelectDogs -> {
                    this.dismissNow()
                    submit(event.dogs)
                }
            }
        }
    }

    private fun initViewPager() {
        val viewPager = binding.vpGroupSelectDogList
        viewPager.offscreenPageLimit = 3
        viewPager.getChildAt(0).overScrollMode = View.OVER_SCROLL_NEVER
        viewPager.adapter = adapter
        initNearViewSize()
    }

    private fun initNearViewSize() {
        val transform = CompositePageTransformer()
        transform.addTransformer(MarginPageTransformer(8))
        transform.addTransformer { view: View, fl: Float ->
            val v = 1 - abs(fl)
            view.scaleY = 0.8f + v * 0.2f
        }
        binding.vpGroupSelectDogList.setPageTransformer(transform)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        toast?.cancel()
        toast = null
    }
}
