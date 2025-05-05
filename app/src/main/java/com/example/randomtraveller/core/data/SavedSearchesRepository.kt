package com.example.randomtraveller.core.data

import com.example.randomtraveller.core.database.SavedSearch
import com.example.randomtraveller.core.database.SavedSearchDao
import com.example.randomtraveller.flights.search_criteria.ui.AirportSuggestion
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedSearchesRepository @Inject constructor(
    private val savedSearchDao: SavedSearchDao
) {

    suspend fun saveSearch(
        airportSuggestion: AirportSuggestion,
        maxPrice: Int,
        outboundStartDate: String,
        outboundEndDate: String,
        inboundStartDate: String,
        inboundEndDate: String
    ) {
        val savedSearch = SavedSearch(
            cityId = airportSuggestion.cityId,
            airportName = airportSuggestion.name,
            iata = airportSuggestion.iata,
            maxPrice = maxPrice,
            outboundStartDate = outboundStartDate,
            outboundEndDate = outboundEndDate,
            inboundStartDate = inboundStartDate,
            inboundEndDate = inboundEndDate
        )
        savedSearchDao.insert(savedSearch)
    }

    fun getSavedSearches(): Flow<List<SavedSearch>> {
        return savedSearchDao.getAll()
    }
}