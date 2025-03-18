package com.example.randomtraveller.flights.ui

import android.icu.text.NumberFormat
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomtraveller.core.utils.toFormattedDateString
import com.example.randomtraveller.core.utils.toLocalDate
import com.example.randomtraveller.flights.data.AirportSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SearchFlightsViewModel @Inject constructor(
    private val airportSearchRepository: AirportSearchRepository
) : ViewModel() {

    private val _screenState: MutableStateFlow<SearchFlightsScreenState> = MutableStateFlow(
        SearchFlightsScreenState()
    )
    val screenState: StateFlow<SearchFlightsScreenState>
        get() = _screenState.asStateFlow()

    private var airportSuggestionsJob: Job? = null

    fun onAction(action: OnAction) {
        when (action) {
            is OnAction.OnAirportSuggestionSelected -> selectAirportSuggestion(action.suggestion)
            is OnAction.OnUpdateAirportText -> updateAirportText(action.newText)
            is OnAction.OnUpdateBudget -> updateBudget(action.newBudget)
            is OnAction.OnShowCalendarPicker -> showCalendarPicker()
            is OnAction.OnDismissDatePicker -> dismissDatePicker()
            is OnAction.OnDateSelected -> selectStartingDate(action.dateInMillis)
            is OnAction.OnTripDurationChanged -> updateTripDuration(action.tripDuration)
        }
    }

    private fun updateAirportText(newText: String) {
        if (newText.length > 3) {
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
        _screenState.update {
            it.copy(
                airportText = TextFieldValue(
                    text = newText,
                    selection = TextRange(newText.length)
                )
            )
        }
    }

    private fun updateBudget(newBudget: String) {
        val formattedBudget = if (newBudget.isEmpty()) {
            ""
        } else {
            val cleanedBudget = newBudget.filter { it.isDigit() }.toLong()
            val formatter = NumberFormat.getInstance(Locale.getDefault())
            formatter.format(cleanedBudget)
        }
        _screenState.update {
            it.copy(
                budgetText = TextFieldValue(
                    text = formattedBudget,
                    selection = TextRange(formattedBudget.length)
                )
            )
        }
    }

    private fun selectAirportSuggestion(suggestion: AirportSuggestion) {
        cancelAirportSuggestionsJob()
        val airportText = "(${suggestion.iata}) ${suggestion.name}"
        _screenState.update {
            it.copy(
                airportText = TextFieldValue(airportText, TextRange(airportText.length)),
                airportSuggestions = emptyList(),
                areSuggestionsLoading = false
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

    private fun selectStartingDate(startDateInMillis: Long?) {
        startDateInMillis ?: return

        val localDate = startDateInMillis.toLocalDate()
        val formattedText = localDate.toFormattedDateString()

        _screenState.update {
            it.copy(
                startDate = SelectedStartDate(
                    startDateText = formattedText,
                    startLocalDate = localDate,
                    startDateInMillis = startDateInMillis
                )
            )
        }
    }

    private fun updateTripDuration(duration: String) {
        val cleanedDuration = duration.filter { it.isDigit() }
        _screenState.update {
            it.copy(tripDuration = cleanedDuration)
        }
    }

    private fun searchAirports() {
        cancelAirportSuggestionsJob()
        _screenState.update {
            it.copy(areSuggestionsLoading = true)
        }
        airportSuggestionsJob = viewModelScope.launch {
            val suggestions = listOf(
                AirportSuggestion("Athens", "ATH"),
                AirportSuggestion("Athens", "ATH2"),
                AirportSuggestion("Athens", "ATH3"),
                AirportSuggestion("Athens", "ATH4"),
                AirportSuggestion("Athens", "ATH5"),
                AirportSuggestion("Athens", "ATH6"),
                AirportSuggestion("Athens", "ATH7"),
                AirportSuggestion("Athens", "ATH8"),
                AirportSuggestion("Athens", "ATH9"),
                AirportSuggestion("Athens", "ATH10"),
                AirportSuggestion("Athens", "ATH11"),
            )
            delay(3000)
            ensureActive()
            _screenState.update {
                it.copy(
                    airportSuggestions = suggestions,
                    areSuggestionsLoading = false
                )
            }
        }
    }

    private fun cancelAirportSuggestionsJob() {
        airportSuggestionsJob?.cancel()
        airportSuggestionsJob = null
    }
}

data class SearchFlightsScreenState(
    val airportText: TextFieldValue = TextFieldValue(),
    val budgetText: TextFieldValue = TextFieldValue(),
    val startDate: SelectedStartDate = SelectedStartDate(),
    val tripDuration: String = "",
    val shouldShowCalendarPicker: Boolean = false,
    val airportSuggestions: List<AirportSuggestion> = emptyList(),
    val areSuggestionsLoading: Boolean = false
)

data class SelectedStartDate(
    val startDateText: String? = null,
    val startLocalDate: LocalDate? = null,
    val startDateInMillis: Long? = null
)

data class AirportSuggestion(
    val name: String,
    val iata: String,
)

sealed class OnAction {
    data class OnUpdateAirportText(val newText: String) : OnAction()
    data class OnUpdateBudget(val newBudget: String) : OnAction()
    data class OnAirportSuggestionSelected(val suggestion: AirportSuggestion) : OnAction()
    data object OnShowCalendarPicker : OnAction()
    data object OnDismissDatePicker : OnAction()
    data class OnDateSelected(val dateInMillis: Long?) : OnAction()
    data class OnTripDurationChanged(val tripDuration: String) : OnAction()
}