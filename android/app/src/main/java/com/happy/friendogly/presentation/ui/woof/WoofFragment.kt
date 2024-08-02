package com.happy.friendogly.presentation.ui.woof

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.happy.friendogly.R
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.databinding.FragmentWoofBinding
import com.happy.friendogly.presentation.base.BaseFragment
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.MainActivity.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.happy.friendogly.presentation.ui.permission.LocationPermission
import com.happy.friendogly.presentation.ui.woof.adapter.FootprintInfoAdapter
import com.happy.friendogly.presentation.ui.woof.model.Footprint
import com.happy.friendogly.presentation.ui.woof.model.WalkStatus
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.CameraUpdate.REASON_GESTURE
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.Duration
import kotlin.math.abs

class WoofFragment : BaseFragment<FragmentWoofBinding>(R.layout.fragment_woof), OnMapReadyCallback {
    private lateinit var map: NaverMap
    private lateinit var latLng: LatLng
    private val mapView: MapView by lazy { binding.mapView }
    private val circleOverlay: CircleOverlay by lazy { CircleOverlay() }
    private val locationPermission: LocationPermission = initLocationPermission()

    private val locationSource: FusedLocationSource by lazy {
        FusedLocationSource(
            this,
            LOCATION_PERMISSION_REQUEST_CODE,
        )
    }
    private val adapter by lazy { FootprintInfoAdapter() }
    private val viewModel by viewModels<WoofViewModel> {
        WoofViewModel.factory(
            postFootprintUseCase = AppModule.getInstance().postFootprintUseCase,
            getNearFootprintsUseCase = AppModule.getInstance().getNearFootprintsUseCase,
            getFootprintMarkBtnInfoUseCase = AppModule.getInstance().getFootprintMarkBtnInfoUseCase,
            getFootprintInfoUseCase = AppModule.getInstance().getFootprintInfoUseCase,
        )
    }
    private var isMarkerHideAnimationEnd: Boolean = false
    private var recentlyClickedMarker: Marker? = null

    override fun onMapReady(naverMap: NaverMap) {
        initMap(naverMap)
        activateMap()
    }

    override fun initViewCreated() {
        initDataBinding()
        initObserve()
        initViewPager()
        clickMarkBtn()
        clickLocationBtn()
        clickMyMFootprint()
        mapView.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (locationPermission.hasPermissions()) {
            showLoadingAnimation()
            activateMap()
        } else {
            hideLoadingAnimation()
            locationPermission.createAlarmDialog().show(parentFragmentManager, tag)
        }
    }

    private fun initMap(naverMap: NaverMap) {
        map = naverMap
        map.minZoom = MIN_ZOOM
        map.maxZoom = MAX_ZOOM
        map.locationSource = locationSource
        map.uiSettings.apply {
            isLocationButtonEnabled = true
            isCompassEnabled = true
            isZoomControlEnabled = false
            isScaleBarEnabled = false
        }

        map.addOnLocationChangeListener { location ->
            latLng = LatLng(location.latitude, location.longitude)
        }

        map.addOnCameraChangeListener { reason, _ ->
            if (reason == REASON_GESTURE) {
                viewModel.changeTrackingModeToNoFollow()
                if (!isMarkerHideAnimationEnd) {
                    hideMarkerDetail()
                }
                recentlyClickedMarker?.let { marker ->
                    marker.width = 72
                    marker.height = 111
                }
            }
        }
    }

    private fun initDataBinding() {
        binding.vm = viewModel
    }

