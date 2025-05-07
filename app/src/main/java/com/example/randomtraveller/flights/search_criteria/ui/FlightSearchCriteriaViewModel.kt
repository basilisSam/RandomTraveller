package com.example.randomtraveller.flights.search_criteria.ui

import android.icu.text.NumberFormat
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomtraveller.core.utils.toLocalDate
import com.example.randomtraveller.core.utils.toUtcIsoEndOfDayString
import com.example.randomtraveller.core.utils.toUtcIsoStartOfDayString
import com.example.randomtraveller.flights.common.domain.model.SavedSearchDomain
import com.example.randomtraveller.flights.common.domain.usecase.saved_search.GetSavedSearchesUseCase
import com.example.randomtraveller.flights.common.domain.usecase.saved_search.SaveSearchUseCase
import com.example.randomtraveller.flights.common.model.SavedSearchUi
import com.example.randomtraveller.flights.common.model.SearchFlightsNavigationParams
import com.example.randomtraveller.flights.common.ui.mapper.SavedSearchDomainToUiMapper
import com.example.randomtraveller.flights.search_criteria.domain.usecase.GetAirportsUseCase
import com.example.randomtraveller.flights.search_criteria.ui.mapper.AirportsMapper
import com.example.randomtraveller.flights.search_criteria.ui.mapper.SavedSearchMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FlightSearchCriteriaViewModel @Inject constructor(
    private val getAirportsUseCase: GetAirportsUseCase,
    private val saveSearchUseCase: SaveSearchUseCase,
    getSavedSearchesUseCase: GetSavedSearchesUseCase,
    private val savedSearchMapper: SavedSearchMapper,
    private val savedSearchDomainToUiMapper: SavedSearchDomainToUiMapper,
    private val airportsMapper: AirportsMapper
) : ViewModel() {

    private val _screenState: MutableStateFlow<SearchFlightsScreenState> =
        MutableStateFlow(
            SearchFlightsScreenState(),
        )
    val screenState: StateFlow<SearchFlightsScreenState>
        get() = _screenState.asStateFlow()

    private val lastSearchUiModelFlow: StateFlow<SavedSearchDomain?> =
        getSavedSearchesUseCase.invoke()
            .map { domainList ->
                domainList.firstOrNull()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = null
            )

    private val lastSearchStateUpdaterJob: Job = viewModelScope.launch {
        lastSearchUiModelFlow.collect { lastSavedSearchDomain ->
            val lastSavedSearchUi = if (lastSavedSearchDomain == null) {
                null
            } else {
                savedSearchDomainToUiMapper.mapSavedSearchDomainToUi(lastSavedSearchDomain)
            }
            _screenState.update { currentState ->
                currentState.copy(lastSearch = lastSavedSearchUi)
            }
        }
    }

    private val _navigation: MutableSharedFlow<SearchFlightsNavigationParams?> = MutableSharedFlow()
    val navigation: SharedFlow<SearchFlightsNavigationParams?> = _navigation.asSharedFlow()

    private var airportSuggestionsJob: Job? = null

    fun onAction(action: OnAction) {
        when (action) {
            is OnAction.OnAirportSuggestionSelected -> selectAirportSuggestion(action.suggestion)
            is OnAction.OnUpdateAirportText -> updateAirportText(action.newText)
            is OnAction.OnUpdateBudget -> updateBudget(action.newBudget)
            is OnAction.OnShowCalendarPicker -> showCalendarPicker()
            is OnAction.OnDismissDatePicker -> dismissDatePicker()
            is OnAction.OnDateRangeSelected -> selectDateRange(
                action.startDateInMillis,
                action.endDateInMillis,
            )

            is OnAction.OnSearchButtonClicked -> handleSearchButtonClick()
            is OnAction.OnLastSearchClicked -> onLastSearchClicked()
        }
    }

    private fun updateAirportText(newText: String) {
        _screenState.update {
            it.copy(
                airportText = TextFieldValue(
                    text = newText,
                    selection = TextRange(newText.length),
                ),
            )
        }
        if (newText.length > 2) {
            searchAirports()
        } else if (newText.isEmpty()) {
            cancelAirportSuggestionsJob()
            _screenState.update {
                it.copy(
                    airportSuggestions = emptyList(),
                    areSuggestionsLoading = false,
                )
            }
        }
    }

    private fun updateBudget(newBudget: String) {
        val formattedBudget =
            if (newBudget.isEmpty()) {
                ""
            } else {
                val cleanedBudget = newBudget.filter { it.isDigit() }.toLong()
                val formatter = NumberFormat.getInstance(Locale.getDefault())
                formatter.format(cleanedBudget)
            }
        _screenState.update {
            it.copy(
                budgetText =
                    TextFieldValue(
                        text = formattedBudget,
                        selection = TextRange(formattedBudget.length),
                    ),
            )
        }
    }

    private fun selectAirportSuggestion(suggestion: AirportSuggestion) {
        cancelAirportSuggestionsJob()
        val airportText = "(${suggestion.iata}) ${suggestion.name}"
        _screenState.update {
            it.copy(
                airportText = TextFieldValue(airportText, TextRange(airportText.length)),
                selectedAirportSuggestion = suggestion,
                airportSuggestions = emptyList(),
                areSuggestionsLoading = false,
            )
        }
    }

    private fun showCalendarPicker() {
        _screenState.update {
            it.copy(shouldShowCalendarPicker = true)
        }
    }

    private fun dismissDatePicker() {
        _screenState.update {
            it.copy(shouldShowCalendarPicker = false)
        }
    }

    private fun selectDateRange(
        startDateInMillis: Long?,
        endDateInMillis: Long?,
    ) {
        if (startDateInMillis == null || endDateInMillis == null) return

        val startDate = startDateInMillis.toLocalDate()
        val endDate = endDateInMillis.toLocalDate()
        val formattedText =
            "${startDate.dayOfMonth}/${startDate.monthValue}/${startDate.year} - ${endDate.dayOfMonth}/${endDate.monthValue}/${endDate.year}"

        _screenState.update {
            it.copy(
                selectedDateRange =
                    SelectedDateRange(
                        startDateText = formattedText,
                        startLocalDate = startDate,
                        startDateInMillis = startDateInMillis,
                        endLocalDate = endDate,
                        endDateInMillis = endDateInMillis,
                    ),
            )
        }
    }

    private fun searchAirports() {
        cancelAirportSuggestionsJob()
        _screenState.update {
            it.copy(areSuggestionsLoading = true)
        }
        airportSuggestionsJob =
            viewModelScope.launch {
                val airportsDomain =
                    getAirportsUseCase.getAirports(_screenState.value.airportText.text)
                val airportSuggestions =
                    airportsMapper.mapAirportsDomainToUi(airportsDomain)

                ensureActive()
                _screenState.update {
                    it.copy(
                        airportSuggestions = airportSuggestions,
                        areSuggestionsLoading = false,
                    )
                }
            }
    }

    private fun cancelAirportSuggestionsJob() {
        airportSuggestionsJob?.cancel()
        airportSuggestionsJob = null
    }

    private fun handleSearchButtonClick() {
        viewModelScope.launch {
            val outboundStartDate =
                _screenState.value.selectedDateRange.startLocalDate!!.toUtcIsoStartOfDayString()
            val outboundEndDate =
                _screenState.value.selectedDateRange.startLocalDate!!.toUtcIsoEndOfDayString()
            val inboundStartDate =
                _screenState.value.selectedDateRange.endLocalDate!!.toUtcIsoStartOfDayString()
            val inboundEndDate =
                _screenState.value.selectedDateRange.endLocalDate!!.toUtcIsoEndOfDayString()
            val cityId = _screenState.value.selectedAirportSuggestion!!.cityId
            val maxPrice = _screenState.value.budgetText.text.toIntOrNull() ?: 0

            val searchFlightsData = SearchFlightsNavigationParams(
                cityId = cityId,
                maxPrice = maxPrice,
                outboundStartDate = outboundStartDate,
                outboundEndDate = outboundEndDate,
                inboundStartDate = inboundStartDate,
                inboundEndDate = inboundEndDate
            )

            val suggestedAirportSelection = _screenState.value.selectedAirportSuggestion

            if (suggestedAirportSelection != null) {
                val savedSearchDomain = savedSearchMapper.flightSearchCriteriaToDomain(
                    cityId = cityId,
                    airportName = suggestedAirportSelection.name,
                    iata = suggestedAirportSelection.iata,
                    maxPrice = maxPrice,
                    outboundStartDate = outboundStartDate,
                    outboundEndDate = outboundEndDate,
                    inboundStartDate = inboundStartDate,
                    inboundEndDate = inboundEndDate
                )

                saveSearchUseCase.invoke(savedSearchDomain)
            }

            _navigation.emit(searchFlightsData)
        }
    }

    private fun onLastSearchClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            val savedSearchDomain = lastSearchUiModelFlow.value
            if (savedSearchDomain != null) {
                val searchFlightsNavigationParams = SearchFlightsNavigationParams(
                    cityId = savedSearchDomain.cityId,
                    maxPrice = savedSearchDomain.maxPrice,
                    outboundStartDate = savedSearchDomain.outboundStartDate,
                    outboundEndDate = savedSearchDomain.outboundEndDate,
                    inboundStartDate = savedSearchDomain.inboundStartDate,
                    inboundEndDate = savedSearchDomain.inboundEndDate
                )
                _navigation.emit(searchFlightsNavigationParams)
            }
        }
    }
}

