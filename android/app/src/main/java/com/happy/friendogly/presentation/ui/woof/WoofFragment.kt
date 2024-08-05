package com.happy.friendogly.presentation.ui.woof

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewTreeObserver
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
import com.happy.friendogly.presentation.ui.otherprofile.OtherProfileActivity
import com.happy.friendogly.presentation.ui.permission.LocationPermission
import com.happy.friendogly.presentation.ui.woof.adapter.FootprintInfoPetDetailAdapter
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
import java.util.Locale
import kotlin.math.abs
import kotlin.math.floor

class WoofFragment :
    BaseFragment<FragmentWoofBinding>(R.layout.fragment_woof),
    WoofActionHandler,
    OnMapReadyCallback {
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
    private val adapter by lazy { FootprintInfoPetDetailAdapter(this) }
    private val viewModel by viewModels<WoofViewModel> {
        WoofViewModel.factory(
            postFootprintUseCase = AppModule.getInstance().postFootprintUseCase,
            getNearFootprintsUseCase = AppModule.getInstance().getNearFootprintsUseCase,
            getFootprintMarkBtnInfoUseCase = AppModule.getInstance().getFootprintMarkBtnInfoUseCase,
            getFootprintInfoUseCase = AppModule.getInstance().getFootprintInfoUseCase,
        )
    }

    private var recentlyClickedMarker: Marker? = null
    private var myMarker: Marker? = null
    private val nearMarkers: MutableList<Marker> = mutableListOf()

    override fun onMapReady(naverMap: NaverMap) {
        initMap(naverMap)
        activateMap()
    }

    override fun initViewCreated() {
        initDataBinding()
        initObserve()
        initViewPager()
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

    override fun clickMarkBtn() {
        if (locationPermission.hasPermissions()) {
            viewModel.loadFootprintMarkBtnInfo()
        } else {
            locationPermission.createAlarmDialog().show(parentFragmentManager, tag)
        }
    }

    override fun clickRegisterMarkerBtn() {
        if (locationPermission.hasPermissions()) {
            viewModel.markFootprint(map.cameraPosition.target)
        } else {
            locationPermission.createAlarmDialog().show(parentFragmentManager, tag)
        }
    }

    override fun clickLocationBtn() {
        if (locationPermission.hasPermissions()) {
            viewModel.changeLocationTrackingMode()
        } else {
            locationPermission.createAlarmDialog().show(parentFragmentManager, tag)
        }
    }

    override fun clickMyFootprintBtn() {
        if (locationPermission.hasPermissions()) {
            if (myMarker == null) {
                showSnackbar(resources.getString(R.string.woof_not_exist_my_footprint))
                return
            }
            val position = myMarker?.position ?: return
            moveCameraCenterPosition(position)
        } else {
            locationPermission.createAlarmDialog().show(parentFragmentManager, tag)
        }
    }

    override fun clickBackBtn() {
        hideRegisterMarkerLayout()
        hideMarkerDetail()
    }

    override fun clickCloseBtn() {
        hideRegisterMarkerLayout()
        hideMarkerDetail()
    }

    override fun clickFootprint(
        footprintId: Long,
        marker: Marker,
    ) {
        marker.setOnClickListener {
            viewModel.loadFootprintInfo(footprintId)
            showMarkerDetail()

            val position =
                LatLng(
                    marker.position.latitude - MARKER_CLICKED_CAMERA_LATITUDE_UP,
                    marker.position.longitude,
                )
            moveCameraCenterPosition(position)

            changeRecentlyClickedMarkerSize()
            recentlyClickedMarker = marker

            marker.width = MARKER_CLICKED_WIDTH
            marker.height = MARKER_CLICKED_HEIGHT

            true
        }
    }

    override fun clickFootprintMemberName(memberId: Long) {
        startActivity(OtherProfileActivity.getIntent(requireContext(), memberId))
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
        binding.lbvWoofLocationRegister.map = map

        map.addOnLocationChangeListener { location ->
            latLng = LatLng(location.latitude, location.longitude)
        }

        map.addOnCameraChangeListener { reason, _ ->
            if (reason == REASON_GESTURE) {
                viewModel.changeTrackingModeToNoFollow()

                if (binding.tvWoofWalkStatus.isVisible) {
                    hideMarkerDetail()
                    changeRecentlyClickedMarkerSize()
                }
            }
        }

        map.addOnCameraIdleListener {
            if (binding.layoutWoofLocationRegister.isVisible) {
                getAddress(map.cameraPosition.target)
            }
        }
    }

    private fun initDataBinding() {
        binding.vm = viewModel
        binding.actionHandler = this
    }

    private fun initObserve() {
        viewModel.nearFootprints.observe(viewLifecycleOwner) { nearFootprints ->
            markNearFootprints(nearFootprints)
        }

        viewModel.footprintSave.observe(viewLifecycleOwner) { footprintSave ->
            myMarker?.map = null
            createMarker(
                footprintId = footprintSave.footprintId,
                latLng = LatLng(footprintSave.latitude, footprintSave.longitude),
                // API 생기면 수정해야함.
                walkStatus = WalkStatus.BEFORE,
                createdAt = footprintSave.createdAt,
                isMine = true,
            )
        }

        viewModel.footprintPetDetails.observe(viewLifecycleOwner) { footPrintPetDetails ->
            adapter.submitList(footPrintPetDetails)
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

                is WoofMapActions.RemoveNearFootprints -> {
                    clearNearMarkers()
                }

                is WoofMapActions.ShowRegisterMarkerLayout -> {
                    showRegisterMarkerLayout()
                }

                is WoofMapActions.HideRegisterMarkerLayout -> {
                    hideRegisterMarkerLayout()
                }
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

                is WoofSnackbarActions.ShowMarkerRegistered ->
                    showSnackbar(resources.getString(R.string.woof_marker_registered))

                is WoofSnackbarActions.ShowInvalidLocationSnackbar ->
                    showSnackbar(resources.getString(R.string.woof_location_load_fail))
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
        transform.addTransformer(MarginPageTransformer(12))
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
                LOADING_DELAY_MILLIS,
            )
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
        val marker = Marker()
        marker.position = latLng
        marker.icon = OverlayImage.fromResource(markerIcon(isMine, walkStatus))
        marker.width = MARKER_WIDTH
        marker.height = MARKER_HEIGHT
        marker.zIndex = createdAt.toZIndex()
        marker.map = map
        clickFootprint(footprintId, marker)

        if (isMine) {
            myMarker = marker
            setUpCircleOverlay(latLng)
        } else {
            nearMarkers.add(marker)
        }
    }

    private fun markerIcon(
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

    private fun showViewAnimation(view: View) {
        if (view.height == 0) {
            view.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        animateView(view)
                        if (view.height > 0) view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                },
            )
        } else {
            animateView(view)
        }
    }

    private fun animateView(view: View) {
        view.apply {
            translationY = height.toFloat()
            animate()
                .translationY(0f)
                .setDuration(ANIMATE_DURATION_MILLIS)
                .setListener(
                    object : AnimatorListenerAdapter() {
                        override fun onAnimationStart(animation: Animator) {
                            isVisible = true
                            bringToFront()
                        }
                    },
                )
        }
    }

    private fun hideViewAnimation(view: View) {
        view.animate()
            .translationY(view.height.toFloat())
            .setDuration(ANIMATE_DURATION_MILLIS)
            .setListener(
                object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        view.isVisible = false
                    }
                },
            )
    }

    private fun showMarkerDetail() {
        showViewAnimation(binding.vpWoofPetDetail)
        showViewAnimation(binding.tvWoofWalkStatus)
    }

    private fun hideMarkerDetail() {
        binding.tvWoofWalkStatus.isVisible = false
        hideViewAnimation(binding.vpWoofPetDetail)
    }

    private fun showRegisterMarkerLayout() {
        myMarker?.map = null
        circleOverlay.map = null
        getAddress(map.cameraPosition.target)

        showViewAnimation(binding.layoutWoofRegisterMarker)
        binding.layoutWoofLocationRegister.isVisible = true
        binding.btnWoofLocationRegister.isVisible = true
        binding.btnWoofBack.isVisible = true
        binding.btnWoofClose.isVisible = true
        binding.ivWoofRegisterMarker.isVisible = true
        changeRecentlyClickedMarkerSize()
    }

    private fun hideRegisterMarkerLayout() {
        if (myMarker != null && myMarker?.map == null) {
            myMarker?.map = map
            circleOverlay.map = map
        }

        hideViewAnimation(binding.layoutWoofRegisterMarker)
        binding.layoutWoofLocationRegister.isVisible = false
        binding.btnWoofLocationRegister.isVisible = false
        binding.btnWoofBack.isVisible = false
        binding.btnWoofClose.isVisible = false
        binding.ivWoofRegisterMarker.isVisible = false
        changeRecentlyClickedMarkerSize()
    }

    private fun changeRecentlyClickedMarkerSize() {
        if (recentlyClickedMarker != null) {
            recentlyClickedMarker?.width = MARKER_WIDTH
            recentlyClickedMarker?.height = MARKER_HEIGHT
        }
    }

    private fun markNearFootprints(footprints: List<Footprint>) {
        footprints.forEach { footprint ->
            createMarker(
                footprintId = footprint.footprintId,
                latLng = LatLng(footprint.latitude, footprint.longitude),
                walkStatus = footprint.walkStatus,
                createdAt = footprint.createdAt,
                isMine = footprint.isMine,
            )
        }
    }

    private fun clearNearMarkers() {
        nearMarkers.forEach { marker ->
            marker.map = null
        }
        nearMarkers.clear()
    }

    private fun getAddress(position: LatLng) {
        val geocoder = Geocoder(requireContext(), Locale.KOREA)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(
                convertLtnLng(position.latitude),
                convertLtnLng(position.longitude),
                1,
            ) { addresses ->
                activity?.runOnUiThread {
                    viewModel.loadAddress(addresses[0])
                }
            }
        } else {
            viewModel.saveLowLevelSdkAddress(
                geocoder = geocoder,
                latitude = convertLtnLng(position.latitude),
                longitude = convertLtnLng(position.longitude),
            )
        }
    }

    private fun convertLtnLng(latLngArg: Double): Double {
        return floor(latLngArg * 100) / 100
    }

    companion object {
        private const val MARKER_CIRCLE_RADIUS = 1000
        private const val MIN_ZOOM = 10.0
        private const val MAX_ZOOM = 20.0
        private const val MARKER_WIDTH = 72
        private const val MARKER_HEIGHT = 111
        private const val MARKER_CLICKED_WIDTH = 96
        private const val MARKER_CLICKED_HEIGHT = 148
        private const val LOADING_DELAY_MILLIS = 2000L
        private const val ANIMATE_DURATION_MILLIS = 300L
        private const val MARKER_CLICKED_CAMERA_LATITUDE_UP = 0.0025
    }
}
