package com.example.randomtraveller.flights.search_criteria.data

import com.example.randomtraveller.flights.search_criteria.domain.model.AirportDomain
import com.example.randomtraveller.umbrella.GetAirportsQuery
import javax.inject.Inject

class AirportsDTOToDomainMapper @Inject constructor() {
    fun mapAirportsDTOToDomain(airportsDTO: GetAirportsQuery.Data?): List<AirportDomain> =
        airportsDTO?.places?.onPlaceConnection?.edges?.mapNotNull { edge ->
            val airportDTO = edge?.node?.onStation ?: return@mapNotNull null
            val cityId = airportDTO.city?.id ?: return@mapNotNull null
            val airportCode = airportDTO.code ?: return@mapNotNull null
            AirportDomain(
                id = airportDTO.id,
                cityId = cityId,
                name = airportDTO.name,
                iata = airportCode,
            )
        } ?: emptyList()
}