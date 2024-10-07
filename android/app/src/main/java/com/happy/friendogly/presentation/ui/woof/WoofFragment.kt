package com.happy.friendogly.presentation.ui.woof

import android.content.Context
import android.content.Context.RECEIVER_NOT_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Rect
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
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
import com.happy.friendogly.presentation.ui.petimage.PetImageActivity
import com.happy.friendogly.presentation.ui.woof.action.WoofAlertActions
import com.happy.friendogly.presentation.ui.woof.action.WoofAlertActions.AlertEndWalkSnackbar
import com.happy.friendogly.presentation.ui.woof.action.WoofAlertActions.AlertHasNotPetDialog
import com.happy.friendogly.presentation.ui.woof.action.WoofAlertActions.AlertMarkerRegisteredSnackbar
import com.happy.friendogly.presentation.ui.woof.action.WoofMapActions
import com.happy.friendogly.presentation.ui.woof.action.WoofMapActions.MakeMyPlaygroundMarker
import com.happy.friendogly.presentation.ui.woof.action.WoofMapActions.MakeNearPlaygroundMarkers
import com.happy.friendogly.presentation.ui.woof.action.WoofNavigateActions
import com.happy.friendogly.presentation.ui.woof.action.WoofTrackingModeActions.FaceTrackingMode
import com.happy.friendogly.presentation.ui.woof.action.WoofTrackingModeActions.FollowTrackingMode
import com.happy.friendogly.presentation.ui.woof.action.WoofTrackingModeActions.NoFollowTrackingMode
import com.happy.friendogly.presentation.ui.woof.adapter.PetDetailAdapter
import com.happy.friendogly.presentation.ui.woof.adapter.PetSummaryAdapter
import com.happy.friendogly.presentation.ui.woof.model.PlayStatus
import com.happy.friendogly.presentation.ui.woof.model.Playground
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
    private val bottomSheetBehavior: BottomSheetBehavior<LinearLayout> by lazy {
        BottomSheetBehavior.from(
            binding.bottomSheetPlayground,
        )
    }

    private val mapView: MapView by lazy { binding.mapView }

    //    private val walkTimeChronometer: Chronometer by lazy { binding.chronometerWoofWalkTime }
    private val petDetailAdapter by lazy { PetDetailAdapter(viewModel) }
    private val petSummaryAdapter by lazy { PetSummaryAdapter() }

    private val viewModel by viewModels<WoofViewModel>()

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewModel.uiState.value is WoofUiState.FindingPlayground) {
                        requireActivity().finish()
                    } else {
                        viewModel.updateUiState(WoofUiState.FindingPlayground)
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
        setupRecyclerView()
        setupBottomSheetBehavior()
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
        binding.lbvWoofRegisterLocation.map = map

        map.onMapClickListener =
            NaverMap.OnMapClickListener { _, _ ->
                if (viewModel.uiState.value is WoofUiState.ViewingPlaygroundInfo || viewModel.uiState.value is WoofUiState.ViewingPlaygroundSummary) {
                    viewModel.updateUiState(WoofUiState.FindingPlayground)
                }
            }

        map.addOnLocationChangeListener { location ->
            latLng = LatLng(location.latitude, location.longitude)

            if (viewModel.myPlayground.value != null && viewModel.uiState.value !is WoofUiState.RegisteringPlayground) {
                pathOverlay.coords = listOf(latLng, viewModel.myPlayground.value?.marker?.position)
            }
        }

        map.addOnCameraChangeListener { reason, _ ->
            if (reason == REASON_GESTURE) {
                viewModel.changeTrackingModeToNoFollow()

                if (viewModel.uiState.value is WoofUiState.FindingPlayground &&
                    viewModel.refreshBtnVisible.value == false
                ) {
                    viewModel.updateRefreshBtnVisibility(visible = true)
                }

                if (viewModel.uiState.value is WoofUiState.ViewingPlaygroundInfo ||
                    viewModel.uiState.value is WoofUiState.ViewingPlaygroundSummary
                ) {
                    viewModel.updateUiState(WoofUiState.FindingPlayground)
                }
            }

            if (viewModel.uiState.value is WoofUiState.RegisteringPlayground) {
                circleOverlay.center = map.cameraPosition.target
                viewModel.updateRegisterPlaygroundBtnCameraIdle(cameraIdle = false)
            }
        }

        map.addOnCameraIdleListener {
            if (viewModel.uiState.value is WoofUiState.RegisteringPlayground) {
                viewModel.updateRegisterPlaygroundBtnCameraIdle(cameraIdle = true)
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

                is WoofUiState.FindingPlayground -> hideRegisterPlaygroundScreen()
                is WoofUiState.RegisteringPlayground -> {
                    showRegisterPlaygroundScreen()
                }

                is WoofUiState.ViewingPlaygroundInfo -> {
                    circleOverlay.map = null
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                }

                else -> return@observe
            }
            if (uiState !is WoofUiState.ViewingPlaygroundInfo) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        viewModel.myPlayStatus.observe(viewLifecycleOwner) { myPlayStatus ->
            if (myPlayStatus == PlayStatus.NO_PLAYGROUND) {
                val myPlaygroundMarker = viewModel.myPlayground.value ?: return@observe
                myPlaygroundMarker.marker.map = null
                circleOverlay.map = null
                pathOverlay.map = null
                balloon?.dismiss()
            }
//            else {
//                startWalkStatusChronometer(myPlayStatus.changedWalkStatusTime)
//            }
        }

        viewModel.playgroundInfo.observe(viewLifecycleOwner) { playgroundInfo ->
            petDetailAdapter.submitList(playgroundInfo.petDetails)
        }

        viewModel.playgroundSummary.observe(viewLifecycleOwner) { playgroundSummary ->
            petSummaryAdapter.submitList(playgroundSummary.petImageUrls)
        }

        viewModel.myPlayground.observe(viewLifecycleOwner) { myPlaygroundMarker ->
            if (myPlaygroundMarker != null) {
                startWalkService()
                myPlaygroundMarker.marker.map = map
                setUpCircleOverlay(myPlaygroundMarker.marker.position)
                setUpPathOverlay()
                bottomSheetBehavior.isHideable = false
                viewModel.loadPlaygroundInfo(myPlaygroundMarker.id)
                viewModel.updatePlaygroundArrival(latLng)
            } else {
                stopWalkService()
                bottomSheetBehavior.isHideable = true
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        viewModel.nearPlaygrounds.observe(viewLifecycleOwner) {
            clearNearPlaygroundMarkers()
            markNearPlaygroundMarkers()
        }

        viewModel.mapActions.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is MakeMyPlaygroundMarker -> {
                    val previousMyFootprintMarker = viewModel.myPlayground.value
                    if (previousMyFootprintMarker != null) {
                        previousMyFootprintMarker.marker.map = null
                    }

                    if (event.myPlayground != null) {
                        val marker = createMarker(playground = event.myPlayground)
                        viewModel.loadMyPlayground(marker)
                    } else {
                        circleOverlay.map = null
                        pathOverlay.map = null
                    }
                }

                is MakeNearPlaygroundMarkers -> {
                    val nearFootprintMarkers =
                        event.nearPlaygrounds.map { playground ->
                            val marker = createMarker(playground = playground)
                            marker
                        }
                    clearNearPlaygroundMarkers()
                    viewModel.loadNearPlaygrounds(nearFootprintMarkers)
                }

                is WoofMapActions.RegisterMyPlayground -> viewModel.registerMyPlayground(map.cameraPosition.target)

                is WoofMapActions.MoveCameraCenterPosition -> moveCameraCenterPosition(event.position)

                is WoofMapActions.ScanNearPlaygrounds -> viewModel.scanNearPlaygrounds()

//                is WoofMapActions.StopWalkTimeChronometer -> walkTimeChronometer.stop()
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
                is WoofAlertActions.AlertAddressOutOfKoreaSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.woof_address_out_of_korea,
                        ),
                    )

                is AlertMarkerRegisteredSnackbar -> showSnackbar(resources.getString(R.string.woof_marker_registered))
                is WoofAlertActions.AlertNotExistMyPlaygroundSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.woof_not_exist_my_playground,
                        ),
                    )

                is WoofAlertActions.AlertExitMyPlaygroundSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.woof_exit_my_playground,
                        ),
                    )

                is AlertEndWalkSnackbar -> showSnackbar(resources.getString(R.string.woof_stop_walk))
                is WoofAlertActions.AlertFailToCheckPetExistence ->
                    showSnackbar(
                        resources.getString(
                            R.string.woof_fail_to_load_pet_existence_btn,
                        ),
                    )

                is WoofAlertActions.AlertFailToLoadPlaygroundsSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.woof_fail_to_load_near_footprints,
                        ),
                    )

                is WoofAlertActions.AlertFailToRegisterPlaygroundSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.woof_fail_to_register_playground,
                        ),
                    )

                is WoofAlertActions.AlertFailToUpdatePlaygroundArrival ->
                    showSnackbar(
                        resources.getString(R.string.woof_fail_to_update_playground_arrival),
                    )

                is WoofAlertActions.AlertFailToEndWalkSnackbar -> showSnackbar(resources.getString(R.string.woof_fail_to_end_walk))
                is WoofAlertActions.AlertFailToDeleteMyFootprintSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.woof_fail_to_delete_my_footprint,
                        ),
                    )

                is WoofAlertActions.AlertFailToLoadPlaygroundInfoSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.fail_to_load_playground_info,
                        ),
                    )

                is WoofAlertActions.AlertFailToLoadPlaygroundSummarySnackbar ->
                    showSnackbar(
                        resources.getString(R.string.fail_to_load_playground_summary),
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

            when (event) {
                is WoofNavigateActions.NavigateToPetImage -> {
                    startActivity(
                        PetImageActivity.getIntent(
                            requireContext(),
                            event.petImageUrl,
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
                addAction(WoofWalkReceiver.ACTION_LOCATION_UPDATE)
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireContext().registerReceiver(
                walkReceiver,
                intentFilter,
                RECEIVER_NOT_EXPORTED,
            )
        } else {
            requireContext().registerReceiver(walkReceiver, intentFilter)
        }
    }

    private fun activateMap() {
        viewModel.updateUiState(WoofUiState.Loading)
        locationSource.activate { location ->
            val lastLocation = location ?: return@activate
            latLng = LatLng(lastLocation.latitude, lastLocation.longitude)
            viewModel.initPlaygrounds()
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
        pathOverlay.coords = listOf(latLng, viewModel.myPlayground.value?.marker?.position)
        pathOverlay.width = 30
        pathOverlay.outlineWidth = 0
        pathOverlay.patternImage = OverlayImage.fromResource(R.drawable.ic_path_pattern)
        pathOverlay.patternInterval = 60
        pathOverlay.color = resources.getColor(R.color.blue, null)
        pathOverlay.map = map
    }

    private fun moveCameraCenterPosition(position: LatLng) {
        val cameraUpdate = CameraUpdate.scrollTo(position).animate(CameraAnimation.Easing)
        map.moveCamera(cameraUpdate)
    }

    private fun createMarker(playground: Playground): Marker {
        val marker = Marker()
        marker.apply {
            position = LatLng(playground.latitude, playground.longitude)
            icon = OverlayImage.fromResource(markerIcon(playground))
            width = MARKER_DEFAULT_WIDTH
            height = MARKER_DEFAULT_HEIGHT
            map = map
            clickPlaygroundMarker(id = playground.id, marker = this)
        }

        return marker
    }

    private fun clickPlaygroundMarker(
        id: Long,
        marker: Marker,
    ) {
        marker.setOnClickListener {
            changePreviousClickedMarkerSize()
            viewModel.loadRecentlyClickedPlayground(marker)
            val position = adjustPosition(marker)
            moveCameraCenterPosition(position)
            changeClickedMarkerSize(marker)
            if (id == viewModel.myPlayground.value?.id) {
                viewModel.loadPlaygroundInfo(id = id)
                viewModel.updateUiState(WoofUiState.ViewingPlaygroundInfo)
            } else {
                viewModel.loadPlaygroundSummary(id = id)
                viewModel.updateUiState(WoofUiState.ViewingPlaygroundSummary)
            }

            true
        }
    }

    private fun markerIcon(playground: Playground): Int {
        return if (playground.isParticipating) R.drawable.ic_marker_ongoing_clicked else R.drawable.ic_marker_before_clicked
    }

    private fun showRegisterPlaygroundScreen() {
        val myPlayground = viewModel.myPlayground.value
        if (myPlayground != null) {
            myPlayground.marker.map = null
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

    private fun hideRegisterPlaygroundScreen() {
        val myPlayground = viewModel.myPlayground.value
        if (myPlayground != null) {
            myPlayground.marker.map = map
            circleOverlay.center = myPlayground.marker.position
            pathOverlay.map = map
        } else {
            circleOverlay.map = null
        }
        changePreviousClickedMarkerSize()
    }

    private fun changePreviousClickedMarkerSize() {
        val recentlyClickedMarker = viewModel.recentlyClickedPlayground.value ?: return
        recentlyClickedMarker.width = MARKER_DEFAULT_WIDTH
        recentlyClickedMarker.height = MARKER_DEFAULT_HEIGHT
    }

    private fun changeClickedMarkerSize(marker: Marker) {
        marker.width = MARKER_CLICKED_WIDTH
        marker.height = MARKER_CLICKED_HEIGHT
    }

    private fun clearNearPlaygroundMarkers() {
        val nearPlaygroundMarkers = viewModel.nearPlaygrounds.value ?: return
        nearPlaygroundMarkers.forEach { playgroundMarker ->
            playgroundMarker.map = null
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
        viewModel.updateRegisterPlaygroundBtnInKorea(inKorea = inKorea)
    }

    private fun convertLtnLng(latLng: LatLng): LatLng {
        return LatLng(floor(latLng.latitude * 100) / 100, floor(latLng.longitude * 100) / 100)
    }

    private fun startWalkService() {
//        val now = java.time.LocalDateTime.now()
//        val duration =
//            Duration.between(myPlayStatus.changedWalkStatusTime.toJavaLocalDateTime(), now)
//        val startMillis = System.currentTimeMillis() - duration.toMillis()
//        val myFootprintMarker = viewModel.myPlayground.value ?: return
//        val position = myFootprintMarker.marker.position

        val myPlayStatus = viewModel.myPlayStatus.value ?: return
        val intent = WoofWalkService.getIntent(requireContext(), myPlayStatus)
        requireContext().startForegroundService(intent)
    }

    private fun monitorDistanceAndManageWalkStatus() {
        val distanceResults = FloatArray(1)
        val myFootprintMarker = viewModel.myPlayground.value ?: return
        val position = myFootprintMarker.marker.position
        Location.distanceBetween(
            latLng.latitude,
            latLng.longitude,
            position.latitude,
            position.longitude,
            distanceResults,
        )
        val distance = distanceResults[0]

        if (startPlayingIfWithinRange(distance) || endWalkIfOutOfRange(distance)) {
            viewModel.updatePlaygroundArrival(latLng)
        }
    }

    private fun startPlayingIfWithinRange(distance: Float): Boolean {
        val myPlayStatus = viewModel.myPlayStatus.value ?: return false
        return myPlayStatus == PlayStatus.AWAY && distance <= WALKING_RADIUS
    }

    private fun endWalkIfOutOfRange(distance: Float): Boolean {
        val myPlayStatus = viewModel.myPlayStatus.value ?: return false
        if (myPlayStatus == PlayStatus.PLAYING && distance > WALKING_RADIUS) {
            stopWalkService()
            return true
        }
        return false
    }

    private fun stopWalkService() {
        requireContext().stopService(Intent(requireContext(), WoofWalkService::class.java))
    }

//    private fun startWalkStatusChronometer(changedWalkStatusTime: LocalDateTime) {
//        val now = java.time.LocalDateTime.now()
//        val duration = Duration.between(changedWalkStatusTime.toJavaLocalDateTime(), now)
//        val startMillis = System.currentTimeMillis() - duration.toMillis()
//        val elapsedRealtimeOffset = System.currentTimeMillis() - SystemClock.elapsedRealtime()
//        walkTimeChronometer.base = startMillis - elapsedRealtimeOffset
//        walkTimeChronometer.start()
//    }

    private fun markNearPlaygroundMarkers() {
        val nearPlaygroundMarkers = viewModel.nearPlaygrounds.value ?: return
        nearPlaygroundMarkers.forEach { playgroundMarker ->
            playgroundMarker.map = map
        }
    }

    private fun setupRecyclerView() {
        binding.rcvPlaygroundPet.adapter = petDetailAdapter
        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.rcvPlaygroundPet.addItemDecoration(divider)

        binding.rcvPlaygroundPetSummary.adapter = petSummaryAdapter
        binding.rcvPlaygroundPetSummary.addItemDecoration(
            object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State,
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    val position = parent.getChildAdapterPosition(view)
                    if (position != 0) {
                        val overlapOffset =
                            (10f * requireContext().resources.displayMetrics.density).toInt()
                        outRect.left = overlapOffset * -1
                    }
                }
            },
        )
    }

    private fun setupBottomSheetBehavior() {
        bottomSheetBehavior.isDraggable = true
        bottomSheetBehavior.isHideable = false
        bottomSheetBehavior.isFitToContents = false
        bottomSheetBehavior.expandedOffset = 50
        bottomSheetBehavior.peekHeight = 250

        bottomSheetBehavior.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(
                    bottomSheet: View,
                    slideOffset: Float,
                ) {
                }

                override fun onStateChanged(
                    bottomSheet: View,
                    newState: Int,
                ) {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        if (viewModel.myPlayground.value != null) {
                            changePreviousClickedMarkerSize()
                        }
                        binding.rcvPlaygroundPet.smoothScrollToPosition(0)
                    } else {
                        viewModel.updateRefreshBtnVisibility(false)
                    }
                }
            },
        )
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