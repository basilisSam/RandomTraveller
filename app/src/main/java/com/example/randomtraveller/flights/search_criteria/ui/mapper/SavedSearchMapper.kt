package com.example.randomtraveller.flights.search_criteria.ui.mapper

import com.example.randomtraveller.flights.common.domain.model.SavedSearchDomain
import javax.inject.Inject

class SavedSearchMapper @Inject constructor() {
    fun flightSearchCriteriaToDomain(
        cityId: String,
        airportName: String,
        iata: String,
        maxPrice: Int,
        outboundStartDate: String,
        outboundEndDate: String,
        inboundStartDate: String,
        inboundEndDate: String
    ): SavedSearchDomain {
        return SavedSearchDomain(
            cityId = cityId,
            airportName = airportName,
            iata = iata,
            maxPrice = maxPrice,
            outboundStartDate = outboundStartDate,
            outboundEndDate = outboundEndDate,
            inboundStartDate = inboundStartDate,
            inboundEndDate = inboundEndDate
        )
    }
}