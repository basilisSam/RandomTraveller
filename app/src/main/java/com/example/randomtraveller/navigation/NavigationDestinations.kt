package com.example.randomtraveller.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object SplashScreen

@Serializable
object SearchFlightCriteria

@Serializable
data class SearchFlights(
    val cityId: String,
    val maxPrice: Int,
    val outboundStartDate: String,
    val outboundEndDate: String,
    val inboundStartDate: String,
    val inboundEndDate: String
)

@Serializable
object SavedFlightSearches