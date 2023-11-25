package com.example.shareapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shareapp.adapter.VenuesAdapter
import com.example.shareapp.viewmodel.VenuesViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.slider.RangeSlider
import com.google.android.material.snackbar.Snackbar


class MainActivity  : AppCompatActivity() {
    val REQUEST_LOCATION_PERMISSION = 25
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var viewModel: VenuesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val venuesList = findViewById<RecyclerView>(R.id.venues_list)
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        viewModel = ViewModelProvider(this)[VenuesViewModel::class.java]

        viewModel.venuesList.observe(this) {
            val adapter = VenuesAdapter(this, it)
            venuesList.adapter = adapter
            progressBar.visibility = View.GONE
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            viewModel.fetchVenues("7mi")
        }
        else
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_LOCATION_PERMISSION)


        venuesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!viewModel.isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    // End of the list reached, load more data
                    progressBar.visibility = View.VISIBLE
                    viewModel.fetchVenues("7mi")
                }
            }
        })

        val rangeSlider = findViewById<RangeSlider>(R.id.range_bar)
        rangeSlider.addOnChangeListener { slider, value, fromUser ->
            // Handle range changes
            val minValue = slider.values[0]
            val maxValue = slider.values[1]
            progressBar.visibility = View.VISIBLE
            viewModel.fetchVenues(value.toString()+"mi")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            viewModel.fetchVenues("7mi")
        } else {
            Snackbar.make(findViewById(android.R.id.content),"Permission Required",Snackbar.LENGTH_LONG).show();
        }
    }
}