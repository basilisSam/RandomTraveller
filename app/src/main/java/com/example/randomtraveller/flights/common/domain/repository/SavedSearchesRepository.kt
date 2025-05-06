package com.example.randomtraveller.flights.common.domain.repository

import com.example.randomtraveller.flights.common.domain.model.SavedSearchDomain
import kotlinx.coroutines.flow.Flow

interface SavedSearchesRepository {
    suspend fun saveSearch(searchDomain: SavedSearchDomain)
    suspend fun getSavedSearchById(savedSearchId: Int): SavedSearchDomain?
    fun getSavedSearches(): Flow<List<SavedSearchDomain>>
    suspend fun deleteSavedSearch(savedSearchId: Int)
}