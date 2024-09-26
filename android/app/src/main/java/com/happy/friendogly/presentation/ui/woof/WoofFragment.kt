package com.happy.friendogly.presentation.ui.woof

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.happy.friendogly.R
import com.happy.friendogly.databinding.FragmentWoofBinding
import com.happy.friendogly.firebase.analytics.AnalyticsHelper
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.dialog.PetAddAlertDialog
import com.happy.friendogly.presentation.ui.MainActivity.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.happy.friendogly.presentation.ui.MainActivityActionHandler
import com.happy.friendogly.presentation.ui.otherprofile.OtherProfileActivity
import com.happy.friendogly.presentation.ui.permission.LocationPermission
import com.happy.friendogly.presentation.ui.playground.PlaygroundBottomSheet
import com.happy.friendogly.presentation.ui.woof.action.WoofAlertActions
import com.happy.friendogly.presentation.ui.woof.action.WoofAlertActions.AlertEndWalkSnackbar
import com.happy.friendogly.presentation.ui.woof.action.WoofAlertActions.AlertHasNotPetDialog
import com.happy.friendogly.presentation.ui.woof.action.WoofAlertActions.AlertMarkBtnClickBeforeTimeoutSnackbar
import com.happy.friendogly.presentation.ui.woof.action.WoofAlertActions.AlertMarkerRegisteredSnackbar
import com.happy.friendogly.presentation.ui.woof.action.WoofMapActions
import com.happy.friendogly.presentation.ui.woof.action.WoofMapActions.MakeMyFootprintMarker
import com.happy.friendogly.presentation.ui.woof.action.WoofMapActions.MakeNearFootprintMarkers
import com.happy.friendogly.presentation.ui.woof.action.WoofNavigateActions
import com.happy.friendogly.presentation.ui.woof.action.WoofTrackingModeActions.FaceTrackingMode
import com.happy.friendogly.presentation.ui.woof.action.WoofTrackingModeActions.FollowTrackingMode
import com.happy.friendogly.presentation.ui.woof.action.WoofTrackingModeActions.NoFollowTrackingMode
import com.happy.friendogly.presentation.ui.woof.model.Footprint
import com.happy.friendogly.presentation.ui.woof.model.WalkStatus
import com.happy.friendogly.presentation.ui.woof.service.WoofWalkReceiver
import com.happy.friendogly.presentation.ui.woof.service.WoofWalkService
import com.happy.friendogly.presentation.ui.woof.state.WoofUiState
import com.happy.friendogly.presentation.ui.woof.viewmodel.WoofViewModel
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
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.FusedLocationSource
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.Duration
import java.util.Locale
import javax.inject.Inject
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

