package com.example.randomtraveller.flights.data.service

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AirportService {
    @GET("airports")
    suspend fun getAirports(
        @Query("name") cityName: String,
    ): Response<List<AirportData>>
}

data class AirportData(
    val icao: String?,
    val iata: String?,
    val name: String?,
    val city: String?,
    val region: String?,
    val country: String?,
    @SerializedName("elevation_ft") val elevationFt: String?,
    val latitude: String?,
    val longitude: String?,
    val timezone: String?,
)