data class SearchFlightsScreenState(
    val airportText: TextFieldValue = TextFieldValue(),
    val selectedAirportSuggestion: AirportSuggestion? = null,
    val budgetText: TextFieldValue = TextFieldValue(),
    val selectedDateRange: SelectedDateRange = SelectedDateRange(),
    val tripDuration: String = "",
    val shouldShowCalendarPicker: Boolean = false,
    val airportSuggestions: List<AirportSuggestion> = emptyList(),
    val areSuggestionsLoading: Boolean = false,
    val lastSearch: SavedSearchUi? = null
)

data class SelectedDateRange(
    val startDateText: String? = null,
    val startLocalDate: LocalDate? = null,
    val startDateInMillis: Long? = null,
    val endLocalDate: LocalDate? = null,
    val endDateInMillis: Long? = null,
)

data class AirportSuggestion(
    val id: String,
    val cityId: String,
    val name: String,
    val iata: String,
)

sealed class OnAction {
    data class OnUpdateAirportText(val newText: String) : OnAction()

    data class OnUpdateBudget(val newBudget: String) : OnAction()

    data class OnAirportSuggestionSelected(val suggestion: AirportSuggestion) : OnAction()

    data object OnShowCalendarPicker : OnAction()

    data object OnDismissDatePicker : OnAction()

    data class OnDateRangeSelected(
        val startDateInMillis: Long?,
        val endDateInMillis: Long?,
    ) : OnAction()

    data object OnSearchButtonClicked : OnAction()

    data object OnLastSearchClicked : OnAction()
}
