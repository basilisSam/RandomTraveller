package com.example.randomtraveller.flights.common.ui.mapper

import com.example.randomtraveller.core.utils.toPrettyLocalDate
import com.example.randomtraveller.flights.common.domain.model.SavedSearchDomain
import com.example.randomtraveller.flights.common.model.SavedSearchUi
import javax.inject.Inject

class SavedSearchDomainToUiMapper @Inject constructor() {

    fun mapSavedSearchesDomainToUi(
        savedSearchesDomain: List<SavedSearchDomain>
    ): List<SavedSearchUi> = savedSearchesDomain.map {
        mapSavedSearchDomainToUi(it)
    }

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