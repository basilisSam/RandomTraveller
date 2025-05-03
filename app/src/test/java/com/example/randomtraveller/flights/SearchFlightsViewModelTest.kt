package com.example.randomtraveller.flights

import android.icu.text.NumberFormat
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import app.cash.turbine.TurbineTestContext
import app.cash.turbine.test
import com.example.randomtraveller.GetAirportsQuery
import com.example.randomtraveller.flights.search_flights.data.AirportSearchRepository
import com.example.randomtraveller.flights.search_flights.ui.AirportSuggestion
import com.example.randomtraveller.flights.search_flights.ui.OnAction
import com.example.randomtraveller.flights.search_flights.ui.SearchFlightsScreenState
import com.example.randomtraveller.flights.search_flights.ui.SearchFlightsViewModel
import com.example.randomtraveller.flights.search_flights.ui.SelectedDateRange
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class SearchFlightsViewModelTest {
    @MockK
    val formatter = mockk<NumberFormat>()

    @MockK
    private val airportSearchRepository = mockk<AirportSearchRepository>()
    private lateinit var viewModel: SearchFlightsViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SearchFlightsViewModel(airportSearchRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial screen state should be correct`() =
        runTest {
            viewModel.screenState.test {
                testInitialState()
            }
        }

    // region updateAirportText

    @Test
    fun `should trigger airport suggestions search when text length is greater than 2`() =
        runTest {
            viewModel.screenState.test {
                coEvery { airportSearchRepository.getAirports(any()) } returns listOf(
                    GetAirportsQuery.Edge(
                        GetAirportsQuery.Node(
                            "typename1",
                            GetAirportsQuery.OnStation("id1", "name1", "code1")
                        )
                    ),
                    GetAirportsQuery.Edge(
                        GetAirportsQuery.Node(
                            "typename2",
                            GetAirportsQuery.OnStation("id2", "name2", "code2")
                        )
                    ),
                )

                testInitialState()

                viewModel.onAction(OnAction.OnUpdateAirportText("Athens"))

                val suggestionsLoadingState = awaitItem()
                assertThat(suggestionsLoadingState.areSuggestionsLoading).isTrue()

                val suggestionsLoadedState = awaitItem()
                assertThat(suggestionsLoadedState.areSuggestionsLoading).isFalse()

                val airportTextUpdatedState = awaitItem()
                assertThat(airportTextUpdatedState.airportText).isEqualTo(
                    TextFieldValue(
                        "Athens",
                        TextRange(6),
                    ),
                )

                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `should return empty airport suggestions when text is empty`() =
        runTest {
            viewModel.screenState.test {
                testInitialState()

                viewModel.onAction(OnAction.OnUpdateAirportText("a"))

                val oneLetterState = awaitItem()
                assertThat(oneLetterState.airportText).isEqualTo(TextFieldValue("a", TextRange(1)))

                viewModel.onAction(OnAction.OnUpdateAirportText(""))
                val emptyTextState = awaitItem()
                assertThat(emptyTextState.airportText).isEqualTo(TextFieldValue("", TextRange(0)))
                assertThat(emptyTextState.airportSuggestions).isEmpty()
                assertThat(emptyTextState.areSuggestionsLoading).isFalse()
            }
        }

    // endregion

    // region updateBudget

    @Test
    fun `should correctly update budget text`() =
        runTest {
            mockkNumberFormatClass()
            viewModel.screenState.test {
                testInitialState()

                every { formatter.format(1L) } returns "1"
                viewModel.onAction(OnAction.OnUpdateBudget("1"))

                val oneDigitState = awaitItem()
                assertThat(oneDigitState.budgetText).isEqualTo(TextFieldValue("1", TextRange(1)))

                viewModel.onAction(OnAction.OnUpdateBudget(""))

                val emptyBudgetState = awaitItem()
                assertThat(emptyBudgetState.budgetText).isEqualTo(TextFieldValue("", TextRange(0)))
            }
        }

    // endregion

    // region selectAirportSuggestion

    @Test
    fun `should correctly update airport text when suggestion is selected`() =
        runTest {
            viewModel.screenState.test {
                testInitialState()

                val airportSuggestion = AirportSuggestion("id1", "Athens", "ATH")
                viewModel.onAction(OnAction.OnAirportSuggestionSelected(airportSuggestion))

                val expectedAirportText = "(${airportSuggestion.iata}) ${airportSuggestion.name}"
                val airportTextUpdatedState = awaitItem()
                assertThat(airportTextUpdatedState.airportText).isEqualTo(
                    TextFieldValue(
                        expectedAirportText,
                        TextRange(expectedAirportText.length),
                    ),
                )
                assertThat(airportTextUpdatedState.airportSuggestions).isEmpty()
                assertThat(airportTextUpdatedState.areSuggestionsLoading).isFalse()
            }
        }

    // endregion

    // region showCalendarPicker

    @Test
    fun `should correctly set showCalendarPicker to true`() =
        runTest {
            viewModel.screenState.test {
                testInitialState()

                viewModel.onAction(OnAction.OnShowCalendarPicker)

                val calendarPickerShownState = awaitItem()
                assertThat(calendarPickerShownState.shouldShowCalendarPicker).isTrue()
            }
        }

    // endregion

    // region dismissDatePicker

    @Test
    fun `should correctly set showCalendarPicker to false`() =
        runTest {
            viewModel.screenState.test {
                testInitialState()

                viewModel.onAction(OnAction.OnShowCalendarPicker)

                val calendarPickerShownState = awaitItem()
                assertThat(calendarPickerShownState.shouldShowCalendarPicker).isTrue()

                viewModel.onAction(OnAction.OnDismissDatePicker)

                val calendarPickerNotShownState = awaitItem()
                assertThat(calendarPickerNotShownState.shouldShowCalendarPicker).isFalse()
            }
        }

    // endregion

    // region selectDateRange

    @Test
    fun `should correctly update selected date range`() =
        runTest {
            viewModel.screenState.test {
                testInitialState()

                val localDateNow = LocalDate.now()
                val localDateNowInMillis =
                    localDateNow.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
                val localDateTomorrow = localDateNow.plusDays(1)
                val localDateTomorrowInMillis =
                    localDateTomorrow.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
                val formattedDateText =
                    "${localDateNow.dayOfMonth}/${localDateNow.monthValue}/${localDateNow.year} - " +
                            "${localDateTomorrow.dayOfMonth}/${localDateTomorrow.monthValue}/${localDateTomorrow.year}"

                viewModel.onAction(
                    OnAction.OnDateRangeSelected(
                        localDateNowInMillis,
                        localDateTomorrowInMillis,
                    ),
                )

                val selectedDateRangeState = awaitItem()
                assertThat(selectedDateRangeState.selectedDateRange).isEqualTo(
                    SelectedDateRange(
                        startDateText = formattedDateText,
                        startLocalDate = localDateNow,
                        startDateInMillis = localDateNowInMillis,
                        endLocalDate = localDateTomorrow,
                        endDateInMillis = localDateTomorrowInMillis,
                    ),
                )
            }
        }

    @Test
    fun `should not update selected date range when start date is null`() =
        runTest {
            viewModel.screenState.test {
                testInitialState()

                viewModel.onAction(OnAction.OnDateRangeSelected(null, 1L))

                expectNoEvents()
            }
        }

    @Test
    fun `should not update selected date range when end date is null`() =
        runTest {
            viewModel.screenState.test {
                testInitialState()

                viewModel.onAction(OnAction.OnDateRangeSelected(1L, null))

                expectNoEvents()
            }
        }

    // endregion

    private suspend fun TurbineTestContext<SearchFlightsScreenState>.testInitialState() {
        val initialState = awaitItem()
        assertThat(initialState).isEqualTo(SearchFlightsScreenState())
    }

    private fun mockkNumberFormatClass() {
        mockkStatic(NumberFormat::class)
        every { NumberFormat.getInstance(Locale.getDefault()) } returns formatter
    }
}
