package com.example.randomtraveller.flights.common.model

data class SavedSearchUi(
    val id: Int,
    val airportInfo: String,
    val dateRange: String,
    val maxPrice: String,
    val iata: String
)