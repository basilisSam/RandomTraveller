package com.example.randomtraveller.flights.search_criteria.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.randomtraveller.core.ui.DateRangeSelectorDialog
import com.example.randomtraveller.core.ui.PrimaryButton
import com.example.randomtraveller.flights.common.model.SearchFlightsNavigationParams
import com.example.randomtraveller.flights.common.ui.components.SavedSearchItem
import com.example.randomtraveller.flights.search_criteria.ui.components.AirportSearchHint
import com.example.randomtraveller.flights.search_criteria.ui.components.AirportSuggestions
import com.example.randomtraveller.flights.search_criteria.ui.components.BudgetTextField
import com.example.randomtraveller.flights.search_criteria.ui.components.DateRangeButton
import com.example.randomtraveller.flights.search_criteria.ui.components.LeavingFromTextField
import com.example.randomtraveller.navigation.SearchFlights
import com.example.randomtraveller.ui.theme.RandomTravellerTheme
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun FlightSearchCriteriaScreen(
    modifier: Modifier,
    onSearchFlightsClicked: (SearchFlights) -> Unit,
    viewModel: FlightSearchCriteriaViewModel = hiltViewModel(),
) {

    NavigationHandler(viewModel.navigation, onSearchFlightsClicked)

    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    Content(screenState, viewModel::onAction, modifier)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Content(
    screenState: SearchFlightsScreenState,
    onAction: (OnAction) -> Unit,
    modifier: Modifier,
) {
    Scaffold(
        bottomBar = {
            PrimaryButton(
                text = "Search flights",
                modifier = Modifier.padding(bottom = 24.dp, top = 8.dp),
                isEnabled =
                    screenState.selectedDateRange.startDateInMillis != null &&
                            screenState.selectedDateRange.endDateInMillis != null &&
                            screenState.budgetText.text.isNotBlank() &&
                            screenState.airportText.text.isNotBlank(),

                ) {
                onAction.invoke(OnAction.OnSearchButtonClicked)
            }
        }
    ) { _ ->
        if (screenState.shouldShowCalendarPicker) {
            DateRangeSelectorDialog(
                screenState.selectedDateRange.startDateInMillis,
                screenState.selectedDateRange.endDateInMillis,
                onAction,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            LeavingFromTextField(onAction, screenState.airportText)

            if (screenState.areSuggestionsLoading) {
                AirportSearchHint()
            }

            if (screenState.airportSuggestions.isNotEmpty()) {
                AirportSuggestions(
                    suggestions = screenState.airportSuggestions,
                    onAirportSelected = onAction,
                )
            }

            BudgetTextField(onAction, screenState.budgetText)
            DateRangeButton(screenState.selectedDateRange, onAction)
            if (screenState.lastSearch != null) {
                Text(
                    text = "Last search",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(
                        start = 8.dp,
                        end = 16.dp,
                        top = 24.dp,
                        bottom = 8.dp
                    )
                )
                SavedSearchItem(
                    screenState.lastSearch,
                    { onAction.invoke(OnAction.OnLastSearchClicked) }
                )
            }
        }
    }
}

@Composable
private fun NavigationHandler(
    navigation: SharedFlow<SearchFlightsNavigationParams?>,
    onSearchFlightsClicked: (SearchFlights) -> Unit
) {
    LaunchedEffect(navigation) {
        navigation.collect { navigationEvent ->
            if (navigationEvent == null) {
                return@collect
            } else {
                val flightsSearch = SearchFlights(
                    cityId = navigationEvent.cityId,
                    maxPrice = navigationEvent.maxPrice,
                    outboundStartDate = navigationEvent.outboundStartDate,
                    outboundEndDate = navigationEvent.outboundEndDate,
                    inboundStartDate = navigationEvent.inboundStartDate,
                    inboundEndDate = navigationEvent.inboundEndDate,
                )
                onSearchFlightsClicked(flightsSearch)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun SearchFlightsScreenPreview() {
    RandomTravellerTheme {
        Content(SearchFlightsScreenState(), {}, Modifier)
    }
}
