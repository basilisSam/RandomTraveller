package com.example.randomtraveller.flights.search_criteria.domain.repository

import com.example.randomtraveller.flights.search_criteria.domain.model.AirportDomain

interface AirportsRepository {
    suspend fun getAirports(cityName: String): List<AirportDomain>
}