package com.example.randomtraveller.flights.flight_results.data

import com.apollographql.apollo.ApolloClient
import com.example.randomtraveller.umbrella.SearchFlightsQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class FlightsRepository @Inject constructor(
    @Named("umbrella") private val apolloClientUmbrella: ApolloClient
) {

    /**
     * Executes the SearchDirectReturnGoodDealsQuery.
     * WARNING: This version assumes the happy path only - no error handling,
     * no checking for null data, and returns the raw Apollo generated type.
     */
    suspend fun searchFlights(
        sourceCityId: String,
        maxPrice: Int,
        outboundStartDate: String,
        outboundEndDate: String,
        inboundStartDate: String,
        inboundEndDate: String
    ): SearchFlightsQuery.Data? = withContext(Dispatchers.IO) {
        // 1. Create the query instance with variables
        val query = SearchFlightsQuery(
            sourceId = sourceCityId,
            outboundDateStart = outboundStartDate,
            outboundDateEnd = outboundEndDate,
            inboundDateStart = inboundStartDate,
            inboundDateEnd = inboundEndDate,
            maxBudgetEnd = maxPrice
        )

        // 2. Execute the query
        val response = apolloClientUmbrella.query(query).execute()

        // 3. Return the data part directly (can be null)
        //    NO error checking, NO union handling in this simple version.
        response.data
    }
}