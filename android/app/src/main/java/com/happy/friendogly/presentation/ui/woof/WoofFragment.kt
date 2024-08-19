package com.happy.friendogly.presentation.ui.woof

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.happy.friendogly.R
import com.happy.friendogly.analytics.AnalyticsHelper
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.databinding.FragmentWoofBinding
import com.happy.friendogly.presentation.base.BaseFragment
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.dialog.PetAddAlertDialog
import com.happy.friendogly.presentation.ui.MainActivity.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.happy.friendogly.presentation.ui.MainActivityActionHandler
import com.happy.friendogly.presentation.ui.otherprofile.OtherProfileActivity
import com.happy.friendogly.presentation.ui.permission.LocationPermission
import com.happy.friendogly.presentation.ui.woof.WoofAlertActions.AlertCantClickMarkBtnSnackbar
import com.happy.friendogly.presentation.ui.woof.WoofAlertActions.AlertEndWalkSnackbar
import com.happy.friendogly.presentation.ui.woof.WoofAlertActions.AlertHasNotPetDialog
import com.happy.friendogly.presentation.ui.woof.WoofAlertActions.AlertMarkerRegisteredSnackbar
import com.happy.friendogly.presentation.ui.woof.WoofChangeTrackingModeActions.ChangeToFaceTrackingMode
import com.happy.friendogly.presentation.ui.woof.WoofChangeTrackingModeActions.ChangeToFollowTrackingMode
import com.happy.friendogly.presentation.ui.woof.WoofChangeTrackingModeActions.ChangeToNoFollowTrackingMode
import com.happy.friendogly.presentation.ui.woof.WoofMakeMarkerActions.MakeMyFootprintMarker
import com.happy.friendogly.presentation.ui.woof.WoofMakeMarkerActions.MakeNearFootprintMarkers
import com.happy.friendogly.presentation.ui.woof.adapter.PetDetailInfoAdapter
import com.happy.friendogly.presentation.ui.woof.model.FilterState
import com.happy.friendogly.presentation.ui.woof.model.Footprint
import com.happy.friendogly.presentation.ui.woof.model.WalkStatus
import com.happy.friendogly.presentation.utils.logBackBtnClicked
import com.happy.friendogly.presentation.utils.logCloseBtnClicked
import com.happy.friendogly.presentation.utils.logFootprintClicked
import com.happy.friendogly.presentation.utils.logFootprintMemberNameClicked
import com.happy.friendogly.presentation.utils.logFootprintPetImageClicked
import com.happy.friendogly.presentation.utils.logLocationBtnClicked
import com.happy.friendogly.presentation.utils.logMarkBtnClicked
import com.happy.friendogly.presentation.utils.logMyFootprintBtnClicked
import com.happy.friendogly.presentation.utils.logRefreshBtnClicked
import com.happy.friendogly.presentation.utils.logRegisterHelpClicked
import com.happy.friendogly.presentation.utils.logRegisterMarkerBtnClicked
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
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
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.Duration
import java.util.Locale
import java.util.Timer
import kotlin.concurrent.timer
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

