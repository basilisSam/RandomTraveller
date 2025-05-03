package com.example.randomtraveller.flights.search_flights.data

import com.apollographql.apollo.ApolloClient
import com.example.randomtraveller.umbrella.GetAirportsQuery
import javax.inject.Inject
import javax.inject.Named

class AirportSearchRepository @Inject constructor(
    @Named("umbrella") private val apolloClient: ApolloClient
) {
    suspend fun getAirports(cityName: String): List<GetAirportsQuery.Edge?>? {
        return apolloClient.query(GetAirportsQuery(cityName))
            .execute().data?.places?.onPlaceConnection?.edges
    }
}
