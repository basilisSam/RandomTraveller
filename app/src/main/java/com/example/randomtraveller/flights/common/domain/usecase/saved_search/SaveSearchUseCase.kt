package com.example.randomtraveller.flights.common.domain.usecase.saved_search

import com.example.randomtraveller.flights.common.domain.model.SavedSearchDomain
import com.example.randomtraveller.flights.common.domain.repository.SavedSearchesRepository
import javax.inject.Inject

class SaveSearchUseCase @Inject constructor(
    private val savedSearchesRepository: SavedSearchesRepository
) {
    suspend operator fun invoke(savedSearchDomain: SavedSearchDomain) {
        savedSearchesRepository.saveSearch(savedSearchDomain)
    }
}