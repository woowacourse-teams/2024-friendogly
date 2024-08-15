package com.happy.friendogly.presentation.ui.club.select

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
import com.happy.friendogly.R
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.databinding.BottomSheetDogSelectorBinding
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ClubFilter
import com.happy.friendogly.presentation.ui.club.select.adapter.PetSelectAdapter
import kotlin.math.abs

class PetSelectBottomSheet(
    private val filters: List<ClubFilter>,
    private val submit: (List<Long>) -> Unit,
) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetDogSelectorBinding? = null
    val binding: BottomSheetDogSelectorBinding
        get() = _binding!!

    private lateinit var dlg: BottomSheetDialog
    private var toast: Toast? = null

    private val viewModel: PetSelectViewModel by viewModels<PetSelectViewModel> {
        PetSelectViewModel.factory(
            getPetsMineUseCase = AppModule.getInstance().getPetsMineUseCase,
        )
    }

    private val adapter: PetSelectAdapter by lazy {
        PetSelectAdapter(viewModel as PetSelectActionHandler)
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
        viewModel.pets.observe(viewLifecycleOwner) { dogs ->
            adapter.submitList(dogs)
        }

        viewModel.petSelectEvent.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                PetSelectEvent.CancelSelection -> this.dismissNow()
                PetSelectEvent.SelectPet -> adapter.notifyItemChanged(binding.vpClubSelectDogList.currentItem)
                is PetSelectEvent.PreventSelection ->
                    makeToast(
                        requireContext().getString(
                            R.string.dog_select_prevent_message,
                            event.dogName,
                        ),
                    )

                is PetSelectEvent.SelectPets -> {
                    this.dismissNow()
                    submit(event.pets)
                }

                PetSelectEvent.FailLoadPet ->
                    makeToast(
                        requireContext().getString(
                            R.string.dog_select_load_fail,
                        ),
                    )
            }
        }
    }

    private fun makeToast(message: String) {
        toast?.cancel()
        toast =
            Toast.makeText(
                requireContext(),
                message,
                Toast.LENGTH_SHORT,
            )
        toast?.show()
    }

    private fun initViewPager() {
        val viewPager = binding.vpClubSelectDogList
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
        binding.vpClubSelectDogList.setPageTransformer(transform)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        toast?.cancel()
        toast = null
    }
}
