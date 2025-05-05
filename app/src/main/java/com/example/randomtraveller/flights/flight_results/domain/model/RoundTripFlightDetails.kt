package com.example.randomtraveller.flights.flight_results.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class RoundTripFlightDetails(
    val id: String,
    val priceAmount: BigDecimal?,
    val currencyCode: String?,
    val bookingUrlPath: String?,
    val outboundLeg: FlightLegDetails?,
    val inboundLeg: FlightLegDetails?
)

data class FlightLegDetails(
    val departureTimeLocal: LocalDateTime?,
    val arrivalTimeLocal: LocalDateTime?,
    val durationSeconds: Int?,
    val originAirportCode: String?,
    val destinationAirportCode: String?,
    val originCityName: String?,
    val finalDestinationCityName: String?,
    val carrierCode: String?
)