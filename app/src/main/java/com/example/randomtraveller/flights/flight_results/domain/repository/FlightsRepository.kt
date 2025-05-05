package com.example.randomtraveller.flights.flight_results.domain.repository

import com.example.randomtraveller.flights.flight_results.domain.model.RoundTripFlightDetails

interface FlightsRepository {
    suspend fun searchFlights(
        sourceCityId: String,
        maxPrice: Int,
        outboundStartDate: String,
        outboundEndDate: String,
        inboundStartDate: String,
        inboundEndDate: String
    ): List<RoundTripFlightDetails>
}