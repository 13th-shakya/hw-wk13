package com.example.kotlinhomework5

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kotlinhomework5.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadMap()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val allGranted = grantResults.isNotEmpty() && grantResults.all {
            it == PackageManager.PERMISSION_GRANTED
        }

        if (allGranted) {
            loadMap()
        } else {
            finish()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        val isAccessFineLocationGranted = ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val isAccessCoarseLocationGranted = ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!isAccessFineLocationGranted || !isAccessCoarseLocationGranted) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0
            )
            return
        }

        map.isMyLocationEnabled = true

        val taipei = LatLng(25.035, 121.54)
        val taipei101 = LatLng(25.033611, 121.565000)
        val taipeiMainStation = LatLng(25.047924, 121.517081)
        val daanPark = LatLng(25.032435, 121.534905)

        with(map) {
            addMarker(MarkerOptions().apply {
                position(taipei101)
                title("台北101")
                draggable(true)
            })
            addMarker(MarkerOptions().apply {
                position(taipeiMainStation)
                title("台北車站")
                draggable(true)
            })
            addPolyline(PolylineOptions().apply {
                add(taipei101)
                add(daanPark)
                add(taipeiMainStation)
                color(Color.BLUE)
            }).width = 10f
            moveCamera(
                CameraUpdateFactory.newLatLngZoom(taipei, 13f)
            )
        }
    }

    private fun loadMap() {
        with (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment) {
            getMapAsync(this@MainActivity)
        }
    }
}
