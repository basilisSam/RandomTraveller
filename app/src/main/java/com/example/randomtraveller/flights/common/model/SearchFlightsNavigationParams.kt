package com.example.randomtraveller.flights.common.model

data class SearchFlightsNavigationParams(
    val cityId: String,
    val maxPrice: Int,
    val outboundStartDate: String,
    val outboundEndDate: String,
    val inboundStartDate: String,
    val inboundEndDate: String,
)