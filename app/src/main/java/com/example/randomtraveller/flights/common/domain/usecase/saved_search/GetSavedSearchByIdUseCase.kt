package com.example.randomtraveller.flights.common.domain.usecase.saved_search

import com.example.randomtraveller.flights.common.domain.repository.SavedSearchesRepository
import javax.inject.Inject

class GetSavedSearchByIdUseCase @Inject constructor(
    private val savedSearchesRepository: SavedSearchesRepository
) {
    suspend operator fun invoke(id: Int) = savedSearchesRepository.getSavedSearchById(id)
}