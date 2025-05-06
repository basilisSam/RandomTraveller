package com.example.randomtraveller.flights.common.data

import com.example.randomtraveller.core.database.SavedSearchDTO
import com.example.randomtraveller.flights.common.domain.model.SavedSearchDomain
import javax.inject.Inject

class SavedSearchMapper @Inject constructor() {
    fun savedSearchDomainToDTO(savedSearchDomain: SavedSearchDomain): SavedSearchDTO {
        return SavedSearchDTO(
            id = savedSearchDomain.id,
            cityId = savedSearchDomain.cityId,
            airportName = savedSearchDomain.airportName,
            iata = savedSearchDomain.iata,
            maxPrice = savedSearchDomain.maxPrice,
            outboundStartDate = savedSearchDomain.outboundStartDate,
            outboundEndDate = savedSearchDomain.outboundEndDate,
            inboundStartDate = savedSearchDomain.inboundStartDate,
            inboundEndDate = savedSearchDomain.inboundEndDate
        )
    }

    fun savedSearchDTOToDomain(savedSearchDTO: SavedSearchDTO): SavedSearchDomain {
        return SavedSearchDomain(
            id = savedSearchDTO.id,
            cityId = savedSearchDTO.cityId,
            airportName = savedSearchDTO.airportName,
            iata = savedSearchDTO.iata,
            maxPrice = savedSearchDTO.maxPrice,
            outboundStartDate = savedSearchDTO.outboundStartDate,
            outboundEndDate = savedSearchDTO.outboundEndDate,
            inboundStartDate = savedSearchDTO.inboundStartDate,
            inboundEndDate = savedSearchDTO.inboundEndDate
        )
    }
}