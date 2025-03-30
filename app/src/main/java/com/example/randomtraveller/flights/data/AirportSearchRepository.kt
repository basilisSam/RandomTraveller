package com.example.randomtraveller.flights.data

import com.example.randomtraveller.flights.data.service.AirportData
import com.example.randomtraveller.flights.data.service.AirportService
import javax.inject.Inject

class AirportSearchRepository @Inject constructor(private val airportService: AirportService) {
    suspend fun getAirports(cityName: String): List<AirportData> {
        return try {
            val response = airportService.getAirports(cityName)
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
