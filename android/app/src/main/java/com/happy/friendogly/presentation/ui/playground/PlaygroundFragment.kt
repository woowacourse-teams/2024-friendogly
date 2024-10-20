package com.happy.friendogly.presentation.ui.playground

import android.app.Activity
import android.content.Context
import android.content.Context.RECEIVER_EXPORTED
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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import com.happy.friendogly.presentation.ui.otherprofile.OtherProfileActivity
import com.happy.friendogly.presentation.ui.permission.LocationPermission
import com.happy.friendogly.presentation.ui.petimage.PetImageActivity
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertAction
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertAction.AlertHasNotPetDialog
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertAction.AlertMarkerRegisteredSnackbar
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundMapAction
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundMapAction.MakeMyPlaygroundMarker
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundMapAction.MakeNearPlaygroundMarkers
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundNavigateAction
import com.happy.friendogly.presentation.ui.playground.adapter.PetDetailAdapter
import com.happy.friendogly.presentation.ui.playground.adapter.PetSummaryAdapter
import com.happy.friendogly.presentation.ui.playground.model.PlayStatus
import com.happy.friendogly.presentation.ui.playground.model.Playground
import com.happy.friendogly.presentation.ui.playground.service.PlaygroundLocationReceiver
import com.happy.friendogly.presentation.ui.playground.service.PlaygroundLocationService
import com.happy.friendogly.presentation.ui.playground.service.PlaygroundLocationService.Companion.ACTION_START
import com.happy.friendogly.presentation.ui.playground.state.PlaygroundUiState
import com.happy.friendogly.presentation.ui.playground.uimodel.PlaygroundUiModel
import com.happy.friendogly.presentation.ui.playground.util.ANIMATE_DURATION_MILLIS
import com.happy.friendogly.presentation.ui.playground.util.hideViewAnimation
import com.happy.friendogly.presentation.ui.playground.util.showViewAnimation
import com.happy.friendogly.presentation.ui.playground.viewmodel.PlaygroundViewModel
import com.happy.friendogly.presentation.ui.statemessage.StateMessageActivity
import com.happy.friendogly.presentation.utils.isSystemInDarkMode
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.CameraUpdate.REASON_GESTURE
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.LocationTrackingMode.Face
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
class PlaygroundFragment : Fragment(), OnMapReadyCallback {
    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private var _binding: FragmentPlaygroundBinding? = null
    private val binding get() = _binding!!
    private var snackbar: Snackbar? = null
    private var balloon: Balloon? = null

    private lateinit var map: NaverMap
    private lateinit var latLng: LatLng
    private lateinit var locationPermission: LocationPermission
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private lateinit var playgroundReceiver: PlaygroundLocationReceiver
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private val mapView: MapView by lazy { binding.mapView }
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

    private val petDetailAdapter by lazy { PetDetailAdapter(viewModel) }
    private val petSummaryAdapter by lazy { PetSummaryAdapter() }

