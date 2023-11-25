package com.example.shareapp.model

data class VenuesList(val venues: List<Venues>)
data class Venues(val name:String, val url: String, val location: Location, val city: String, val address: String)
data class Location(val lat: Number, val long: Number)