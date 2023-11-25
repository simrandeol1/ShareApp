package com.example.shareapp.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shareapp.retrofit.LocationApi
import com.example.shareapp.retrofit.RetrofitHelper
import com.example.shareapp.model.Venues
import com.example.shareapp.model.VenuesList
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class VenuesViewModel(application: Application):AndroidViewModel(application) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
    private var currentPage = 1
    var isLoading = false

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val viewModelScope = CoroutineScope(coroutineContext)
    private val currentList: MutableList<Venues> = mutableListOf()
    private val _venuesList: MutableLiveData<MutableList<Venues>> = MutableLiveData()
    val venuesList: LiveData<MutableList<Venues>> get() = _venuesList
    val locationApi = RetrofitHelper.getInstance().create(LocationApi::class.java)

    @SuppressLint("MissingPermission")
    fun fetchVenues(range: String){
        if (!isLoading) {
            currentPage++
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    viewModelScope.launch {
                        isLoading = true
                        addData(locationApi.getVenues("Mzg0OTc0Njl8MTcwMDgxMTg5NC44MDk2NjY5", 10, currentPage, longitude, latitude, range).body())
                        isLoading = false
                    }
                }
            }
        }
    }

    private fun addData(newData: VenuesList?){
        if(newData != null) {
            currentList.addAll(newData.venues)
            _venuesList.postValue(currentList)
        }
    }
}