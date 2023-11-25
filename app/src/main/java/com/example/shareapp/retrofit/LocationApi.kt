package com.example.shareapp.retrofit

import com.example.shareapp.model.VenuesList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApi {

    @GET("/venues")
    suspend fun getVenues(@Query("client_id") clientId: String, @Query("per_page") pageSize: Int,
                          @Query("page")currentPage: Int,@Query("lon")long: Number, @Query("lat")lat: Number, @Query("range")range: String) : Response<VenuesList>
}