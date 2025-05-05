package com.example.randomtraveller.flights.flight_results.domain.usecase

import com.example.randomtraveller.flights.flight_results.domain.repository.FlightsRepository
import com.example.randomtraveller.flights.flight_results.domain.model.RoundTripFlightDetails
import java.math.BigDecimal
import javax.inject.Inject

class FindCheapestFlightsPerDestinationUseCase @Inject constructor(
    private val flightsRepository: FlightsRepository
) {

    suspend operator fun invoke(
        sourceCityId: String,
        maxPrice: Int,
        outboundStartDate: String,
        outboundEndDate: String,
        inboundStartDate: String,
        inboundEndDate: String
    ): List<RoundTripFlightDetails> {
        val roundTripFlights = flightsRepository.searchFlights(
            sourceCityId,
            maxPrice,
            outboundStartDate,
            outboundEndDate,
            inboundStartDate,
            inboundEndDate
        )
        if (roundTripFlights.isEmpty()) {
            return emptyList()
        }

        val groupedByDestCity = roundTripFlights.groupBy { offer ->
            offer.outboundLeg?.finalDestinationCityName ?: "UNKNOWN_DESTINATION"
        }

        val cheapestOffers = groupedByDestCity.mapNotNull { (cityName, offersToSameDest) ->
            if (cityName == "UNKNOWN_DESTINATION") {
                null
            } else {
                offersToSameDest.minByOrNull { offer ->
                    offer.priceAmount
                        ?: BigDecimal(Double.MAX_VALUE)
                }
            }
        }

        return cheapestOffers
    }
}