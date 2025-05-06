package com.example.randomtraveller.flights.search_criteria.domain.usecase

import com.example.randomtraveller.flights.search_criteria.domain.repository.AirportsRepository
import javax.inject.Inject

class GetAirportsUseCase @Inject constructor(
    private val airportsRepository: AirportsRepository
) {
    suspend fun getAirports(cityName: String) = airportsRepository.getAirports(cityName)
}