class WoofFragment :
    BaseFragment<FragmentWoofBinding>(R.layout.fragment_woof),
    WoofActionHandler,
    OnMapReadyCallback {
    private var timer: Timer? = null
    private val locationPermission: LocationPermission = initLocationPermission()

    private lateinit var map: NaverMap
    private lateinit var latLng: LatLng
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    private val analyticsHelper: AnalyticsHelper by lazy { AppModule.getInstance().analyticsHelper }
    private val mapView: MapView by lazy { binding.mapView }
    private val circleOverlay: CircleOverlay by lazy { CircleOverlay() }
    private val locationSource: FusedLocationSource by lazy {
        FusedLocationSource(
            this,
            LOCATION_PERMISSION_REQUEST_CODE,
        )
    }
    private val adapter by lazy { PetDetailInfoAdapter(this) }

    private val viewModel by viewModels<WoofViewModel> {
        WoofViewModel.factory(
            analyticsHelper = analyticsHelper,
            postFootprintUseCase = AppModule.getInstance().postFootprintUseCase,
            patchFootprintRecentWalkStatusAutoUseCase = AppModule.getInstance().patchFootprintRecentWalkStatusAutoUseCase,
            patchFootprintRecentWalkStatusManualUseCase = AppModule.getInstance().patchFootprintRecentWalkStatusManualUseCase,
            getNearFootprintsUseCase = AppModule.getInstance().getNearFootprintsUseCase,
            getFootprintMarkBtnInfoUseCase = AppModule.getInstance().getFootprintMarkBtnInfoUseCase,
            getFootprintInfoUseCase = AppModule.getInstance().getFootprintInfoUseCase,
            deleteFootprintUseCase = AppModule.getInstance().deleteFootprintUseCase,
        )
    }

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
        if (locationPermission.hasPermissions() && !locationSource.isActivated) {
            activateMap()
        }
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewModel.uiState.value is WoofUiState.FindingFriends) {
                        requireActivity().onBackPressed()
                    } else {
                        viewModel.updateUiState(WoofUiState.FindingFriends())
                    }
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onDetach() {
        super.onDetach()
        onBackPressedCallback.remove()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (!locationPermission.hasPermissions()) {
            viewModel.updateUiState(WoofUiState.FindingFriends())
        }

        runIfLocationPermissionGranted {
            activateMap()
        }
    }

    override fun clickMarkBtn() {
        analyticsHelper.logMarkBtnClicked()
        runIfLocationPermissionGranted {
            viewModel.loadFootprintMarkBtnInfo()
        }
    }

    override fun clickRegisterMarkerBtn() {
        analyticsHelper.logRegisterMarkerBtnClicked()
        runIfLocationPermissionGranted {
            viewModel.registerFootprint(map.cameraPosition.target)
        }
    }

    override fun clickLocationBtn() {
        analyticsHelper.logLocationBtnClicked()
        runIfLocationPermissionGranted {
            viewModel.changeLocationTrackingMode()
        }
    }

    override fun clickMyFootprintBtn() {
        analyticsHelper.logMyFootprintBtnClicked()
        runIfLocationPermissionGranted {
            val myFootprintMarker = viewModel.myFootprintMarker.value
            if (myFootprintMarker != null) {
                val position = myFootprintMarker.marker.position
                moveCameraCenterPosition(position)
                viewModel.changeTrackingModeToNoFollow()
            } else {
                showSnackbar(resources.getString(R.string.woof_not_exist_my_footprint))
            }
        }
    }

    override fun clickStatusAll() {
        viewModel.updateFilterState(FilterState.ALL)
    }

    override fun clickStatusBefore() {
        viewModel.updateFilterState(FilterState.BEFORE)
    }

    override fun clickStatusOnGoing() {
        viewModel.updateFilterState(FilterState.ONGOING)
    }

    override fun clickStatusAfter() {
        viewModel.updateFilterState(FilterState.AFTER)
    }

    override fun clickRefreshBtn() {
        analyticsHelper.logRefreshBtnClicked()
        viewModel.changeRefreshBtnVisibility(false)
        runIfLocationPermissionGranted {
            viewModel.scanNearFootprints(latLng)
        }
    }

    override fun clickDeleteMyFootprintMarkerBtn() {
        viewModel.deleteMyFootprintMarker()
        binding.chronometerWoofWalkTime.stop()
    }

    override fun clickEndWalkBtn() {
        viewModel.endWalk()
        binding.chronometerWoofWalkTime.stop()
    }

    override fun clickBackBtn() {
        analyticsHelper.logBackBtnClicked()
        viewModel.updateUiState(WoofUiState.FindingFriends())
    }

    override fun clickCloseBtn() {
        analyticsHelper.logCloseBtnClicked()
        viewModel.updateUiState(WoofUiState.FindingFriends())
    }

    override fun clickFootprintMarker(
        footprintId: Long,
        marker: Marker,
    ) {
        marker.setOnClickListener {
            analyticsHelper.logFootprintClicked()
            changePreviousClickedMarkerSize()
            viewModel.loadFootprintInfo(footprintId, marker)
            val position = adjustPosition(marker)
            moveCameraCenterPosition(position)
            changeClickedMarkerSize(marker)
            viewModel.updateUiState(WoofUiState.ViewingFootprintInfo)
            true
        }
    }

    override fun clickFootprintPetImage(petImageUrl: String) {
        analyticsHelper.logFootprintPetImageClicked()
        startActivity(PetImageActivity.getIntent(requireContext(), petImageUrl))
    }

    override fun clickFootprintMemberName(memberId: Long) {
        analyticsHelper.logFootprintMemberNameClicked()
        startActivity(OtherProfileActivity.getIntent(requireContext(), memberId))
    }

    override fun clickRegisterHelp() {
        analyticsHelper.logRegisterHelpClicked()
        showRegisterHelpBalloon()
    }

    private fun initMap(naverMap: NaverMap) {
        map = naverMap
        map.extent =
            LatLngBounds(
                LatLng(MIN_KOREA_LATITUDE, MIN_KOREA_LONGITUDE),
                LatLng(MAX_KOREA_LATITUDE, MAX_KOREA_LONGITUDE),
            )
        map.minZoom = MIN_ZOOM
        map.maxZoom = MAX_ZOOM
        map.locationSource = locationSource

        map.uiSettings.apply {
            isScaleBarEnabled = true
            isCompassEnabled = true
            isLocationButtonEnabled = false
            isZoomControlEnabled = false
        }
        binding.lbvWoofLocation.map = map
        binding.lbvWoofLocationRegister.map = map

        map.onMapClickListener =
            NaverMap.OnMapClickListener { _, _ ->
                if (viewModel.uiState.value is WoofUiState.ViewingFootprintInfo) {
                    viewModel.updateUiState(WoofUiState.FindingFriends())
                }
            }

        map.addOnLocationChangeListener { location ->
            latLng = LatLng(location.latitude, location.longitude)
        }

        map.addOnCameraChangeListener { reason, _ ->
            if (reason == REASON_GESTURE) {
                viewModel.changeTrackingModeToNoFollow()

                if (viewModel.uiState.value is WoofUiState.FindingFriends) {
                    viewModel.changeRefreshBtnVisibility(true)
                }

                if (viewModel.uiState.value is WoofUiState.ViewingFootprintInfo) {
                    viewModel.updateUiState(WoofUiState.FindingFriends())
                }
            }

            if (viewModel.uiState.value is WoofUiState.RegisteringFootprint) {
                circleOverlay.center = map.cameraPosition.target
            }
        }

        map.addOnCameraIdleListener {
            if (viewModel.uiState.value is WoofUiState.RegisteringFootprint) {
                getAddress(map.cameraPosition.target)
            }
        }
    }

    private fun initDataBinding() {
        binding.vm = viewModel
        binding.actionHandler = this
    }

    private fun initObserve() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is WoofUiState.FindingFriends -> hideRegisterFootprintScreen()
                is WoofUiState.RegisteringFootprint -> showRegisterFootprintScreen()
                else -> return@observe
            }
        }

        viewModel.filterState.observe(viewLifecycleOwner) { filterState ->
            markFootprintMarkers(filterState)
        }

        viewModel.myWalkStatus.observe(viewLifecycleOwner) { myWalkStatus ->
            if (myWalkStatus == null) {
                return@observe
            }

            if (myWalkStatus.walkStatus == WalkStatus.BEFORE || myWalkStatus.walkStatus == WalkStatus.ONGOING) {
                startWalkStatusTimer(myWalkStatus.changedWalkStatusTime)
            }

            if (myWalkStatus.walkStatus == WalkStatus.AFTER) {
                timer?.cancel()
            } else {
                startTimerAndUpdateWalkStatus()
            }
        }

        viewModel.myFootprintMarker.observe(viewLifecycleOwner) { myFootprintMarker ->
            if (myFootprintMarker != null) {
                myFootprintMarker.marker.map = map
                setUpCircleOverlay(myFootprintMarker.marker.position)
                viewModel.updateFootprintRecentWalkStatus(latLng)
            }
        }

        viewModel.nearFootprintMarkers.observe(viewLifecycleOwner) {
            clearNearFootprintMarkers()
            val filterState = viewModel.filterState.value ?: return@observe
            markFootprintMarkers(filterState)
        }

        viewModel.footprintInfo.observe(viewLifecycleOwner) { footprintInfo ->
            adapter.submitList(footprintInfo.petsDetailInfo)
        }

        viewModel.makeMarkerActions.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is MakeMyFootprintMarker -> {
                    val previousMyFootprintMarker = viewModel.myFootprintMarker.value
                    if (previousMyFootprintMarker != null) {
                        previousMyFootprintMarker.marker.map = null
                    }

                    if (event.myFootprint != null) {
                        val myMarker = createMarker(footprint = event.myFootprint)
                        viewModel.loadMyFootprintMarker(myMarker)
                    } else {
                        circleOverlay.map = null
                        timer?.cancel()
                    }
                }

                is MakeNearFootprintMarkers -> {
                    val nearFootprintMarkers =
                        event.nearFootprints.map { footprint ->
                            val marker = createMarker(footprint = footprint)
                            marker
                        }
                    clearNearFootprintMarkers()
                    viewModel.loadNearFootprintMarkers(nearFootprintMarkers)
                }
            }
        }

        viewModel.changeTrackingModeActions.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is ChangeToNoFollowTrackingMode ->
                    map.locationTrackingMode = LocationTrackingMode.NoFollow

                is ChangeToFollowTrackingMode ->
                    map.locationTrackingMode = LocationTrackingMode.Follow

                is ChangeToFaceTrackingMode ->
                    map.locationTrackingMode = LocationTrackingMode.Face
            }
        }

        viewModel.alertActions.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is AlertHasNotPetDialog -> showRegisterPetDialog()

                is AlertCantClickMarkBtnSnackbar ->
                    showSnackbar(
                        String.format(
                            resources.getString(
                                R.string.woof_cant_mark,
                                event.remainingTime,
                            ),
                        ),
                    )

                is AlertMarkerRegisteredSnackbar -> showSnackbar(resources.getString(R.string.woof_marker_registered))
                is WoofAlertActions.AlertDeleteMyFootprintMarkerSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.woof_delete_my_footprint_marker,
                        ),
                    )

                is AlertEndWalkSnackbar -> showSnackbar(resources.getString(R.string.woof_stop_walk))
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
        viewModel.updateUiState(WoofUiState.Loading)
        locationSource.activate { location ->
            val lastLocation = location ?: return@activate
            latLng = LatLng(lastLocation.latitude, lastLocation.longitude)
            viewModel.initFootprintMarkers(latLng)
            if (viewModel.myFootprintMarker.value != null) {
                viewModel.updateFootprintRecentWalkStatus(latLng)
            }
            moveCameraCenterPosition(latLng)
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    map.locationTrackingMode = LocationTrackingMode.Follow
                },
                TRACKING_DELAY_MILLIS,
            )
        }
    }

    private fun setUpCircleOverlay(latLng: LatLng) {
        circleOverlay.center = latLng
        circleOverlay.radius = WALKING_RADIUS
        circleOverlay.color = resources.getColor(R.color.map_circle, null)
        circleOverlay.map = map
    }

    private fun moveCameraCenterPosition(position: LatLng) {
        val cameraUpdate = CameraUpdate.scrollTo(position).animate(CameraAnimation.Easing)
        map.moveCamera(cameraUpdate)
    }

    private fun createMarker(footprint: Footprint): Marker {
        val marker = Marker()
        marker.apply {
            position = LatLng(footprint.latitude, footprint.longitude)
            icon = OverlayImage.fromResource(markerIcon(footprint))
            width = MARKER_DEFAULT_WIDTH
            height = MARKER_DEFAULT_HEIGHT
            zIndex = footprint.createdAt.toZIndex()
            map = map
            clickFootprintMarker(footprintId = footprint.footprintId, marker = marker)
        }

        return marker
    }

    private fun markerIcon(footprint: Footprint): Int {
        return if (footprint.isMine) {
            R.drawable.ic_marker_mine_clicked
        } else {
            when (footprint.walkStatus) {
                WalkStatus.BEFORE -> R.drawable.ic_marker_before_clicked
                WalkStatus.ONGOING -> R.drawable.ic_marker_ongoing_clicked
                WalkStatus.AFTER -> R.drawable.ic_marker_after_clicked
            }
        }
    }

    private fun LocalDateTime.toZIndex(): Int {
        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val duration =
            Duration.between(this.toJavaLocalDateTime(), currentDateTime.toJavaLocalDateTime())
        val millis = duration.toMillis()
        return (-millis / 1000).toInt()
    }

    private fun showRegisterFootprintScreen() {
        val myFootprintMarker = viewModel.myFootprintMarker.value
        if (myFootprintMarker != null) {
            myFootprintMarker.marker.map = null
        }
        setUpCircleOverlay(map.cameraPosition.target)
        getAddress(map.cameraPosition.target)
        changePreviousClickedMarkerSize()
    }

    private fun hideRegisterFootprintScreen() {
        val myFootprintMarker = viewModel.myFootprintMarker.value
        if (myFootprintMarker != null) {
            myFootprintMarker.marker.map = map
            circleOverlay.center = myFootprintMarker.marker.position
        } else {
            circleOverlay.map = null
        }
        changePreviousClickedMarkerSize()
    }

    private fun showRegisterHelpBalloon() {
        val balloon =
            Balloon.Builder(requireContext())
                .setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP)
                .setText(resources.getString(R.string.woof_register_description))
                .setTextColorResource(R.color.white)
                .setTextSize(15f)
                .setMarginBottom(10)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowSize(10)
                .setArrowPosition(0.5f)
                .setPadding(12)
                .setCornerRadius(8f)
                .setBackgroundColorResource(R.color.coral400)
                .setBalloonAnimation(BalloonAnimation.ELASTIC)
                .setLifecycleOwner(viewLifecycleOwner)
                .build()

        balloon.showAlignTop(binding.btnWoofRegisterHelp)
    }

    private fun changePreviousClickedMarkerSize() {
        val footprintInfo = viewModel.footprintInfo.value ?: return
        footprintInfo.marker.width = MARKER_DEFAULT_WIDTH
        footprintInfo.marker.height = MARKER_DEFAULT_HEIGHT
    }

    private fun changeClickedMarkerSize(marker: Marker) {
        marker.width = MARKER_CLICKED_WIDTH
        marker.height = MARKER_CLICKED_HEIGHT
    }

    private fun clearNearFootprintMarkers() {
        val footprintMarkers = viewModel.nearFootprintMarkers.value ?: return
        footprintMarkers.forEach { footprintMarker ->
            footprintMarker.marker.map = null
        }
    }

    private fun adjustPosition(marker: Marker): LatLng {
        val bearingRadians = Math.toRadians(map.cameraPosition.bearing)
        val offsetDistance =
            (map.contentBounds.northLatitude - map.contentBounds.southLatitude) / 8.0

        val latitude = marker.position.latitude - offsetDistance * cos(bearingRadians)
        val longitude = marker.position.longitude - offsetDistance * sin(bearingRadians)

        return LatLng(latitude, longitude)
    }

    private fun getAddress(position: LatLng) {
        val geocoder = Geocoder(requireContext(), Locale.KOREA)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(
                convertLtnLng(position.latitude),
                convertLtnLng(position.longitude),
                1,
            ) { addresses ->
                showAddress(addresses)
            }
        } else {
            val addresses =
                geocoder.getFromLocation(position.latitude, position.longitude, 1) ?: return
            showAddress(addresses)
        }
    }

    private fun showAddress(addresses: List<Address>) {
        if (addresses.isEmpty()) return
        val address = addresses[0]
        val addressLine =
            address.getAddressLine(0)
                .replace(resources.getString(R.string.woof_address_korea), "")
                .replace(resources.getString(R.string.woof_address_korea_english), "")
                .trimStart()
        requireActivity().runOnUiThread {
            viewModel.loadAddressLine(addressLine)
        }
    }

    private fun convertLtnLng(latLngArg: Double): Double {
        return floor(latLngArg * 100) / 100
    }

    private fun startTimerAndUpdateWalkStatus() {
        if (timer != null) {
            timer?.cancel()
        }

        timer =
            timer(period = UPDATE_WALK_STATUS_PERIOD_MILLS) {
                Handler(Looper.getMainLooper()).post {
                    val distanceResults = FloatArray(1)
                    val myFootprintMarker = viewModel.myFootprintMarker.value ?: return@post
                    val position = myFootprintMarker.marker.position
                    Location.distanceBetween(
                        latLng.latitude,
                        latLng.longitude,
                        position.latitude,
                        position.longitude,
                        distanceResults,
                    )
                    val distance = distanceResults[0]
                    if (startWalk(distance) || endWalk(distance)) {
                        viewModel.updateFootprintRecentWalkStatus(latLng)
                    }
                }
            }
    }

    private fun startWalk(distance: Float): Boolean {
        val myWalkStatus = viewModel.myWalkStatus.value ?: return false
        return myWalkStatus.walkStatus == WalkStatus.BEFORE && distance <= WALKING_RADIUS
    }

    private fun endWalk(distance: Float): Boolean {
        val myWalkStatus = viewModel.myWalkStatus.value ?: return false
        return myWalkStatus.walkStatus == WalkStatus.ONGOING && distance > WALKING_RADIUS
    }

    private fun startWalkStatusTimer(changedWalkStatusTime: LocalDateTime) {
        val now = java.time.LocalDateTime.now()
        val duration = Duration.between(changedWalkStatusTime.toJavaLocalDateTime(), now)
        binding.chronometerWoofWalkTime.base =
            SystemClock.elapsedRealtime() - duration.toMillis()
        binding.chronometerWoofWalkTime.start()
    }

    private fun markFootprintMarkers(filterState: FilterState) {
        when (filterState) {
            FilterState.ALL -> filterAllFootprintMarkers()
            FilterState.BEFORE -> filterFootprintMarkersByWalkStatus(WalkStatus.BEFORE)
            FilterState.ONGOING -> filterFootprintMarkersByWalkStatus(WalkStatus.ONGOING)
            FilterState.AFTER -> filterFootprintMarkersByWalkStatus(WalkStatus.AFTER)
        }
    }

    private fun filterAllFootprintMarkers() {
        val allFootprintMarkers = viewModel.nearFootprintMarkers.value ?: return
        allFootprintMarkers.forEach { footprintMarker ->
            footprintMarker.marker.map = map
        }
    }

    private fun filterFootprintMarkersByWalkStatus(walkStatus: WalkStatus) {
        val filteredFootprintMarkers = viewModel.nearFootprintMarkers.value ?: return
        filteredFootprintMarkers.forEach { footprintMarker ->
            if (footprintMarker.walkStatus == walkStatus) {
                footprintMarker.marker.map = map
            } else {
                footprintMarker.marker.map = null
            }
        }
    }

    private fun runIfLocationPermissionGranted(action: () -> Unit) {
        if (locationPermission.hasPermissions()) {
            action()
        } else {
            locationPermission.createAlarmDialog().show(parentFragmentManager, tag)
        }
    }

    private fun showRegisterPetDialog() {
        PetAddAlertDialog(
            clickToNegative = {},
            clickToPositive = {
                (activity as MainActivityActionHandler).navigateToRegisterPet(null)
            },
        ).show(parentFragmentManager, tag)
    }

    companion object {
        private const val WALKING_RADIUS = 1000.0
        private const val MIN_ZOOM = 10.0
        private const val MAX_ZOOM = 20.0
        private const val MARKER_DEFAULT_WIDTH = 72
        private const val MARKER_DEFAULT_HEIGHT = 111
        private const val MARKER_CLICKED_WIDTH = 96
        private const val MARKER_CLICKED_HEIGHT = 148
        private const val TRACKING_DELAY_MILLIS = 300L
        private const val UPDATE_WALK_STATUS_PERIOD_MILLS = 30000L
        private const val MIN_KOREA_LATITUDE = 33.0
        private const val MAX_KOREA_LATITUDE = 39.0
        private const val MIN_KOREA_LONGITUDE = 125.0
        private const val MAX_KOREA_LONGITUDE = 132.0
    }
}
