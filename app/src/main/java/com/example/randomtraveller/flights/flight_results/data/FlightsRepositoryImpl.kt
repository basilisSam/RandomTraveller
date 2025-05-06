package com.example.randomtraveller.flights.flight_results.data

import com.apollographql.apollo.ApolloClient
import com.example.randomtraveller.flights.flight_results.domain.repository.FlightsRepository
import com.example.randomtraveller.flights.flight_results.domain.model.RoundTripFlightDetails
import com.example.randomtraveller.umbrella.SearchFlightsQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class FlightsRepositoryImpl @Inject constructor(
    @Named("umbrella") private val apolloClientUmbrella: ApolloClient,
    private val roundTripFlightDetailsMapper: RoundTripFlightDetailsMapper
) : FlightsRepository {
    
    override suspend fun searchFlights(
        sourceCityId: String,
        maxPrice: Int,
        outboundStartDate: String,
        outboundEndDate: String,
        inboundStartDate: String,
        inboundEndDate: String
    ): List<RoundTripFlightDetails> = withContext(Dispatchers.IO) {
        val query = SearchFlightsQuery(
            sourceId = sourceCityId,
            outboundDateStart = outboundStartDate,
            outboundDateEnd = outboundEndDate,
            inboundDateStart = inboundStartDate,
            inboundDateEnd = inboundEndDate,
            maxBudgetEnd = maxPrice
        )

        val response = apolloClientUmbrella.query(query).execute().data

        roundTripFlightDetailsMapper.mapDtoToDomain(response)
    }
}