package com.example.randomtraveller.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object SplashScreen

@Serializable
object SearchFlight

@Serializable
data class FlightResults(
    val outboundDateMillis: Long,
    val inboundDateMillis: Long,
    val departureAirportIata: String,
    val maxBudget: String
)
