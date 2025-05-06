package com.example.randomtraveller.flights.common.domain.usecase.saved_search

import com.example.randomtraveller.flights.common.domain.repository.SavedSearchesRepository
import javax.inject.Inject

class GetSavedSearchesUseCase @Inject constructor(
    private val savedSearchesRepository: SavedSearchesRepository
) {
    operator fun invoke() = savedSearchesRepository.getSavedSearches()
}