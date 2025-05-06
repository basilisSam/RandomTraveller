package com.example.randomtraveller.flights.common.domain.model

data class SavedSearchDomain(
    val id: Int = 0,
    val cityId: String,
    val airportName: String,
    val iata: String,
    val maxPrice: Int,
    val outboundStartDate: String,
    val outboundEndDate: String,
    val inboundStartDate: String,
    val inboundEndDate: String
)