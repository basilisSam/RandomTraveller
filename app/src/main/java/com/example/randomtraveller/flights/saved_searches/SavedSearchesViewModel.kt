package com.example.randomtraveller.flights.saved_searches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomtraveller.core.data.SavedSearchesRepository
import com.example.randomtraveller.core.database.SavedSearch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedSearchesViewModel @Inject constructor(
    private val savedSearchesRepository: SavedSearchesRepository
) : ViewModel() {

    val savedSearches = savedSearchesRepository.getSavedSearches()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteSavedSearch(search: SavedSearch) {
        viewModelScope.launch {
            savedSearchesRepository.deleteSavedSearch(search)
        }
    }
}