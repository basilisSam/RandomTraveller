package com.example.randomtraveller.flights.search_criteria.ui.mapper

import com.example.randomtraveller.flights.search_criteria.domain.model.AirportDomain
import com.example.randomtraveller.flights.search_criteria.ui.AirportSuggestion
import javax.inject.Inject

class AirportsMapper @Inject constructor() {
    fun mapAirportsDomainToUi(airports: List<AirportDomain>): List<AirportSuggestion> =
        airports.map { airport ->
            AirportSuggestion(
                id = airport.id,
                cityId = airport.cityId,
                name = airport.name,
                iata = airport.iata
            )
        }
}