package com.example.randomtraveller.flights.search_criteria.data

import com.apollographql.apollo.ApolloClient
import com.example.randomtraveller.flights.search_criteria.domain.model.AirportDomain
import com.example.randomtraveller.flights.search_criteria.domain.repository.AirportsRepository
import com.example.randomtraveller.umbrella.GetAirportsQuery
import javax.inject.Inject
import javax.inject.Named

class AirportsRepositoryImpl @Inject constructor(
    @Named("umbrella") private val apolloClient: ApolloClient,
    private val airportsDTOToDomainMapper: AirportsDTOToDomainMapper
) : AirportsRepository {
    override suspend fun getAirports(cityName: String): List<AirportDomain> {
        val airportsDTO = apolloClient.query(GetAirportsQuery(cityName))
            .execute().data

        return airportsDTOToDomainMapper.mapAirportsDTOToDomain(airportsDTO)
    }
}
