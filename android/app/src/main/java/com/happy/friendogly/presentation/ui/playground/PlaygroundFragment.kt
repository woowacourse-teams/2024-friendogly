package com.happy.friendogly.presentation.ui.playground

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
import android.view.Gravity
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
import com.happy.friendogly.databinding.FragmentPlaygroundBinding
import com.happy.friendogly.firebase.analytics.AnalyticsHelper
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.dialog.PetAddAlertDialog
import com.happy.friendogly.presentation.ui.MainActivity.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.happy.friendogly.presentation.ui.MainActivityActionHandler
import com.happy.friendogly.presentation.ui.message.MessageActivity
import com.happy.friendogly.presentation.ui.otherprofile.OtherProfileActivity
import com.happy.friendogly.presentation.ui.permission.LocationPermission
import com.happy.friendogly.presentation.ui.petimage.PetImageActivity
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertAction
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertAction.AlertEndWalkSnackbar
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertAction.AlertHasNotPetDialog
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertAction.AlertMarkerRegisteredSnackbar
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundMapAction
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundMapAction.MakeMyPlaygroundMarker
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundMapAction.MakeNearPlaygroundMarkers
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundNavigateAction
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundTrackingModeAction.FaceTrackingMode
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundTrackingModeAction.FollowTrackingMode
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundTrackingModeAction.NoFollowTrackingMode
import com.happy.friendogly.presentation.ui.playground.adapter.PetDetailAdapter
import com.happy.friendogly.presentation.ui.playground.adapter.PetSummaryAdapter
import com.happy.friendogly.presentation.ui.playground.model.PlayStatus
import com.happy.friendogly.presentation.ui.playground.model.Playground
import com.happy.friendogly.presentation.ui.playground.service.PlaygroundLocationReceiver
import com.happy.friendogly.presentation.ui.playground.service.PlaygroundLocationService
import com.happy.friendogly.presentation.ui.playground.service.PlaygroundLocationService.Companion.ACTION_START
import com.happy.friendogly.presentation.ui.playground.state.PlaygroundUiState
import com.happy.friendogly.presentation.ui.playground.uimodel.PlaygroundMarkerUiModel
import com.happy.friendogly.presentation.ui.playground.viewmodel.PlaygroundViewModel
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
class PlaygroundFragment :
    Fragment(),
    OnMapReadyCallback {
    private var _binding: FragmentPlaygroundBinding? = null
    private val binding get() = _binding!!
    private var snackbar: Snackbar? = null
    private var balloon: Balloon? = null

    private lateinit var map: NaverMap
    private lateinit var latLng: LatLng
    private lateinit var locationPermission: LocationPermission
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private lateinit var walkReceiver: PlaygroundLocationReceiver

    private val registeringCircleOverlay: CircleOverlay by lazy { CircleOverlay() }
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

    private val petDetailAdapter by lazy { PetDetailAdapter(viewModel) }
    private val petSummaryAdapter by lazy { PetSummaryAdapter() }

    private val viewModel by viewModels<PlaygroundViewModel>()

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewModel.uiState.value is PlaygroundUiState.FindingPlayground) {
                        requireActivity().finish()
                    } else {
                        viewModel.updateUiState(PlaygroundUiState.FindingPlayground)
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
        _binding = FragmentPlaygroundBinding.inflate(inflater, container, false)
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
        if (locationPermission.hasPermissions() && viewModel.uiState.value is PlaygroundUiState.LocationPermissionsNotGranted) {
            activateMap()
        }

        if (!locationPermission.hasPermissions() && viewModel.uiState.value !is PlaygroundUiState.LocationPermissionsNotGranted) {
            viewModel.updateUiState(PlaygroundUiState.LocationPermissionsNotGranted)
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
        viewModel.leavePlayground()
        stopLocationService()
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
            logoGravity = Gravity.TOP or Gravity.START
            setLogoMargin(MAP_LOGO_MARGIN, MAP_LOGO_MARGIN, 0, 0)
        }
        binding.lbvWoofLocation.map = map
        binding.lbvWoofRegisterLocation.map = map

        map.onMapClickListener =
            NaverMap.OnMapClickListener { _, _ ->
                if (viewModel.uiState.value is PlaygroundUiState.ViewingPlaygroundInfo ||
                    viewModel.uiState.value is PlaygroundUiState.ViewingPlaygroundSummary
                ) {
                    viewModel.updateUiState(PlaygroundUiState.FindingPlayground)
                }
            }

        map.addOnLocationChangeListener { location ->
            latLng = LatLng(location.latitude, location.longitude)

            if (viewModel.myPlayground.value != null && viewModel.uiState.value !is PlaygroundUiState.RegisteringPlayground) {
                pathOverlay.coords =
                    listOf(
                        latLng,
                        viewModel.myPlayground.value
                            ?.marker
                            ?.position,
                    )
            }
        }

        map.addOnCameraChangeListener { reason, _ ->
            if (reason == REASON_GESTURE) {
                viewModel.changeTrackingModeToNoFollow()

                if (viewModel.uiState.value is PlaygroundUiState.FindingPlayground &&
                    viewModel.refreshBtnVisible.value == false
                ) {
                    viewModel.updateRefreshBtnVisibility(visible = true)
                }

                if (viewModel.uiState.value is PlaygroundUiState.ViewingPlaygroundInfo ||
                    viewModel.uiState.value is PlaygroundUiState.ViewingPlaygroundSummary
                ) {
                    viewModel.updateUiState(PlaygroundUiState.FindingPlayground)
                }
            }

            if (viewModel.uiState.value is PlaygroundUiState.RegisteringPlayground) {
                registeringCircleOverlay.center = map.cameraPosition.target
                viewModel.updateRegisterPlaygroundBtnCameraIdle(cameraIdle = false)
            } else {
                registeringCircleOverlay.map = null
            }
        }

        map.addOnCameraIdleListener {
            if (viewModel.uiState.value is PlaygroundUiState.RegisteringPlayground) {
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
            when (uiState) {
                is PlaygroundUiState.LocationPermissionsNotGranted ->
                    locationPermission
                        .createAlarmDialog()
                        .show(parentFragmentManager, tag)

                is PlaygroundUiState.FindingPlayground -> hideRegisterPlaygroundScreen()
                is PlaygroundUiState.RegisteringPlayground -> showRegisterPlaygroundScreen()

                else -> return@observe
            }
        }

        viewModel.myPlayStatus.observe(viewLifecycleOwner) { myPlayStatus ->
            if (myPlayStatus == PlayStatus.NO_PLAYGROUND) {
                val myPlaygroundMarker = viewModel.myPlayground.value ?: return@observe
                myPlaygroundMarker.marker.map = null
                myPlaygroundMarker.circleOverlay.map = null
                pathOverlay.map = null
                balloon?.dismiss()
            }
        }

        viewModel.playgroundInfo.observe(viewLifecycleOwner) { playgroundInfo ->
            if (playgroundInfo != null) {
                petDetailAdapter.submitList(playgroundInfo.petDetails)

                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        if (playgroundInfo.petDetails.size <= EXPANDED_PET_SIZE) {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                        } else {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                        }
                    },
                    DELAY_MILLIS,
                )
            } else {
                petDetailAdapter.submitList(emptyList())
            }
        }

        viewModel.playgroundSummary.observe(viewLifecycleOwner) { playgroundSummary ->
            petSummaryAdapter.submitList(playgroundSummary.petImageUrls)
        }

        viewModel.myPlayground.observe(viewLifecycleOwner) { myPlaygroundMarker ->
            stopLocationService()
            if (myPlaygroundMarker != null) {
                myPlaygroundMarker.marker.map = map
                setUpPathOverlay()
                bottomSheetBehavior.isHideable = false
                viewModel.updatePlaygroundArrival(latLng)
                viewModel.loadPlaygroundInfo(myPlaygroundMarker.id)
            } else {
                bottomSheetBehavior.isHideable = true
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        viewModel.nearPlaygrounds.observe(viewLifecycleOwner) {
            clearNearPlaygroundMarkers()
            markNearPlaygroundMarkers()
        }

        viewModel.mapAction.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is MakeMyPlaygroundMarker -> {
                    val previousMyFootprintMarker = viewModel.myPlayground.value
                    if (previousMyFootprintMarker != null) {
                        previousMyFootprintMarker.marker.map = null
                    }

                    val marker = createMarker(playground = event.myPlayground)
                    val circleOverlay = createCircleOverlay(position = marker.position)
                    viewModel.loadMyPlayground(marker, circleOverlay)
                }

                is MakeNearPlaygroundMarkers -> {
                    val nearFootprintMarkers =
                        event.nearPlaygrounds.map { playground ->
                            val marker = createMarker(playground = playground)
                            val circleOverlay =
                                createCircleOverlay(
                                    position =
                                        LatLng(
                                            playground.latitude,
                                            playground.longitude,
                                        ),
                                )
                            PlaygroundMarkerUiModel(
                                id = playground.id,
                                marker = marker,
                                circleOverlay = circleOverlay,
                            )
                        }
                    clearNearPlaygroundMarkers()
                    viewModel.loadNearPlaygrounds(nearFootprintMarkers)
                }

                is PlaygroundMapAction.RegisterMyPlayground -> viewModel.registerMyPlayground(map.cameraPosition.target)

                is PlaygroundMapAction.MoveCameraCenterPosition -> moveCameraCenterPosition(event.position)

                is PlaygroundMapAction.ScanNearPlaygrounds -> viewModel.scanNearPlaygrounds()

                is PlaygroundMapAction.StartLocationService -> startLocationService()
            }
        }

        viewModel.changeTrackingModeAction.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is NoFollowTrackingMode -> map.locationTrackingMode = LocationTrackingMode.NoFollow

                is FollowTrackingMode -> map.locationTrackingMode = LocationTrackingMode.Follow

                is FaceTrackingMode -> map.locationTrackingMode = LocationTrackingMode.Face
            }
        }

        viewModel.alertAction.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is PlaygroundAlertAction.AlertHasNotLocationPermissionDialog ->
                    locationPermission
                        .createAlarmDialog()
                        .show(parentFragmentManager, tag)

                is AlertHasNotPetDialog -> showRegisterPetDialog()
                is PlaygroundAlertAction.AlertAddressOutOfKoreaSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.woof_address_out_of_korea,
                        ),
                    )

                is AlertMarkerRegisteredSnackbar -> showSnackbar(resources.getString(R.string.woof_marker_registered))
                is PlaygroundAlertAction.AlertNotExistMyPlaygroundSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.woof_not_exist_my_playground,
                        ),
                    )

                is PlaygroundAlertAction.AlertLeaveMyPlaygroundSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.playground_leave_my_playground,
                        ),
                    )

                is PlaygroundAlertAction.AlertAlreadyParticipatingInPlayground -> {
                    showSnackbar(
                        resources.getString(
                            R.string.playground_already_participating_in_playground,
                        ),
                    )
                }

                is PlaygroundAlertAction.AlertOverlapPlaygroundCreationSnackbar -> {
                    resources.getString(
                        R.string.playground_overlap_playground_creation,
                    )
                }

                is AlertEndWalkSnackbar -> showSnackbar(resources.getString(R.string.woof_stop_walk))
                is PlaygroundAlertAction.AlertFailToCheckPetExistence ->
                    showSnackbar(
                        resources.getString(
                            R.string.woof_fail_to_load_pet_existence_btn,
                        ),
                    )

                is PlaygroundAlertAction.AlertFailToLoadPlaygroundsSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.woof_fail_to_load_near_footprints,
                        ),
                    )

                is PlaygroundAlertAction.AlertFailToRegisterPlaygroundSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.woof_fail_to_register_playground,
                        ),
                    )

                is PlaygroundAlertAction.AlertFailToUpdatePlaygroundArrival ->
                    showSnackbar(
                        resources.getString(R.string.woof_fail_to_update_playground_arrival),
                    )

                is PlaygroundAlertAction.AlertFailToEndWalkSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.woof_fail_to_end_walk,
                        ),
                    )

                is PlaygroundAlertAction.AlertFailToDeleteMyFootprintSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.woof_fail_to_delete_my_footprint,
                        ),
                    )

                is PlaygroundAlertAction.AlertFailToLoadPlaygroundInfoSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.fail_to_load_playground_info,
                        ),
                    )

                is PlaygroundAlertAction.AlertFailToLoadPlaygroundSummarySnackbar ->
                    showSnackbar(
                        resources.getString(R.string.fail_to_load_playground_summary),
                    )

                is PlaygroundAlertAction.AlertFailToJoinPlaygroundSnackbar -> {
                    showSnackbar(
                        resources.getString(R.string.fail_to_join_playground),
                    )
                }

                is PlaygroundAlertAction.AlertHelpBalloon -> showHelpBalloon(event.textResId)
            }
        }

        viewModel.navigateAction.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is PlaygroundNavigateAction.NavigateToOtherProfile -> {
                    startActivity(
                        OtherProfileActivity.getIntent(
                            requireContext(),
                            event.memberId,
                        ),
                    )
                }
            }

            when (event) {
                is PlaygroundNavigateAction.NavigateToOtherProfile -> {
                    startActivity(
                        OtherProfileActivity.getIntent(
                            requireContext(),
                            event.memberId,
                        ),
                    )
                }

                is PlaygroundNavigateAction.NavigateToPetImage -> {
                    startActivity(
                        PetImageActivity.getIntent(
                            requireContext(),
                            event.petImageUrl,
                        ),
                    )
                }

                is PlaygroundNavigateAction.NavigateToPlaygroundMessage -> {
                    startActivity(
                        MessageActivity.getIntent(
                            requireContext(),
                            event.message,
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
            PlaygroundLocationReceiver(::updateLocation, ::leavePlayground)
        val intentFilter =
            IntentFilter().apply {
                addAction(PlaygroundLocationReceiver.ACTION_UPDATE_LOCATION)
                addAction(PlaygroundLocationReceiver.ACTION_LEAVE_PLAYGROUND)
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireContext().registerReceiver(
                walkReceiver,
                intentFilter,
                RECEIVER_NOT_EXPORTED,
            )
        } else {
            requireContext().registerReceiver(walkReceiver, intentFilter, RECEIVER_NOT_EXPORTED)
        }
    }

    private fun activateMap() {
        viewModel.updateUiState(PlaygroundUiState.Loading)
        locationSource.activate { location ->
            val lastLocation = location ?: return@activate
            latLng = LatLng(lastLocation.latitude, lastLocation.longitude)
            viewModel.updatePlaygrounds()
            moveCameraCenterPosition(latLng)
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    map.locationTrackingMode = LocationTrackingMode.Follow
                },
                DELAY_MILLIS,
            )
        }
    }

    private fun setupRegisteringCircleOverlay(position: LatLng) {
        registeringCircleOverlay.apply {
            center = position
            radius = PLAYGROUND_RADIUS
            color = resources.getColor(R.color.map_circle, null)
        }
        registeringCircleOverlay.map = map
    }

    private fun setUpPathOverlay() {
        pathOverlay.apply {
            coords =
                listOf(
                    latLng,
                    viewModel.myPlayground.value
                        ?.marker
                        ?.position,
                )
            width = 30
            outlineWidth = 0
            patternImage = OverlayImage.fromResource(R.drawable.ic_footprint)
            patternInterval = 60
            color = resources.getColor(R.color.blue, null)
            map = map
        }
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
            clickPlaygroundMarker(id = playground.id, marker = this)
        }
        marker.map = map

        return marker
    }

    private fun createCircleOverlay(position: LatLng): CircleOverlay {
        val circleOverlay = CircleOverlay()
        circleOverlay.apply {
            center = position
            radius = PLAYGROUND_RADIUS
            color = resources.getColor(R.color.map_circle, null)
        }
        circleOverlay.map = map
        return circleOverlay
    }

    private fun clickPlaygroundMarker(
        id: Long,
        marker: Marker,
    ) {
        marker.setOnClickListener {
            changeClickedMarkerSize(marker)
            viewModel.loadRecentlyClickedPlayground(marker)
            val position = adjustPosition(marker)
            moveCameraCenterPosition(position)

            if (id == viewModel.myPlayground.value?.id) {
                viewModel.loadPlaygroundInfo(id = id)
                viewModel.updateUiState(PlaygroundUiState.ViewingPlaygroundInfo)
            } else {
                viewModel.loadPlaygroundSummary(id = id)
                viewModel.updateUiState(PlaygroundUiState.ViewingPlaygroundSummary)
            }
            true
        }
    }

    private fun markerIcon(playground: Playground): Int =
        if (playground.isParticipating) R.drawable.ic_my_playground else R.drawable.ic_near_playground

    private fun showRegisterPlaygroundScreen() {
        val myPlayground = viewModel.myPlayground.value
        if (myPlayground != null) {
            myPlayground.marker.map = null
            myPlayground.circleOverlay.map = null
            pathOverlay.map = null
        }
        setupRegisteringCircleOverlay(map.cameraPosition.target)
        getAddress(map.cameraPosition.target)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                showHelpBalloon(textRestId = R.string.playground_register_help)
            },
            DELAY_MILLIS,
        )
    }

    private fun hideRegisterPlaygroundScreen() {
        val myPlayground = viewModel.myPlayground.value
        if (myPlayground != null) {
            myPlayground.marker.map = map
            myPlayground.circleOverlay.center = myPlayground.marker.position
            pathOverlay.map = map
        }
        registeringCircleOverlay.map = null
        changeRecentlyClickedMarkerSize()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun changeRecentlyClickedMarkerSize() {
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
            playgroundMarker.marker.map = null
            playgroundMarker.circleOverlay.map = null
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
            address
                .getAddressLine(0)
                .replace(resources.getString(R.string.woof_address_korea), "")
                .trimStart()
        viewModel.updateAddressLine(addressLine)

        val countryName = address.countryName
        val inKorea = countryName == resources.getString(R.string.woof_address_korea)
        viewModel.updateRegisterPlaygroundBtnInKorea(inKorea = inKorea)
    }

    private fun convertLtnLng(latLng: LatLng): LatLng = LatLng(floor(latLng.latitude * 100) / 100, floor(latLng.longitude * 100) / 100)

    private fun startLocationService() {
        val myPlayStatus = viewModel.myPlayStatus.value ?: return
        val intent =
            PlaygroundLocationService.getIntent(requireContext(), myPlayStatus).apply {
                action = ACTION_START
            }
        requireContext().startForegroundService(intent)
    }

    private fun updateLocation(location: Location) {
        latLng = LatLng(location.latitude, location.longitude)
        monitorDistanceAndManagePlayStatus()
    }

    private fun leavePlayground() {
        viewModel.leavePlayground()
    }

    private fun monitorDistanceAndManagePlayStatus() {
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

        if (withinPlaygroundRange(distance) || outOfPlaygroundRange(distance)) {
            viewModel.updatePlaygroundArrival(latLng)
            val myPlayground = viewModel.myPlayground.value ?: return
            viewModel.loadPlaygroundInfo(myPlayground.id)
        }
    }

    private fun withinPlaygroundRange(distance: Float): Boolean {
        val myPlayStatus = viewModel.myPlayStatus.value ?: return false
        return myPlayStatus == PlayStatus.AWAY && distance <= PLAYGROUND_RADIUS
    }

    private fun outOfPlaygroundRange(distance: Float): Boolean {
        val myPlayStatus = viewModel.myPlayStatus.value ?: return false
        return myPlayStatus == PlayStatus.PLAYING && distance > PLAYGROUND_RADIUS
    }

    private fun stopLocationService() {
        requireContext().stopService(
            Intent(
                requireContext(),
                PlaygroundLocationService::class.java,
            ),
        )
    }

    private fun markNearPlaygroundMarkers() {
        val nearPlaygroundMarkers = viewModel.nearPlaygrounds.value ?: return
        nearPlaygroundMarkers.forEach { playgroundMarker ->
            playgroundMarker.marker.map = map
            playgroundMarker.circleOverlay.map = map
        }
    }

    private fun setupRecyclerView() {
        binding.rcvPlaygroundPet.itemAnimator = null
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
                    if (viewModel.uiState.value == PlaygroundUiState.FindingPlayground && slideOffset >= 0.4f) {
                        viewModel.updateUiState(PlaygroundUiState.ViewingPlaygroundInfo)
                    }
                }

                override fun onStateChanged(
                    bottomSheet: View,
                    newState: Int,
                ) {
                    if (viewModel.uiState.value == PlaygroundUiState.ViewingPlaygroundInfo &&
                        newState == BottomSheetBehavior.STATE_COLLAPSED
                    ) {
                        changeRecentlyClickedMarkerSize()
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
            Balloon
                .Builder(requireContext())
                .setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP)
                .setText(text)
                .setTextColorResource(R.color.white)
                .setTextSize(14f)
                .setMarginBottom(10)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowSize(10)
                .setArrowPosition(0.5f)
                .setPadding(12)
                .setFocusable(false)
                .setCornerRadius(8f)
                .setBackgroundColorResource(R.color.coral400)
                .setBalloonAnimation(BalloonAnimation.ELASTIC)
                .setLifecycleOwner(viewLifecycleOwner)
                .build()

        balloon?.showAlignTop(binding.btnPlaygroundRegisteringHelp)
    }

    private fun showHelpBalloon(textRestId: Int) {
        val text = resources.getString(textRestId)
        showBalloon(text)
    }

    companion object {
        private const val PLAYGROUND_RADIUS = 150.0
        private const val MIN_ZOOM = 7.0
        private const val DEFAULT_ZOOM = 15.0
        private const val MARKER_DEFAULT_WIDTH = 96
        private const val MARKER_DEFAULT_HEIGHT = 128
        private const val MARKER_CLICKED_WIDTH = 144
        private const val MARKER_CLICKED_HEIGHT = 192
        private const val DELAY_MILLIS = 300L
        private const val MIN_KOREA_LATITUDE = 33.0
        private const val MAX_KOREA_LATITUDE = 39.0
        private const val MIN_KOREA_LONGITUDE = 125.0
        private const val MAX_KOREA_LONGITUDE = 132.0
        private const val MAP_LOGO_MARGIN = 20
        private const val EXPANDED_PET_SIZE = 2

        const val TAG = "PlaygroundFragment"
    }
}
