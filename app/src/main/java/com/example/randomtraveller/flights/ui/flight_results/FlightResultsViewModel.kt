package com.example.randomtraveller.flights.ui.flight_results

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.example.randomtraveller.navigation.FlightResults
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FlightResultsViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _screenState = MutableStateFlow(createSampleScreenState())
    val screenState = _screenState.asStateFlow()

    private val flightParams = savedStateHandle.toRoute<FlightResults>()
}

data class ScreenState(
    val flights: List<RoundTripFlight> = emptyList(),
)

data class RoundTripFlight(
    val id: String,
    val price: String,
    val inbound: FlightDetails,
    val outbound: FlightDetails,
)

data class FlightDetails(
    val date: String,
    val flightDirection: FlightDirection,
    val departureTime: String,
    val departureAirport: String,
    val duration: String,
    val arrivalTime: String,
    val arrivalAirport: String,
)

enum class FlightDirection(val value: String) {
    INBOUND("inbound"),
    OUTBOUND("outbound")
}

fun createSampleScreenState(): ScreenState {
    return ScreenState(
        flights = listOf(
            createRoundTripFlight("1", "250"),
            createRoundTripFlight("2", "300"),
            createRoundTripFlight("3", "200"),
            createRoundTripFlight("4", "350"),
            createRoundTripFlight("5", "280")
        )
    )
}

fun createRoundTripFlight(id: String, price: String): RoundTripFlight {
    return RoundTripFlight(
        id = id,
        price = "$price$",
        outbound = createFlightDetails(FlightDirection.OUTBOUND),
        inbound = createFlightDetails(FlightDirection.INBOUND)
    )
}

fun createFlightDetails(direction: FlightDirection): FlightDetails {
    return FlightDetails(
        date = "10 Apr",
        flightDirection = direction,
        departureTime = "08:00",
        departureAirport = "JFK",
        duration = "3h 30m",
        arrivalTime = "11:30",
        arrivalAirport = "LAX"
    )
}