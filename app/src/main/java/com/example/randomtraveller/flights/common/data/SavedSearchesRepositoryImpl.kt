package com.example.randomtraveller.flights.common.data

import com.example.randomtraveller.core.database.SavedSearchDao
import com.example.randomtraveller.flights.common.domain.model.SavedSearchDomain
import com.example.randomtraveller.flights.common.domain.repository.SavedSearchesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedSearchesRepositoryImpl @Inject constructor(
    private val savedSearchDao: SavedSearchDao,
    private val savedSearchMapper: SavedSearchMapper
) : SavedSearchesRepository {

    override suspend fun saveSearch(
        savedSearchDomain: SavedSearchDomain
    ) {
        val savedSearchDTO = savedSearchMapper.savedSearchDomainToDTO(savedSearchDomain)
        savedSearchDao.insert(savedSearchDTO)
    }

    override fun getSavedSearches(): Flow<List<SavedSearchDomain>> {
        return savedSearchDao.getAll().map { savedSearchesFlow ->
            savedSearchesFlow.map { savedSearchDTO ->
                savedSearchMapper.savedSearchDTOToDomain(
                    savedSearchDTO
                )
            }
        }
    }

    override suspend fun getSavedSearchById(savedSearchId: Int): SavedSearchDomain? {
        val savedSearchDTO = savedSearchDao.getById(savedSearchId) ?: return null
        return savedSearchMapper.savedSearchDTOToDomain(savedSearchDTO)
    }

    override suspend fun deleteSavedSearch(savedSearchId: Int) {
        savedSearchDao.deleteById(savedSearchId)
    }
}