    private fun initObserve() {
        viewModel.nearFootprints.observe(viewLifecycleOwner) { nearFootprints ->
            markNearFootPrints(nearFootprints)
        }

        viewModel.footprintSave.observe(viewLifecycleOwner) { footprintSave ->
            createMarker(
                footprintId = footprintSave.footprintId,
                latLng = LatLng(footprintSave.latitude, footprintSave.longitude),
                // 수정해야함.
                walkStatus = WalkStatus.ONGOING,
                createdAt = footprintSave.createdAt,
                isMine = true,
            )
            map.locationTrackingMode = LocationTrackingMode.Follow
        }

        viewModel.footprintInfo.observe(viewLifecycleOwner) { footPrintInfo ->
            adapter.setMemberName(footPrintInfo.memberName)
            adapter.submitList(footPrintInfo.pets)
        }

        viewModel.mapActions.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is WoofMapActions.ChangeMapToNoFollowTrackingMode ->
                    map.locationTrackingMode =
                        LocationTrackingMode.NoFollow

                is WoofMapActions.ChangeMapToFollowTrackingMode ->
                    map.locationTrackingMode =
                        LocationTrackingMode.Follow

                is WoofMapActions.ChangeMapToFaceTrackingMode ->
                    map.locationTrackingMode =
                        LocationTrackingMode.Face
            }
        }

        viewModel.snackbarActions.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is WoofSnackbarActions.ShowHasNotPetSnackbar ->
                    showSnackbar(
                        String.format(
                            resources.getString(
                                R.string.woof_has_not_pet,
                            ),
                        ),
                    )

                is WoofSnackbarActions.ShowCantClickMarkBtnSnackbar -> {
                    showSnackbar(
                        String.format(
                            resources.getString(
                                R.string.woof_cant_mark,
                                event.remainingTime,
                            ),
                        ),
                    )
                }
            }
        }
    }

    private fun initViewPager() {
        val viewPager = binding.vpWoofPetDetail
        viewPager.offscreenPageLimit = 3
        viewPager.getChildAt(0).overScrollMode = View.OVER_SCROLL_NEVER
        viewPager.adapter = adapter
        initNearViewSize()
    }

    private fun initNearViewSize() {
        val transform = CompositePageTransformer()
        transform.addTransformer(MarginPageTransformer(14))
        transform.addTransformer { view: View, fl: Float ->
            val v = 1 - abs(fl)
            view.scaleY = 0.8f + v * 0.2f
        }
        binding.vpWoofPetDetail.setPageTransformer(transform)
    }

    private fun initLocationPermission(): LocationPermission {
        return LocationPermission.from(this) { isPermitted ->
            if (isPermitted) {
                activateMap()
            } else {
                showSnackbar(getString(R.string.permission_denied_message))
            }
        }
    }

    private fun activateMap() {
        showLoadingAnimation()
        locationSource.activate { location ->
            val lastLocation = location ?: return@activate
            latLng = LatLng(lastLocation.latitude, lastLocation.longitude)
            viewModel.loadNearFootprints(latLng)
            moveCameraCenterPosition(latLng)
            map.locationTrackingMode = LocationTrackingMode.Follow
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    hideLoadingAnimation()
                },
                2000,
            )
        }
    }

    private fun clickMarkBtn() {
        binding.btnWoofMark.setOnClickListener {
            if (locationPermission.hasPermissions()) {
                viewModel.loadFootprintMarkBtnInfo(latLng)
            } else {
                locationPermission.createAlarmDialog().show(parentFragmentManager, tag)
            }
        }
    }

    private fun clickLocationBtn() {
        binding.btnWoofLocation.setOnClickListener {
            if (locationPermission.hasPermissions()) {
                viewModel.changeLocationTrackingMode()
            } else {
                locationPermission.createAlarmDialog().show(parentFragmentManager, tag)
            }
        }
    }

    private fun clickMyMFootprint() {
        binding.btnWoofMyFootprint.setOnClickListener {
            if (locationPermission.hasPermissions()) {
                val nearFootprints = viewModel.nearFootprints.value ?: return@setOnClickListener
                val myMarker = nearFootprints.firstOrNull { footprint -> footprint.isMine }
                if (myMarker == null) {
                    showSnackbar(resources.getString(R.string.woof_not_exist_my_footprint))
                    return@setOnClickListener
                }
                val position = LatLng(myMarker.latitude, myMarker.longitude)
                moveCameraCenterPosition(position)
            } else {
                locationPermission.createAlarmDialog().show(parentFragmentManager, tag)
            }
        }
    }

    private fun showLoadingAnimation() {
        binding.layoutWoofLoading.isVisible = true
        binding.lottieWoofLoading.playAnimation()
    }

    private fun hideLoadingAnimation() {
        binding.layoutWoofLoading.isVisible = false
        binding.lottieWoofLoading.pauseAnimation()
    }

    private fun setUpCircleOverlay(latLng: LatLng) {
        circleOverlay.center = latLng
        circleOverlay.radius = MARKER_CIRCLE_RADIUS / map.projection.metersPerPixel
        circleOverlay.color = resources.getColor(R.color.map_circle, null)
        circleOverlay.map = map
    }

    private fun moveCameraCenterPosition(position: LatLng) {
        val cameraUpdate =
            CameraUpdate.scrollTo(position)
                .animate(CameraAnimation.Easing)
        map.moveCamera(cameraUpdate)
    }

    private fun createMarker(
        footprintId: Long,
        latLng: LatLng,
        walkStatus: WalkStatus,
        createdAt: LocalDateTime,
        isMine: Boolean,
    ) {
        val iconImage =
            decideMarkerIcon(isMine, walkStatus)

        val marker = Marker()
        marker.position = latLng
        marker.icon = OverlayImage.fromResource(iconImage)
        marker.width = 72
        marker.height = 111
        marker.zIndex = createdAt.toZIndex()
        marker.map = map

        setUpMarkerAction(footprintId, marker)
        if (isMine) setUpCircleOverlay(latLng)
    }

    private fun decideMarkerIcon(
        isMine: Boolean,
        walkStatus: WalkStatus,
    ): Int {
        val iconImage =
            if (isMine) {
                R.drawable.ic_marker_mine_clicked
            } else {
                when (walkStatus) {
                    WalkStatus.BEFORE -> R.drawable.ic_marker_before_clicked
                    WalkStatus.ONGOING -> R.drawable.ic_marker_ongoing_clicked
                    WalkStatus.AFTER -> R.drawable.ic_marker_after_clicked
                }
            }
        return iconImage
    }

    private fun LocalDateTime.toZIndex(): Int {
        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val duration =
            Duration.between(this.toJavaLocalDateTime(), currentDateTime.toJavaLocalDateTime())
        val millis = duration.toMillis()
        return (-millis / 1000).toInt()
    }

    private fun setUpMarkerAction(
        footprintId: Long,
        marker: Marker,
    ) {
        marker.setOnClickListener {
            isMarkerHideAnimationEnd = false
            viewModel.loadFootprintInfo(footprintId)
            showMarkerDetail()
            val position = LatLng(marker.position.latitude - 0.0025, marker.position.longitude)
            moveCameraCenterPosition(position)
            if (recentlyClickedMarker != null) {
                recentlyClickedMarker?.width = 72
                recentlyClickedMarker?.height = 111
            }
            marker.width = 96
            marker.height = 148
            recentlyClickedMarker = marker
            true
        }
    }

    private fun showMarkerDetail() {
        binding.vpWoofPetDetail.post {
            binding.vpWoofPetDetail.apply {
                isVisible = true
                translationY = height.toFloat()
                animate()
                    .translationY(0f)
                    .setDuration(300)
                    .setListener(null)
            }
        }

        binding.tvWoofWalkStatus.post {
            binding.tvWoofWalkStatus.apply {
                isVisible = true
                translationY = height.toFloat()
                animate()
                    .translationY(0f)
                    .setDuration(300)
                    .setListener(null)
            }
        }
    }

    private fun hideMarkerDetail() {
        binding.tvWoofWalkStatus.isVisible = false

        binding.vpWoofPetDetail.post {
            binding.vpWoofPetDetail.animate()
                .translationY(binding.vpWoofPetDetail.height.toFloat())
                .setDuration(300)
                .setListener(
                    object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            binding.vpWoofPetDetail.isVisible = false
                        }
                    },
                )
        }

        isMarkerHideAnimationEnd = true
    }

    private fun markNearFootPrints(footPrints: List<Footprint>) {
        footPrints.forEach { footPrint ->
            createMarker(
                footprintId = footPrint.footprintId,
                latLng = LatLng(footPrint.latitude, footPrint.longitude),
                walkStatus = footPrint.walkStatus,
                createdAt = footPrint.createdAt,
                isMine = footPrint.isMine,
            )
        }
    }

    companion object {
        private const val MARKER_CIRCLE_RADIUS = 500
        private const val MIN_ZOOM = 10.0
        private const val MAX_ZOOM = 20.0
    }
}
