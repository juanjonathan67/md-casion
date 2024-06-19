package com.example.casion.views.mapview

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.casion.BuildConfig
import com.example.casion.R
import com.example.casion.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchByTextRequest
import kotlin.math.cos

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var placesClient: PlacesClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // location permission
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener { finish() }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Define a variable to hold the Places API key.
        val apiKey = BuildConfig.PLACES_API_KEY

        // Log an error if apiKey is not set.
        if (apiKey.isEmpty() || apiKey == "DEFAULT_API_KEY") {
            Log.e("Places test", "No api key")
            finish()
            return
        }

        // Initialize the SDK
        Places.initializeWithNewPlacesApiEnabled(applicationContext, apiKey)

        // Create a new PlacesClient instance
        placesClient = Places.createClient(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        getMyLastLocation()

    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun getMyLastLocation() {
        if     (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    mMap.isMyLocationEnabled = true
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))

                    // Calculate bounds
                    val (southwest, northeast) = calculateBounds(latLng, 5.0) // 10 km radius

                    getNearbyPuskesmas(
                        southwest,
                        northeast
                    ).observe(this) { result ->
                        Log.d("MAPS DEBUG", result.toString())
                        for (place in result) {
                            mMap.addMarker(
                                MarkerOptions()
                                    .position(place.latLng)
                                    .title(place.name)
                            )
                        }
                    }
                } else {
                    Toast.makeText(
                        this@MapsActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun getNearbyPuskesmas(southwest: LatLng, northEast: LatLng) : LiveData<List<Place>> {
        val result = MutableLiveData<List<Place>>()

        val placeField = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

        val searchByTextRequest = SearchByTextRequest.builder("puskesmas terdekat", placeField)
            .setMaxResultCount(100)
            .setLocationRestriction(RectangularBounds.newInstance(southwest, northEast))
            .build()

        placesClient.searchByText(searchByTextRequest)
            .addOnSuccessListener { response ->
                result.value = response.places
            }

        return result
    }

    // Function to calculate new coordinates
    private fun calculateBounds(center: LatLng, distanceInKm: Double): Pair<LatLng, LatLng> {
        val earthRadius = 6371.0 // Earth's radius in kilometers

        val lat = center.latitude
        val lng = center.longitude

        // Convert distance to radians
        val distanceInRad = distanceInKm / earthRadius

        // Calculate new latitude and longitude for the southwest corner
        val southLat = lat - Math.toDegrees(distanceInRad)
        val westLng = lng - Math.toDegrees(distanceInRad / cos(Math.toRadians(lat)))

        // Calculate new latitude and longitude for the northeast corner
        val northLat = lat + Math.toDegrees(distanceInRad)
        val eastLng = lng + Math.toDegrees(distanceInRad / cos(Math.toRadians(lat)))

        val southwest = LatLng(southLat, westLng)
        val northeast = LatLng(northLat, eastLng)

        return Pair(southwest, northeast)
    }
}