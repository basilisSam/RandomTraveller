package com.example.randomtraveller.flights.flight_results.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.randomtraveller.flights.flight_results.domain.usecase.FindCheapestFlightsPerDestinationUseCase
import com.example.randomtraveller.flights.flight_results.ui.mapper.RoundTripFlightMapper
import com.example.randomtraveller.navigation.SearchFlights
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchFlightsViewModel @Inject constructor(
    private val cheapestFlightsPerDestinationUseCase: FindCheapestFlightsPerDestinationUseCase,
    private val roundTripFlightMapper: RoundTripFlightMapper,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val flightParams = savedStateHandle.toRoute<SearchFlights>()

    private val _screenState = MutableStateFlow(ScreenState())
    val screenState = _screenState.onStart {
        delay(5000)
        val roundTripFlights = cheapestFlightsPerDestinationUseCase(
            flightParams.cityId,
            flightParams.maxPrice,
            flightParams.outboundStartDate,
            flightParams.outboundEndDate,
            flightParams.inboundStartDate,
            flightParams.inboundEndDate
        )

        val mappedFlights = roundTripFlightMapper.mapDomainToUiModel(roundTripFlights)

        _screenState.update {
            it.copy(
                flights = mappedFlights,
                isLoading = false
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ScreenState())
}

data class ScreenState(
    val isLoading: Boolean = true,
    val flights: List<RoundTripFlight> = emptyList(),
)

data class RoundTripFlight(
    val id: String,
    val price: String,
    val priceAmount: Int?,
    val inbound: FlightDetails,
    val outbound: FlightDetails,
    val bookingUrl: String? = null
)

data class FlightDetails(
    val date: String,
    val flightDirection: FlightDirection,
    val sourceCityName: String,
    val destinationCityName: String,
    val departureTime: String,
    val departureAirport: String,
    val duration: String,
    val arrivalTime: String,
    val arrivalAirport: String,
    val carrierCode: String?
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
        ),
        isLoading = false
    )
}

fun createRoundTripFlight(id: String, price: String): RoundTripFlight {
    return RoundTripFlight(
        id = id,
        price = "$price$",
        priceAmount = 123,
        outbound = createFlightDetails(FlightDirection.OUTBOUND),
        inbound = createFlightDetails(FlightDirection.INBOUND)
    )
}

fun createFlightDetails(direction: FlightDirection): FlightDetails {
    return FlightDetails(
        date = "10 Apr",
        sourceCityName = "Berlin",
        destinationCityName = "Frankfurt",
        flightDirection = direction,
        departureTime = "08:00",
        departureAirport = "JFK",
        duration = "3h 30m",
        arrivalTime = "11:30",
        arrivalAirport = "LAX",
        carrierCode = null
    )
}