    private val viewModel by viewModels<PlaygroundViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewModel.uiState.value !is PlaygroundUiState.FindingPlayground) {
                        viewModel.updateUiState(PlaygroundUiState.FindingPlayground())
                    } else {
                        requireActivity().finish()
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
        setupRecyclerView()
        setupBottomSheetBehavior()
        setupActivityResultLauncher()
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
        requireContext().unregisterReceiver(playgroundReceiver)
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
        setUpMap(naverMap)
        if (locationPermission.hasPermissions()) {
            activateMap()
        }
    }

    private fun setUpMap(naverMap: NaverMap) {
        map = naverMap
        map.mapType = NaverMap.MapType.Navi
        map.isNightModeEnabled = isSystemInDarkMode()
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
        binding.lbvPlaygroundLocation.map = map
        binding.lbvPlaygroundRegisterLocation.map = map

        map.onMapClickListener =
            NaverMap.OnMapClickListener { _, _ ->
                reduceMarkerSize()
                if (viewModel.uiState.value is PlaygroundUiState.ViewingPlaygroundInfo ||
                    viewModel.uiState.value is PlaygroundUiState.ViewingPlaygroundSummary
                ) {
                    viewModel.updateUiState(PlaygroundUiState.FindingPlayground())
                }
            }

        map.addOnLocationChangeListener { location ->
            latLng = LatLng(location.latitude, location.longitude)

            if (viewModel.myPlayStatus.value != PlayStatus.NO_PLAYGROUND &&
                viewModel.uiState.value !is PlaygroundUiState.RegisteringPlayground
            ) {
                viewModel.updatePathOverlay(latLng)
            }
        }

        map.addOnCameraChangeListener { reason, _ ->
            val currentState = viewModel.uiState.value
            if (reason == REASON_GESTURE) {
                reduceMarkerSize()
                viewModel.changeTrackingModeToNoFollow()

                when (currentState) {
                    is PlaygroundUiState.FindingPlayground -> {
                        if (!currentState.refreshBtnVisible) {
                            viewModel.updateRefreshBtnVisibility(
                                refreshBtnVisible = true,
                            )
                        }
                    }

                    is PlaygroundUiState.RegisteringPlayground -> {
                        currentState.circleOverlay.center = map.cameraPosition.target
                        viewModel.updateCameraIdle(cameraIdle = false)
                    }

                    is PlaygroundUiState.ViewingPlaygroundSummary, PlaygroundUiState.ViewingPlaygroundInfo -> {
                        viewModel.updateUiState(
                            PlaygroundUiState.FindingPlayground(
                                refreshBtnVisible = true,
                            ),
                        )
                    }

                    else -> return@addOnCameraChangeListener
                }
            }
        }

        map.addOnCameraIdleListener {
            val currentState = viewModel.uiState.value
            if (currentState is PlaygroundUiState.RegisteringPlayground) {
                viewModel.updateCameraIdle(cameraIdle = true)
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
                    locationPermission.createAlarmDialog()
                        .show(parentFragmentManager, tag)

                is PlaygroundUiState.FindingPlayground -> {
                    viewModel.showMyPlayground(map)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }

                is PlaygroundUiState.RegisteringPlayground -> {
                    setUpRegisteringCircleOverlay(uiState, map.cameraPosition.target)
                }

                else -> return@observe
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
                    ANIMATE_DURATION_MILLIS,
                )
            }
        }

        viewModel.playgroundSummary.observe(viewLifecycleOwner) { playgroundSummary ->
            petSummaryAdapter.submitList(playgroundSummary.petImageUrls)
        }

        viewModel.myPlayground.observe(viewLifecycleOwner) { myPlayground ->
            stopLocationService()
            if (myPlayground != null) {
                enlargeMarkerSize(myPlayground.marker)
                bottomSheetBehavior.isHideable = false
                viewModel.updatePlaygroundArrival(latLng)
            } else {
                bottomSheetBehavior.isHideable = true
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        viewModel.mapAction.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is MakeMyPlaygroundMarker -> {
                    val marker = createMarker(playground = event.myPlayground)
                    val circleOverlay = createCircleOverlay(position = marker.position)
                    val pathOverlay = createPathOverlay(marker.position)
                    viewModel.loadMyPlayground(marker, circleOverlay, pathOverlay)
                }

                is MakeNearPlaygroundMarkers -> {
                    val nearPlaygrounds =
                        event.nearPlaygrounds.map { playground ->
                            PlaygroundUiModel(
                                id = playground.id,
                                marker = createMarker(playground = playground),
                                circleOverlay =
                                createCircleOverlay(
                                    position =
                                    LatLng(
                                        playground.latitude,
                                        playground.longitude,
                                    ),
                                ),
                            )
                        }
                    viewModel.loadNearPlaygrounds(nearPlaygrounds)
                }

                is PlaygroundMapAction.RegisterMyPlayground -> viewModel.registerMyPlayground(map.cameraPosition.target)

                is PlaygroundMapAction.MoveCameraCenterPosition -> moveCameraCenterPosition(event.position)

                is PlaygroundMapAction.ScanNearPlaygrounds -> viewModel.scanNearPlaygrounds()

                is PlaygroundMapAction.ShowRegisteringPlaygroundScreen -> {
                    getAddress(map.cameraPosition.target)
                    binding.layoutPlaygroundRegisterMarker.showViewAnimation()
                    Handler(Looper.getMainLooper()).postDelayed(
                        {
                            showHelpBalloon(textRestId = R.string.playground_register_help)
                        },
                        ANIMATE_DURATION_MILLIS,
                    )
                }

                is PlaygroundMapAction.HideRegisteringPlaygroundScreen -> {
                    binding.layoutPlaygroundRegisterMarker.hideViewAnimation()
                }

                is PlaygroundMapAction.StartLocationService -> startLocationService()

                is PlaygroundMapAction.NoFollowTrackingMode ->
                    map.locationTrackingMode =
                        LocationTrackingMode.NoFollow

                is PlaygroundMapAction.FollowTrackingMode ->
                    map.locationTrackingMode =
                        LocationTrackingMode.Follow

                is PlaygroundMapAction.FaceTrackingMode -> map.locationTrackingMode = Face
            }
        }

        viewModel.alertAction.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is PlaygroundAlertAction.AlertHasNotLocationPermissionDialog ->
                    locationPermission.createAlarmDialog()
                        .show(parentFragmentManager, tag)

                is AlertHasNotPetDialog -> showRegisterPetDialog()
                is PlaygroundAlertAction.AlertAddressOutOfKoreaSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.playground_address_out_of_korea,
                        ),
                    )

                is AlertMarkerRegisteredSnackbar -> showSnackbar(resources.getString(R.string.playground_marker_registered))
                is PlaygroundAlertAction.AlertNotExistMyPlaygroundSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.playground_not_exist_my_playground,
                        ),
                    )

                is PlaygroundAlertAction.AlertLeaveMyPlaygroundSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.playground_leave_my_playground,
                        ),
                    )

                is PlaygroundAlertAction.AlertAutoLeavePlaygroundSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.playground_leave_my_playground,
                        ),
                    )

                is PlaygroundAlertAction.AlertOverlapPlaygroundCreationSnackbar -> {
                    showSnackbar(
                        resources.getString(
                            R.string.playground_overlap_playground_creation,
                        ),
                    )
                }

                is PlaygroundAlertAction.AlertAlreadyParticipatePlaygroundSnackbar -> {
                    showSnackbar(
                        resources.getString(
                            R.string.playground_already_participate_playground,
                        ),
                    )
                }

                is PlaygroundAlertAction.AlertFailToCheckPetExistence ->
                    showSnackbar(
                        resources.getString(
                            R.string.playground_fail_to_load_pet_existence_btn,
                        ),
                    )

                is PlaygroundAlertAction.AlertFailToLoadPlaygroundsSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.playground_fail_to_load_near_playgrounds,
                        ),
                    )

                is PlaygroundAlertAction.AlertFailToRegisterPlaygroundSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.playground_fail_to_register_playground,
                        ),
                    )

                is PlaygroundAlertAction.AlertFailToUpdatePlaygroundArrival ->
                    showSnackbar(
                        resources.getString(R.string.playground_fail_to_update_playground_arrival),
                    )

                is PlaygroundAlertAction.AlertFailToLeavePlaygroundSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.playground_fail_to_leave,
                        ),
                    )

                is PlaygroundAlertAction.AlertFailToLoadPlaygroundInfoSnackbar ->
                    showSnackbar(
                        resources.getString(
                            R.string.playground_fail_to_load_playground_info,
                        ),
                    )

                is PlaygroundAlertAction.AlertFailToLoadPlaygroundSummarySnackbar ->
                    showSnackbar(
                        resources.getString(R.string.playground_fail_to_load_playground_summary),
                    )

                is PlaygroundAlertAction.AlertFailToJoinPlaygroundSnackbar -> {
                    showSnackbar(
                        resources.getString(R.string.playground_fail_to_join_playground),
                    )
                }

                is PlaygroundAlertAction.AlertHelpBalloon -> showHelpBalloon(event.textResId)
            }
        }

        viewModel.navigateAction.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is PlaygroundNavigateAction.NavigateToOtherProfile -> {
                    val intent = OtherProfileActivity.getIntent(requireContext(), event.memberId)
                    startActivity(intent)
                }

                is PlaygroundNavigateAction.NavigateToPetImage -> {
                    val intent = PetImageActivity.getIntent(requireContext(), event.petImageUrl)
                    startActivity(intent)
                }

                is PlaygroundNavigateAction.NavigateToPlaygroundMessage -> {
                    val intent = StateMessageActivity.getIntent(requireContext(), event.message)
                    activityResultLauncher.launch(intent)
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
        playgroundReceiver = PlaygroundLocationReceiver(::updateLocation, ::leavePlayground)
        val intentFilter =
            IntentFilter().apply {
                addAction(PlaygroundLocationReceiver.ACTION_UPDATE_LOCATION)
                addAction(PlaygroundLocationReceiver.ACTION_LEAVE_PLAYGROUND)
            }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireContext().registerReceiver(
                playgroundReceiver,
                intentFilter,
                RECEIVER_EXPORTED,
            )
        } else {
            requireContext().registerReceiver(playgroundReceiver, intentFilter)
        }
    }

    private fun activateMap() {
        locationSource.activate { location ->
            val lastLocation = location ?: return@activate
            latLng = LatLng(lastLocation.latitude, lastLocation.longitude)
            moveCameraCenterPosition(latLng)
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    map.locationTrackingMode = LocationTrackingMode.Follow
                },
                ANIMATE_DURATION_MILLIS,
            )
            setupObserving()
            viewModel.updatePlaygrounds()
        }
    }

    private fun setUpRegisteringCircleOverlay(
        uiState: PlaygroundUiState.RegisteringPlayground,
        position: LatLng,
    ) {
        uiState.circleOverlay.apply {
            center = position
            radius = PLAYGROUND_RADIUS
            color = resources.getColor(R.color.map_circle, null)
        }
        uiState.circleOverlay.map = map
    }

    private fun createPathOverlay(position: LatLng): PathOverlay {
        val pathOverlay = PathOverlay()
        pathOverlay.apply {
            coords =
                listOf(
                    latLng,
                    position,
                )
            width = PATH_OVERLAY_WIDTH
            outlineWidth = PATH_OVERLAY_OUTLINE_WIDTH
            patternImage = OverlayImage.fromResource(R.drawable.ic_footprint)
            patternInterval = PATH_OVERLAY_PATTERN_INTERVAL
            color = resources.getColor(R.color.blue, null)
        }
        pathOverlay.map = map

        return pathOverlay
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
            reduceMarkerSize()
            enlargeMarkerSize(marker)
            viewModel.loadRecentlyClickedPlayground(marker)
            val position = adjustPosition(marker)
            moveCameraCenterPosition(position)
            viewModel.handlePlaygroundInfo(id)
            true
        }
    }

    private fun markerIcon(playground: Playground): Int =
        if (playground.isParticipating) R.drawable.ic_my_playground else R.drawable.ic_near_playground

    private fun reduceMarkerSize() {
        val recentlyClickedMarker = viewModel.recentlyClickedPlayground.value ?: return
        recentlyClickedMarker.width = MARKER_DEFAULT_WIDTH
        recentlyClickedMarker.height = MARKER_DEFAULT_HEIGHT
    }

    private fun enlargeMarkerSize(marker: Marker) {
        marker.width = MARKER_CLICKED_WIDTH
        marker.height = MARKER_CLICKED_HEIGHT
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
                updateAddress(addresses)
            }
        } else {
            val addresses =
                geocoder.getFromLocation(addressLatLng.latitude, addressLatLng.longitude, 1)
                    ?: return
            updateAddress(addresses)
        }
    }

    private fun updateAddress(addresses: List<Address>) {
        if (addresses.isEmpty()) return
        val firstAddress = addresses[0]
        val address =
            firstAddress.getAddressLine(0)
                .replace(resources.getString(R.string.playground_address_korea), "").trimStart()
        val countryName = firstAddress.countryName
        val inKorea = countryName == resources.getString(R.string.playground_address_korea)
        viewModel.updateAddressAndInKorea(address = address, inKorea = inKorea)
    }

    private fun convertLtnLng(latLng: LatLng): LatLng =
        LatLng(floor(latLng.latitude * 100) / 100, floor(latLng.longitude * 100) / 100)

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
        viewModel.monitorDistanceAndManagePlayStatus(latLng)
    }

    private fun leavePlayground() {
        viewModel.hideMyPlayground()
        viewModel.leavePlayground()
        viewModel.updatePlaygrounds()
    }

    private fun stopLocationService() {
        requireContext().stopService(
            Intent(
                requireContext(),
                PlaygroundLocationService::class.java,
            ),
        )
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

    private fun setupActivityResultLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val messageUpdated =
                        result.data?.getBooleanExtra(EXTRA_MESSAGE_UPDATED, DEFAULT_MESSAGE_UPDATED)
                            ?: return@registerForActivityResult

                    if (messageUpdated) {
                        viewModel.playgroundMessageUpdated()
                    }
                }
            }
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
                    if (viewModel.uiState.value is PlaygroundUiState.ViewingPlaygroundInfo &&
                        newState == BottomSheetBehavior.STATE_COLLAPSED
                    ) {
                        reduceMarkerSize()
                        binding.rcvPlaygroundPet.smoothScrollToPosition(0)
                        viewModel.updateUiState(PlaygroundUiState.FindingPlayground())
                    }

                    if (viewModel.uiState.value is PlaygroundUiState.FindingPlayground &&
                        (newState == BottomSheetBehavior.STATE_HALF_EXPANDED || newState == BottomSheetBehavior.STATE_EXPANDED)
                    ) {
                        viewModel.updateUiState(PlaygroundUiState.ViewingPlaygroundInfo)
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
        private const val MIN_KOREA_LATITUDE = 33.0
        private const val MAX_KOREA_LATITUDE = 39.0
        private const val MIN_KOREA_LONGITUDE = 125.0
        private const val MAX_KOREA_LONGITUDE = 132.0
        private const val MAP_LOGO_MARGIN = 30
        private const val EXPANDED_PET_SIZE = 2
        private const val PATH_OVERLAY_WIDTH = 30
        private const val PATH_OVERLAY_OUTLINE_WIDTH = 0
        private const val PATH_OVERLAY_PATTERN_INTERVAL = 60
        private const val DEFAULT_MESSAGE_UPDATED = false

        const val EXTRA_MESSAGE_UPDATED = "messageUpdated"
        const val TAG = "PlaygroundFragment"
    }
}
