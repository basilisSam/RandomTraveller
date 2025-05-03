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
    val cityId: String,
    val maxPrice: Int,
    val departureStartDate: String,
    val departureEndDate: String,
)
