package com.example.randomtraveller.flights.flight_results.ui.mapper

import com.example.randomtraveller.flights.flight_results.domain.model.FlightLegDetails
import com.example.randomtraveller.flights.flight_results.domain.model.RoundTripFlightDetails
import com.example.randomtraveller.flights.flight_results.ui.FlightDetails
import com.example.randomtraveller.flights.flight_results.ui.FlightDirection
import com.example.randomtraveller.flights.flight_results.ui.RoundTripFlight
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class RoundTripFlightMapper @Inject constructor() {

    fun mapDomainToUiModel(domainOffers: List<RoundTripFlightDetails>): List<RoundTripFlight> {
        val uiFlights = domainOffers.mapNotNull { mapSingleOfferToUi(it) }
        return uiFlights.sortedBy { it.priceAmount ?: Int.MAX_VALUE }
    }

    private fun mapSingleOfferToUi(offer: RoundTripFlightDetails): RoundTripFlight? {
        val outboundUiDetails = mapDomainLegToUiDetails(offer.outboundLeg, FlightDirection.OUTBOUND)
        val inboundUiDetails = mapDomainLegToUiDetails(offer.inboundLeg, FlightDirection.INBOUND)

        if (outboundUiDetails == null || inboundUiDetails == null) {
            println("Warning: Could not map domain offer fully to UI. ID: ${offer.id}")
            return null
        }

        val priceString = formatPrice(offer.priceAmount, offer.currencyCode)
        val priceCents = getPriceAsIntegerCents(offer.priceAmount)
        val fullBookingUrl = offer.bookingUrlPath?.let { "https://kiwi.com/$it" } ?: ""

        return RoundTripFlight(
            id = offer.id,
            price = priceString,
            priceAmount = priceCents, // Store numeric price for sorting
            outbound = outboundUiDetails,
            inbound = inboundUiDetails,
            bookingUrl = fullBookingUrl
        )
    }

    private fun mapDomainLegToUiDetails(
        leg: FlightLegDetails?,
        direction: FlightDirection
    ): FlightDetails? {
        if (leg == null) return null

        val formattedDuration = formatDuration(leg.durationSeconds)

        return FlightDetails(
            date = formatDate(leg.departureTimeLocal),
            flightDirection = direction,
            sourceCityName = leg.originCityName ?: "N/A",
            destinationCityName = leg.finalDestinationCityName ?: "N/A", // Use final dest here
            departureTime = formatTime(leg.departureTimeLocal),
            departureAirport = leg.originAirportCode ?: "N/A",
            duration = formattedDuration,
            arrivalTime = formatTime(leg.arrivalTimeLocal),
            arrivalAirport = leg.destinationAirportCode ?: "N/A",
            carrierCode = leg.carrierCode
        )
    }

    // --- Helper Formatting Functions (likely in shared core/utils or ui/utils) ---

    private fun formatDate(dateTime: LocalDateTime?): String {
        return dateTime?.format(DateTimeFormatter.ofPattern("MMM d, uuuu", Locale.ENGLISH)) ?: "N/A"
    }

    private fun formatTime(dateTime: LocalDateTime?): String {
        return dateTime?.format(DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)) ?: "N/A"
    }

    private fun formatDuration(durationInSeconds: Int?): String {
        if (durationInSeconds == null || durationInSeconds < 0) return "N/A"
        if (durationInSeconds == 0) return "0m"
        val totalSeconds = durationInSeconds.toLong()
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val hPart = if (hours > 0) "${hours}h" else ""
        val mPart = if (minutes > 0) "${minutes}m" else ""
        return when {
            hPart.isNotEmpty() && mPart.isNotEmpty() -> "$hPart$mPart"
            hPart.isNotEmpty() -> hPart
            mPart.isNotEmpty() -> mPart
            totalSeconds > 0 -> "1m" // Show <1 min as 1m
            else -> "0m"
        }
    }

    private fun formatPrice(amount: BigDecimal?, currencyCode: String?): String {
        if (amount == null || currencyCode == null) return "N/A"
        return try {
            // Format BigDecimal to integer string for display if desired, or use proper currency formatting
            val amountInt = amount.setScale(0, RoundingMode.HALF_UP).toInt()
            "$currencyCode $amountInt"
        } catch (e: Exception) {
            println("Error formatting price: $amount $currencyCode - ${e.message}")
            "N/A"
        }
    }

    private fun getPriceAsIntegerCents(amount: BigDecimal?): Int? {
        if (amount == null) return null
        return try {
            amount.multiply(BigDecimal(100)).setScale(0, RoundingMode.HALF_UP).toInt()
        } catch (e: Exception) {
            println("Error converting price BigDecimal $amount to integer cents: ${e.message}")
            null
        }
    }
}