@AndroidEntryPoint
class WoofFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentWoofBinding? = null
    private val binding get() = _binding!!
    private var snackbar: Snackbar? = null
    private var balloon: Balloon? = null

    private lateinit var map: NaverMap
    private lateinit var latLng: LatLng
    private lateinit var locationPermission: LocationPermission
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private lateinit var walkReceiver: WoofWalkReceiver

    private val circleOverlay: CircleOverlay by lazy { CircleOverlay() }
    private val pathOverlay: PathOverlay by lazy { PathOverlay() }
    private val locationSource: FusedLocationSource by lazy {
        FusedLocationSource(
            this,
            LOCATION_PERMISSION_REQUEST_CODE,
        )
    }

    private val mapView: MapView by lazy { binding.mapView }
    private val walkTimeChronometer: Chronometer by lazy { binding.chronometerWoofWalkTime }

    private val viewModel by viewModels<WoofViewModel>()

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewModel.uiState.value is WoofUiState.FindingFriends) {
                        requireActivity().finish()
                    } else {
                        viewModel.updateUiState(WoofUiState.FindingFriends)
                    }
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWoofBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        setupLocationPermission()
        setupBroadCastReceiver()
        setupDataBinding()
        setupObserving()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        if (locationPermission.hasPermissions() && viewModel.uiState.value is WoofUiState.LocationPermissionsNotGranted) {
            activateMap()
        }

        if (!locationPermission.hasPermissions() && viewModel.uiState.value !is WoofUiState.LocationPermissionsNotGranted) {
            viewModel.updateUiState(WoofUiState.LocationPermissionsNotGranted)
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
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopWalkService()
        requireContext().unregisterReceiver(walkReceiver)
    }

    override fun onDetach() {
        super.onDetach()
        onBackPressedCallback.remove()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(naverMap: NaverMap) {
        initMap(naverMap)
        if (locationPermission.hasPermissions()) {
            activateMap()
        }
    }

    private fun initMap(naverMap: NaverMap) {
        map = naverMap
        map.locationSource = locationSource
        map.extent =
            LatLngBounds(
                LatLng(MIN_KOREA_LATITUDE, MIN_KOREA_LONGITUDE),
                LatLng(MAX_KOREA_LATITUDE, MAX_KOREA_LONGITUDE),
            )
        map.minZoom = MIN_ZOOM
        map.moveCamera(CameraUpdate.zoomTo(DEFAULT_ZOOM))

        map.uiSettings.apply {
            isScaleBarEnabled = true
            isCompassEnabled = false
            isLocationButtonEnabled = false
            isZoomControlEnabled = false
        }
        binding.lbvWoofLocation.map = map

        map.onMapClickListener =
            NaverMap.OnMapClickListener { _, _ ->
                if (viewModel.uiState.value is WoofUiState.ViewingFootprintInfo) {
                    viewModel.updateUiState(WoofUiState.FindingFriends)
                }
            }

        map.addOnLocationChangeListener { location ->
            latLng = LatLng(location.latitude, location.longitude)

            if (viewModel.myFootprintMarker.value != null &&
                viewModel.myWalkStatus.value?.walkStatus != WalkStatus.AFTER &&
                viewModel.uiState.value !is WoofUiState.RegisteringFootprint
            ) {
                pathOverlay.coords =
                    listOf(latLng, viewModel.myFootprintMarker.value?.marker?.position)
            }
        }

        map.addOnCameraChangeListener { reason, _ ->
            if (reason == REASON_GESTURE) {
                viewModel.changeTrackingModeToNoFollow()

                if (viewModel.uiState.value is WoofUiState.FindingFriends && viewModel.refreshBtnVisible.value == false) {
                    viewModel.updateRefreshBtnVisibility(visible = true)
                }

                if (viewModel.uiState.value is WoofUiState.ViewingFootprintInfo) {
                    viewModel.updateUiState(WoofUiState.FindingFriends)
                }
            }

            if (viewModel.uiState.value is WoofUiState.RegisteringFootprint) {
                circleOverlay.center = map.cameraPosition.target
                viewModel.updateRegisterFootprintBtnCameraIdle(cameraIdle = false)
            }
        }

        map.addOnCameraIdleListener {
            if (viewModel.uiState.value is WoofUiState.RegisteringFootprint) {
                viewModel.updateRegisterFootprintBtnCameraIdle(cameraIdle = true)
                getAddress(map.cameraPosition.target)
            }
        }
    }

    private fun setupDataBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
    }

    private fun setupObserving() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState as WoofUiState) {
                is WoofUiState.LocationPermissionsNotGranted ->
                    locationPermission.createAlarmDialog()
                        .show(parentFragmentManager, tag)

                is WoofUiState.FindingFriends -> hideRegisterFootprintScreen()
                is WoofUiState.RegisteringFootprint -> {
                    showRegisterFootprintScreen()
                }

                is WoofUiState.ViewingFootprintInfo -> circleOverlay.map = null
                else -> return@observe
            }
        }

        viewModel.myWalkStatus.observe(viewLifecycleOwner) { myWalkStatus ->
            stopWalkService()
            if (myWalkStatus == null) {
                val myFootprintMarker = viewModel.myFootprintMarker.value ?: return@observe
                myFootprintMarker.marker.map = null
                circleOverlay.map = null
                pathOverlay.map = null
                return@observe
            }

            if (myWalkStatus.walkStatus == WalkStatus.AFTER) {
                circleOverlay.map = null
                pathOverlay.map = null
                balloon?.dismiss()
            } else {
                startWalkStatusChronometer(myWalkStatus.changedWalkStatusTime)
                startWalkService()
                val textResId =
                    if (myWalkStatus.walkStatus == WalkStatus.BEFORE) {
                        R.string.woof_walk_before_help
                    } else {
                        R.string.woof_walk_ongoing_help
                    }
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        showHelpBalloon(textRestId = textResId)
                    },
                    DELAY_MILLIS,
                )
            }
        }

        viewModel.myFootprintMarker.observe(viewLifecycleOwner) { myFootprintMarker ->
            if (myFootprintMarker != null) {
                myFootprintMarker.marker.map = map
                setUpCircleOverlay(myFootprintMarker.marker.position)
                setUpPathOverlay()
                viewModel.updateFootprintRecentWalkStatus(latLng)
            }
        }

        viewModel.nearFootprintMarkers.observe(viewLifecycleOwner) {
            clearNearFootprintMarkers()
            markFootprintMarkers()
        }

        viewModel.mapActions.observeEvent(viewLifecycleOwner) { event ->
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
                        pathOverlay.map = null
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

                is WoofMapActions.RegisterMyFootprint -> viewModel.registerMyFootprint(map.cameraPosition.target)

                is WoofMapActions.MoveCameraCenterPosition -> moveCameraCenterPosition(event.position)

                is WoofMapActions.ScanNearFootprints -> viewModel.scanNearFootprints(latLng)

                is WoofMapActions.StopWalkTimeChronometer -> walkTimeChronometer.stop()
            }
        }

        viewModel.changeTrackingModeActions.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is NoFollowTrackingMode -> map.locationTrackingMode = LocationTrackingMode.NoFollow

                is FollowTrackingMode -> map.locationTrackingMode = LocationTrackingMode.Follow

                is FaceTrackingMode -> map.locationTrackingMode = LocationTrackingMode.Face
            }
        }

        viewModel.alertActions.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is WoofAlertActions.AlertHasNotLocationPermissionDialog ->
                    locationPermission.createAlarmDialog()
                        .show(parentFragmentManager, tag)

                is AlertHasNotPetDialog -> showRegisterPetDialog()
                is AlertMarkBtnClickBeforeTimeoutSnackbar ->
                    showSnackbar(
                        String.format(
                            resources.getString(
                                R.string.woof_cant_mark,
                                event.remainingTime,
                            ),
                        ),
                    )

                is WoofAlertActions.AlertAddressOutOfKoreaSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.woof_address_out_of_korea,
                        ),
                    )

                is AlertMarkerRegisteredSnackbar -> showSnackbar(resources.getString(R.string.woof_marker_registered))
                is WoofAlertActions.AlertNotExistMyFootprintSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.woof_not_exist_my_footprint,
                        ),
                    )

                is WoofAlertActions.AlertDeleteMyFootprintMarkerSnackbar -> {
                    showSnackbar(
                        resources.getString(
                            R.string.woof_delete_my_footprint_marker,
                        ),
                    )
                    stopWalkService()
                }

                is AlertEndWalkSnackbar -> showSnackbar(resources.getString(R.string.woof_stop_walk))
                is WoofAlertActions.AlertFailToLoadFootprintMarkBtnInfoSnackbar -> {
                    showSnackbar(
                        resources.getString(R.string.woof_fail_to_load_footprint_mark_btn_info),
                    )
                    stopWalkService()
                }

                is WoofAlertActions.AlertFailToLoadNearFootprintsSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.woof_fail_to_load_near_footprints,
                        ),
                    )

                is WoofAlertActions.AlertFailToRegisterFootprintSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.woof_fail_to_register_footprint,
                        ),
                    )

                is WoofAlertActions.AlertFailToUpdateFootprintWalkStatusSnackbar ->
                    showSnackbar(
                        resources.getString(R.string.woof_fail_to_update_footprint_walk_status),
                    )

                is WoofAlertActions.AlertFailToEndWalkSnackbar -> showSnackbar(resources.getString(R.string.woof_fail_to_end_walk))
                is WoofAlertActions.AlertFailToDeleteMyFootprintSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.woof_fail_to_delete_my_footprint,
                        ),
                    )

                is WoofAlertActions.AlertHelpBalloon -> showHelpBalloon(event.textResId)
            }
        }

        viewModel.navigateActions.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is WoofNavigateActions.NavigateToOtherProfile -> {
                    startActivity(
                        OtherProfileActivity.getIntent(
                            requireContext(),
                            event.memberId,
                        ),
                    )
                }
            }
        }
    }

    private fun setupLocationPermission() {
        locationPermission =
            LocationPermission.from(this, analyticsHelper) { isPermitted ->
                if (isPermitted) {
                    activateMap()
                } else {
                    showSnackbar(getString(R.string.permission_denied_message))
                }
            }
    }

    private fun setupBroadCastReceiver() {
        walkReceiver =
            WoofWalkReceiver { location ->
                latLng = LatLng(location.latitude, location.longitude)
                monitorDistanceAndManageWalkStatus()
            }
        val intentFilter =
            IntentFilter().apply {
                addAction(WoofWalkReceiver.ACTION_LOCATION_UPDATED)
            }

        requireContext().registerReceiver(walkReceiver, intentFilter)
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
                DELAY_MILLIS,
            )
        }
    }

    private fun setUpCircleOverlay(latLng: LatLng) {
        circleOverlay.center = latLng
        circleOverlay.radius = WALKING_RADIUS
        circleOverlay.color = resources.getColor(R.color.map_circle, null)
        circleOverlay.map = map
    }

    private fun setUpPathOverlay() {
        pathOverlay.coords = listOf(latLng, viewModel.myFootprintMarker.value?.marker?.position)
        pathOverlay.width = 40
        pathOverlay.outlineWidth = 0
        pathOverlay.patternImage = OverlayImage.fromResource(R.drawable.ic_path_pattern)
        pathOverlay.patternInterval = 80
        pathOverlay.color = resources.getColor(R.color.blue, null)
        pathOverlay.map = map
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
            clickFootprintMarker(footprintId = footprint.footprintId, marker = this)
        }

        return marker
    }

    private fun clickFootprintMarker(
        footprintId: Long,
        marker: Marker,
    ) {
        marker.setOnClickListener {
            changePreviousClickedMarkerSize()
            viewModel.loadRecentlyClickedMarker(marker)
            val position = adjustPosition(marker)
            moveCameraCenterPosition(position)
            changeClickedMarkerSize(marker)
            viewModel.updateUiState(WoofUiState.ViewingFootprintInfo)
            showPlaygroundBottomSheet(footprintId)
            true
        }
    }

    private fun showPlaygroundBottomSheet(footprintId: Long) {
        val playgroundBottomSheet =
            PlaygroundBottomSheet.newInstance(playgroundId = footprintId)
        playgroundBottomSheet.show(
            requireActivity().supportFragmentManager,
            PlaygroundBottomSheet.TAG,
        )
    }

    private fun markerIcon(footprint: Footprint): Int {
        return if (footprint.isMine) R.drawable.ic_marker_ongoing_clicked else R.drawable.ic_marker_before_clicked
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
            pathOverlay.map = null
        }
        setUpCircleOverlay(map.cameraPosition.target)
        changePreviousClickedMarkerSize()
        getAddress(map.cameraPosition.target)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                showHelpBalloon(textRestId = R.string.woof_register_playground_help)
            },
            DELAY_MILLIS,
        )
    }

    private fun hideRegisterFootprintScreen() {
        val myFootprintMarker = viewModel.myFootprintMarker.value
        if (myFootprintMarker != null) {
            myFootprintMarker.marker.map = map
            if (viewModel.myWalkStatus.value?.walkStatus != WalkStatus.AFTER) {
                circleOverlay.center = myFootprintMarker.marker.position
                pathOverlay.map = map
            } else {
                circleOverlay.map = null
            }
        } else {
            circleOverlay.map = null
        }
        changePreviousClickedMarkerSize()
    }

    private fun changePreviousClickedMarkerSize() {
        val recentlyClickedMarker = viewModel.recentlyClickedMarker.value ?: return
        recentlyClickedMarker.width = MARKER_DEFAULT_WIDTH
        recentlyClickedMarker.height = MARKER_DEFAULT_HEIGHT
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
        val addressLatLng = convertLtnLng(position)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(
                addressLatLng.latitude,
                addressLatLng.longitude,
                1,
            ) { addresses ->
                showAddress(addresses)
            }
        } else {
            val addresses =
                geocoder.getFromLocation(addressLatLng.latitude, addressLatLng.longitude, 1)
                    ?: return
            showAddress(addresses)
        }
    }

    private fun showAddress(addresses: List<Address>) {
        if (addresses.isEmpty()) return
        val address = addresses[0]
        val addressLine =
            address.getAddressLine(0).replace(resources.getString(R.string.woof_address_korea), "")
                .trimStart()
        viewModel.updateAddressLine(addressLine)

        val countryName = address.countryName
        val inKorea = countryName == resources.getString(R.string.woof_address_korea)
        viewModel.updateRegisterFootprintBtnInKorea(inKorea = inKorea)
    }

    private fun convertLtnLng(latLng: LatLng): LatLng {
        return LatLng(floor(latLng.latitude * 100) / 100, floor(latLng.longitude * 100) / 100)
    }

    private fun startWalkService() {
        val myWalkStatus = viewModel.myWalkStatus.value ?: return
        val walkStatus = myWalkStatus.walkStatus
        val now = java.time.LocalDateTime.now()
        val duration =
            Duration.between(myWalkStatus.changedWalkStatusTime.toJavaLocalDateTime(), now)
        val startMillis = System.currentTimeMillis() - duration.toMillis()
        val myFootprintMarker = viewModel.myFootprintMarker.value ?: return
        val position = myFootprintMarker.marker.position

        val intent = WoofWalkService.getIntent(requireContext(), walkStatus, startMillis, position)
        requireContext().startForegroundService(intent)
    }

    private fun monitorDistanceAndManageWalkStatus() {
        val distanceResults = FloatArray(1)
        val myFootprintMarker = viewModel.myFootprintMarker.value ?: return
        val position = myFootprintMarker.marker.position
        Location.distanceBetween(
            latLng.latitude,
            latLng.longitude,
            position.latitude,
            position.longitude,
            distanceResults,
        )
        val distance = distanceResults[0]

        if (startWalkIfWithinRange(distance) || endWalkIfOutOfRange(distance)) {
            viewModel.updateFootprintRecentWalkStatus(latLng)
        }
    }

    private fun startWalkIfWithinRange(distance: Float): Boolean {
        val myWalkStatus = viewModel.myWalkStatus.value ?: return false
        return myWalkStatus.walkStatus == WalkStatus.BEFORE && distance <= WALKING_RADIUS
    }

    private fun endWalkIfOutOfRange(distance: Float): Boolean {
        val myWalkStatus = viewModel.myWalkStatus.value ?: return false
        if (myWalkStatus.walkStatus == WalkStatus.ONGOING && distance > WALKING_RADIUS) {
            stopWalkService()
        }
        return myWalkStatus.walkStatus == WalkStatus.ONGOING && distance > WALKING_RADIUS
    }

    private fun stopWalkService() {
        requireContext().stopService(Intent(requireContext(), WoofWalkService::class.java))
    }

    private fun startWalkStatusChronometer(changedWalkStatusTime: LocalDateTime) {
        val now = java.time.LocalDateTime.now()
        val duration = Duration.between(changedWalkStatusTime.toJavaLocalDateTime(), now)
        val startMillis = System.currentTimeMillis() - duration.toMillis()
        val elapsedRealtimeOffset = System.currentTimeMillis() - SystemClock.elapsedRealtime()
        walkTimeChronometer.base = startMillis - elapsedRealtimeOffset
        walkTimeChronometer.start()
    }

    private fun markFootprintMarkers() {
        val footprintMarkers = viewModel.nearFootprintMarkers.value ?: return
        footprintMarkers.forEach { footprintMarker ->
            footprintMarker.marker.map = map
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

    private fun showSnackbar(
        message: String,
        action: Snackbar.() -> Unit = {},
    ) {
        snackbar?.dismiss()
        snackbar =
            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).apply {
                anchorView = requireActivity().findViewById(R.id.bottom_navi)
                action()
            }
        snackbar?.show()
    }

    private fun showBalloon(text: String) {
        balloon?.dismiss()
        balloon =
            Balloon.Builder(requireContext()).setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP).setText(text).setTextColorResource(R.color.white)
                .setTextSize(14f).setMarginBottom(10)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR).setArrowSize(10)
                .setArrowPosition(0.5f).setPadding(12).setFocusable(false).setCornerRadius(8f)
                .setBackgroundColorResource(R.color.coral400)
                .setBalloonAnimation(BalloonAnimation.ELASTIC).setLifecycleOwner(viewLifecycleOwner)
                .build()
        balloon?.showAlignTop(binding.btnWoofWalkHelp)
    }

    private fun showHelpBalloon(textRestId: Int) {
        val text = resources.getString(textRestId)
        showBalloon(text)
    }

    companion object {
        private const val WALKING_RADIUS = 150.0
        private const val MIN_ZOOM = 7.0
        private const val DEFAULT_ZOOM = 15.0
        private const val MARKER_DEFAULT_WIDTH = 96
        private const val MARKER_DEFAULT_HEIGHT = 144
        private const val MARKER_CLICKED_WIDTH = 144
        private const val MARKER_CLICKED_HEIGHT = 216
        private const val DELAY_MILLIS = 300L
        private const val MIN_KOREA_LATITUDE = 33.0
        private const val MAX_KOREA_LATITUDE = 39.0
        private const val MIN_KOREA_LONGITUDE = 125.0
        private const val MAX_KOREA_LONGITUDE = 132.0

        const val TAG = "WoofFragment"
    }
}
