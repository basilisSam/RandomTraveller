package com.example.randomtraveller.flights.saved_searches.ui

import com.example.randomtraveller.flights.common.domain.model.SavedSearchDomain
import com.example.randomtraveller.core.utils.toPrettyLocalDate
import javax.inject.Inject

class SavedSearchMapper @Inject constructor() {
    fun mapSavedSearchDomainToUi(savedSearch: SavedSearchDomain): SavedSearchUi {
        return SavedSearchUi(
            id = savedSearch.id,
            airportInfo = "(${savedSearch.iata}) ${savedSearch.airportName}",
            dateRange = "${savedSearch.outboundStartDate.toPrettyLocalDate()} - ${savedSearch.inboundStartDate.toPrettyLocalDate()}",
            maxPrice = "Max price: ${savedSearch.maxPrice}",
            iata = savedSearch.iata
        )
    }
}