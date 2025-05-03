package com.example.randomtraveller.flights.ui.search_flights

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.randomtraveller.R
import com.example.randomtraveller.core.ui.PrimaryButton
import com.example.randomtraveller.core.ui.TitledTextField
import com.example.randomtraveller.core.ui.TitledTextFieldLikeButton
import com.example.randomtraveller.flights.ui.MyDatePicker
import com.example.randomtraveller.navigation.FlightResults
import com.example.randomtraveller.ui.theme.RandomTravellerTheme
import com.firebase.ui.auth.AuthUI

@Composable
fun SearchFlightScreen(
    modifier: Modifier,
    onSearchFlightsClicked: (FlightResults) -> Unit,
    viewModel: SearchFlightsViewModel = hiltViewModel(),
) {

    LaunchedEffect(viewModel.navigation) {
        viewModel.navigation.collect { navigationEvent ->
            if (navigationEvent == null) {
                return@collect
            } else {
                val flightsSearch = FlightResults(
                    cityId = navigationEvent.cityId,
                    maxPrice = navigationEvent.maxPrice,
                    departureStartDate = navigationEvent.departureStartDate,
                    departureEndDate = navigationEvent.departureEndDate
                )
                onSearchFlightsClicked(flightsSearch)
            }
        }
    }

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
    RandomTravellerTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(title = {
                    Text(
                        "Search Flight",
                        fontWeight = FontWeight.Bold,
                    )
                })
            },
            bottomBar = {
                PrimaryButton(
                    text = "Search flights",
                    modifier = Modifier.padding(bottom = 24.dp),
                    isEnabled =
                        screenState.selectedDateRange.startDateInMillis != null &&
                                screenState.selectedDateRange.endDateInMillis != null &&
                                screenState.budgetText.text.isNotBlank() &&
                                screenState.airportText.text.isNotBlank(),

                    ) {
                    onAction.invoke(OnAction.OnSearchButtonClicked)
                }
            }
        ) { innerPadding ->
            if (screenState.shouldShowCalendarPicker) {
                MyDatePicker(
                    screenState.selectedDateRange.startDateInMillis,
                    screenState.selectedDateRange.endDateInMillis,
                    onAction,
                )
            }
            Column(
                modifier =
                    modifier
                        .padding(innerPadding)
                        .padding(horizontal = 8.dp),
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

                val context = LocalContext.current
                Button(onClick = {
                    AuthUI.getInstance().signOut(context)
                }) {
                    Text(stringResource(R.string.sign_out))
                }
            }
        }
    }
}

@Composable
private fun DateRangeButton(
    startDate: SelectedDateRange,
    onAction: (OnAction) -> Unit,
) {
    TitledTextFieldLikeButton(
        headerText = stringResource(R.string.dates),
        placeholderText =
            startDate.startDateText
                ?: stringResource(R.string.select_dates),
        trailingIcon = R.drawable.ic_calendar,
        onClick = { onAction(OnAction.OnShowCalendarPicker) },
        modifier =
            Modifier
                .padding(top = 24.dp)
                .clickable { onAction(OnAction.OnShowCalendarPicker) },
    )
}

@Composable
private fun BudgetTextField(
    onAction: (OnAction) -> Unit,
    budgetText: TextFieldValue,
) {
    TitledTextField(
        headerText = stringResource(R.string.budget),
        placeholderText = stringResource(R.string.enter_budget),
        trailingIcon = R.drawable.ic_dollar,
        onValueChange = { onAction(OnAction.OnUpdateBudget(it)) },
        currentText = budgetText,
        keyboardType = KeyboardType.Number,
        modifier = Modifier.padding(top = 24.dp),
    )
}

@Composable
private fun AirportSearchHint() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(Modifier.width(8.dp))
        CircularProgressIndicator(
            modifier = Modifier.size(12.dp),
            strokeWidth = 2.dp,
        )
        Spacer(Modifier.width(8.dp))
        Text(stringResource(R.string.searching_airports))
    }
}

@Composable
private fun LeavingFromTextField(
    onAction: (OnAction) -> Unit,
    airportText: TextFieldValue,
) {
    TitledTextField(
        headerText = stringResource(R.string.leaving_from),
        placeholderText = stringResource(R.string.enter_airport),
        trailingIcon = R.drawable.ic_airplane,
        onValueChange = { onAction(OnAction.OnUpdateAirportText(it)) },
        currentText = airportText,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AirportSuggestions(
    suggestions: List<AirportSuggestion>,
    onAirportSelected: (OnAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (suggestions.isNotEmpty()) {
        LazyColumn(
            modifier = modifier
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .fillMaxHeight(0.5f)
                .wrapContentHeight(),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            stickyHeader {
                Surface(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .zIndex(1f),
                    color = MaterialTheme.colorScheme.surfaceContainer,
                ) {
                    Text(
                        text = stringResource(R.string.choose_airport),
                        modifier =
                            Modifier.padding(
                                bottom = 8.dp,
                                top = 4.dp,
                                start = 16.dp,
                            ),
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            itemsIndexed(
                items = suggestions,
                key = { _, suggestion -> suggestion.id },
            ) { index, suggestion ->
                Column {
                    if (index > 0) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 4.dp),
                        )
                    }
                    Row(
                        modifier =
                            Modifier
                                .padding(vertical = 16.dp)
                                .fillMaxWidth()
                                .clickable {
                                    onAirportSelected(
                                        OnAction.OnAirportSuggestionSelected(
                                            suggestion,
                                        ),
                                    )
                                },
                    ) {
                        Icon(painterResource(R.drawable.ic_airplane), contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("${suggestion.iata}, ${suggestion.name}")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SearchFlightsScreenPreview() {
    RandomTravellerTheme {
        Content(SearchFlightsScreenState(), {}, Modifier)
    }
}

@Preview(showBackground = true)
@Composable
private fun AirportSuggestionPreview() {
    RandomTravellerTheme {
        AirportSuggestions(
            listOf(
                AirportSuggestion("id1", "cityId1", "Athens", "ATH"),
                AirportSuggestion("id2", "cityId2", "Athens", "ATH2")
            ),
            {},
        )
    }
}
