package com.example.randomtraveller.flights.saved_searches.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomtraveller.flights.common.domain.usecase.saved_search.DeleteSavedSearchUseCase
import com.example.randomtraveller.flights.common.domain.usecase.saved_search.GetSavedSearchByIdUseCase
import com.example.randomtraveller.flights.common.domain.usecase.saved_search.GetSavedSearchesUseCase
import com.example.randomtraveller.flights.common.model.SearchFlightsNavigationParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedSearchesViewModel @Inject constructor(
    getSavedSearchesUseCase: GetSavedSearchesUseCase,
    private val deleteSavedSearchUseCase: DeleteSavedSearchUseCase,
    private val getSavedSearchByIdUseCase: GetSavedSearchByIdUseCase,
    private val savedSearchMapper: SavedSearchMapper
) : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<SearchFlightsNavigationParams?>()
    val navigationEvent = _navigationEvent

    val savedSearches = getSavedSearchesUseCase().map { savedSearches ->
        savedSearches.map { search -> savedSearchMapper.mapSavedSearchDomainToUi(search) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteSavedSearch(savedSearch: SavedSearchUi) {
        viewModelScope.launch {
            deleteSavedSearchUseCase.invoke(savedSearch.id)
        }
    }

    fun onSavedSearchClicked(savedSearchId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val savedSearchDomain = getSavedSearchByIdUseCase(savedSearchId)
            if (savedSearchDomain != null) {
                val searchFlightsNavigationParams = SearchFlightsNavigationParams(
                    cityId = savedSearchDomain.cityId,
                    maxPrice = savedSearchDomain.maxPrice,
                    outboundStartDate = savedSearchDomain.outboundStartDate,
                    outboundEndDate = savedSearchDomain.outboundEndDate,
                    inboundStartDate = savedSearchDomain.inboundStartDate,
                    inboundEndDate = savedSearchDomain.inboundEndDate
                )
                _navigationEvent.emit(searchFlightsNavigationParams)
            }
        }
    }
}

data class SavedSearchUi(
    val id: Int,
    val airportInfo: String,
    val dateRange: String,
    val maxPrice: String,
    val iata: String
)
