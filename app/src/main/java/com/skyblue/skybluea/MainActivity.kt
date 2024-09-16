package com.skyblue.skybluea

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity() , OnMapReadyCallback{
    private val PERMISSION_ID = 77
    private var locationPermissionGranted = false
    private lateinit var mMap: GoogleMap

    val Maharashtra = LatLng(18.8072136, 74.5307497)
    val Uttar_Pradesh = LatLng(26.8467811, 80.9111396)
    val Delhi = LatLng(28.6863446, 77.1867635)

    private var locationArrayList:  ArrayList<LatLng>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager.findFragmentById(
            R.id.map_fragment
        ) as? SupportMapFragment

        mapFragment?.getMapAsync(this)

        locationArrayList = ArrayList()
        locationArrayList!!.add(Maharashtra)
        locationArrayList!!.add(Uttar_Pradesh)
        locationArrayList!!.add(Delhi)
    }


    // Check if location permissions are
    // granted to the application
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    // Request permissions if not granted before
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    // function to check if GPS is on
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    // What must happen when permission is granted
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Granted. Start getting the location information
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mMap = p0

                for (i in locationArrayList!!.indices){
                    mMap.addMarker(MarkerOptions().position(locationArrayList!![i]).title("Marker"))
                    val zoomLevel = 18.0.toFloat()
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList!!.get(i)))
                }

            }else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }else {
            requestPermissions()
        }
    }
}