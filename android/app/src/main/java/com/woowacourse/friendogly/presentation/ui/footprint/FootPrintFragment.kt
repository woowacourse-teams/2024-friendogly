package com.woowacourse.friendogly.presentation.ui.footprint

import android.Manifest
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.FragmentFootPrintBinding
import com.woowacourse.friendogly.presentation.base.BaseFragment


class FootPrintFragment : BaseFragment<FragmentFootPrintBinding>(R.layout.fragment_foot_print) {
    private lateinit var footPrintMap: KakaoMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun initViewCreated() {
        binding.mvFootprintMap.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // 지도 API 가 정상적으로 종료될 때 호출됨
            }

            override fun onMapError(error: Exception) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                // 인증 후 API 가 정상적으로 실행될 때 호출됨
                footPrintMap = kakaoMap

                fusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(requireContext())

                locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                    }
                }

                val locationRequest =
                    LocationRequest.Builder(LocationRequest.PRIORITY_HIGH_ACCURACY, 100).build()

                requestLocationUpdates(locationRequest)

                binding.btnFootprintMark.setOnClickListener {
                    moveCameraCenterPosition()
                }

                footPrintMap.setOnLabelClickListener { map, labelLayer, label ->
                    // 라벨 클릭 시 이벤트 처리
                }
            }

            override fun getPosition(): LatLng {
                return super.getPosition()
            }

            override fun getZoomLevel(): Int {
                return 15
            }
        })
    }

    override fun onResume() {
        super.onResume()
        binding.mvFootprintMap.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.mvFootprintMap.pause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun requestLocationUpdates(locationRequest: LocationRequest) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    private fun moveCameraCenterPosition() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { lastLocation ->
                if (lastLocation != null) {
                    val latLng = LatLng.from(lastLocation.latitude, lastLocation.longitude)
                    val cameraUpdate = CameraUpdateFactory.newCenterPosition(latLng, 15)
                    footPrintMap.moveCamera(cameraUpdate, CameraAnimation.from(500, true, true))
                    markFootPrint(latLng)
                }
            }.addOnFailureListener { exception ->
                // 위치 정보를 가져오지 못했을 때 처리
            }
        }
    }

    private fun markFootPrint(latLng: LatLng) {
        val styles =
            footPrintMap.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.ic_footprint)))
        val options = LabelOptions.from(latLng).setStyles(styles).setTag("userId") // userId 를 tag로 설정
        val layer = footPrintMap.labelManager?.layer
        val label = layer?.addLabel(options)
        label?.show()
    }

    companion object {
        const val TAG = "FootPrintFragment"

        fun newInstance(): Fragment {
            return FootPrintFragment()
        }
    }
}
