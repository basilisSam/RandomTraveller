package com.example.randomtraveller.flights.flight_results.ui.mapper

import android.util.Log
import com.example.randomtraveller.flights.flight_results.ui.FlightDetails
import com.example.randomtraveller.flights.flight_results.ui.FlightDirection
import com.example.randomtraveller.flights.flight_results.ui.RoundTripFlight
import com.example.randomtraveller.umbrella.SearchFlightsQuery
import com.example.randomtraveller.umbrella.fragment.TripInfo
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun SearchFlightsQuery.Data.toUiModel(): List<RoundTripFlight> {
    // 1. Get the raw list of itineraries, filtering out nulls within the list
    val rawItineraries = this.returnItineraries?.onItineraries?.itineraries
        ?.filterNotNull() // Ensure we work with non-null Itinerary objects
        ?: return emptyList()
    Log.d(
        "SearchFlightGroup",
        rawItineraries.size.toString()
    )
    rawItineraries.forEach { iterinary ->
        Log.d(
            "SearchFlightGroup",
            iterinary.tripInfo.onItineraryReturn?.outbound?.sectorSegments?.first()?.segment?.destination?.station?.city?.name
                ?: "null"
        )
    }

    if (rawItineraries.isEmpty()) {
        return emptyList()
    }

    // 2. Group raw itineraries by the FINAL destination city name of the outbound leg
    val rawGroupedByDestCity = rawItineraries.groupBy { itinerary ->
        getFinalDestinationCityName(itinerary) // Use helper to get key safely
    }

    rawGroupedByDestCity.forEach {
        Log.d("SearchFlightGroup", it.key)
    }

    // 3. For each group, find the raw itinerary with the minimum price
    val cheapestRawItineraries =
        rawGroupedByDestCity.mapNotNull { (cityName, rawItinerariesToSameDest) ->
            // Skip if destination couldn't be determined
            if (cityName == "UNKNOWN_DESTINATION") {
                null
            } else {
                // Find the itinerary with the minimum price (using parsed Int cents)
                rawItinerariesToSameDest.minByOrNull { itinerary ->
                    getPriceAsInteger(itinerary.price) ?: Int.MAX_VALUE // Compare numeric price
                }
            }
        }

    // 4. Map ONLY the selected cheapest raw itineraries to the UI model
    val finalUiFlights = cheapestRawItineraries.mapNotNull { cheapItinerary ->
        // Use the existing detailed mapper, passing the required parts
        // Ensure tripInfo is not null before calling the extension function
        cheapItinerary.tripInfo.toRoundTripFlight(
            cheapItinerary.id,
            cheapItinerary.price,
            cheapItinerary.bookingOptions
        )
    }

    // 5. Optionally sort the final list of unique destinations by price
    return finalUiFlights.sortedBy { it.priceAmount ?: Int.MAX_VALUE }
}

/**
 * Maps the TripInfo fragment data (along with id/price from the main itinerary)
 * to the RoundTripFlight UI model.
 */
fun TripInfo.toRoundTripFlight(
    itineraryId: String?,
    priceData: SearchFlightsQuery.Price?,
    bookingOptions: SearchFlightsQuery.BookingOptions?
): RoundTripFlight? {
    // We expect this fragment to be on an ItineraryReturn based on the query structure
    val returnInfo = this.onItineraryReturn ?: return null

    // Safely map outbound and inbound sectors
    val outboundDetails = returnInfo.outbound?.toFlightDetails(FlightDirection.OUTBOUND)
    val inboundDetails = returnInfo.inbound?.toFlightDetails(FlightDirection.INBOUND)

    if (itineraryId == null || outboundDetails == null || inboundDetails == null) {
        println("Warning: Could not map itinerary fully. ID: $itineraryId, Outbound: $outboundDetails, Inbound: $inboundDetails")
        return null
    }

    val numericPrice = getPriceAsInteger(priceData)
    val bookingUrl = bookingOptions?.edges?.firstOrNull()?.node?.bookingUrl

    return RoundTripFlight(
        id = itineraryId,
        price = formatPrice(priceData),
        priceAmount = numericPrice,
        outbound = outboundDetails,
        inbound = inboundDetails,
        bookingUrl = "${"https://kiwi.com/"}$bookingUrl"
    )
}

/**
 * Maps an Outbound sector to the FlightDetails UI model.
 */
fun TripInfo.Outbound.toFlightDetails(direction: FlightDirection): FlightDetails? {
    // A sector is made of segments. We need the start of the first and end of the last.
    val firstSegment = this.sectorSegments?.firstOrNull()?.segment
    val lastSegment = this.sectorSegments?.lastOrNull()?.segment

    if (firstSegment == null || lastSegment == null) {
        println("Warning: Sector missing first or last segment for ${direction.value}")
        return null // Cannot determine full flight details
    }

    val departureDateTime = parseGraphQLDateTime(firstSegment.source?.localTime as String)
    val arrivalDateTime = parseGraphQLDateTime(lastSegment.destination?.localTime as String)

    if (departureDateTime == null || arrivalDateTime == null) {
        println("Warning: Could not parse departure/arrival time for ${direction.value}")
        return null
    }

    val formattedDuration = formatDuration(this.duration)
    val departureAirportStr = firstSegment.source.station?.code ?: "N/A"
    val arrivalAirportStr = lastSegment.destination.station?.code ?: "N/A"


    return FlightDetails(
        date = formatDate(departureDateTime),
        flightDirection = direction,
        sourceCityName = firstSegment.source.station?.city?.name ?: "N/A",
        destinationCityName = lastSegment.destination.station?.city?.name ?: "N/A",
        departureTime = formatTime(departureDateTime),
        departureAirport = departureAirportStr,
        duration = formattedDuration,
        arrivalTime = formatTime(arrivalDateTime),
        arrivalAirport = arrivalAirportStr,
        carrierCode = firstSegment.carrier?.code
    )
}

/**
 * Maps an Inbound sector to the FlightDetails UI model.
 * (Implementation is identical to Outbound, could be refactored)
 */
fun TripInfo.Inbound.toFlightDetails(direction: FlightDirection): FlightDetails? {
    val firstSegment = this.sectorSegments?.firstOrNull()?.segment
    val lastSegment = this.sectorSegments?.lastOrNull()?.segment

    if (firstSegment == null || lastSegment == null) {
        println("Warning: Sector missing first or last segment for ${direction.value}")
        return null
    }

    val departureDateTime = parseGraphQLDateTime(firstSegment.source?.localTime as String)
    val arrivalDateTime = parseGraphQLDateTime(lastSegment.destination?.localTime as String)

    if (departureDateTime == null || arrivalDateTime == null) {
        println("Warning: Could not parse departure/arrival time for ${direction.value}")
        return null
    }

    return FlightDetails(
        date = formatDate(departureDateTime),
        flightDirection = direction,
        sourceCityName = firstSegment.source.station?.city?.name ?: "N/A",
        destinationCityName = lastSegment.destination.station?.city?.name ?: "N/A",
        departureTime = formatTime(departureDateTime),
        departureAirport = firstSegment.source.station?.code ?: "N/A",
        duration = formatDuration(this.duration),
        arrivalTime = formatTime(arrivalDateTime),
        arrivalAirport = lastSegment.destination.station?.code ?: "N/A",
        carrierCode = firstSegment.carrier?.code
    )
}

/**
 * Parses the DateTime string from GraphQL (assuming ISO 8601 format)
 * Requires a DateTime library (e.g., kotlinx-datetime or Java 8 time).
 * !! MISSING INFORMATION: Actual parsing logic needs implementation. !!
 */
fun parseGraphQLDateTime(dateTimeString: String?): LocalDateTime? {
    return try {
        dateTimeString?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) }
    } catch (e: Exception) {
        println("Error parsing date-time: $dateTimeString - ${e.message}")
        null
    }
}

/**
 * Formats the date part of a LocalDateTime.
 */
fun formatDate(dateTime: LocalDateTime?): String {
    // Example: "May 6, 2025"
    return dateTime?.format(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)) ?: "N/A"
}

/**
 * Formats the time part of a LocalDateTime.
 */
fun formatTime(dateTime: LocalDateTime?): String {
    // Example: "10:30"
    return dateTime?.format(DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)) ?: "N/A"
}

/**
 * Formats duration from seconds to a human-readable string (e.g., "3h 15m")
 * without using java.time.Duration.
 */
fun formatDuration(durationInSeconds: Int?): String {
    // 1. Handle null or invalid input
    if (durationInSeconds == null || durationInSeconds < 0) {
        return "N/A"
    }

    // 2. Handle zero duration explicitly
    if (durationInSeconds == 0) {
        return "0m"
    }

    val totalSeconds = durationInSeconds.toLong()

    val hours = totalSeconds / 3600
    val remainingSecondsAfterHours =
        totalSeconds % 3600 // Calculate seconds remaining after full hours
    val minutes = remainingSecondsAfterHours / 60 // Calculate full minutes from the remainder

    // 4. Build the formatted string
    val hoursPart = if (hours > 0) "${hours}h" else ""
    val minutesPart = if (minutes > 0) "${minutes}m" else ""

    return when {
        // Both hours and minutes are significant
        hours > 0 && minutes > 0 -> "$hoursPart$minutesPart" // e.g., "3h15m"
        // Only hours are significant
        hours > 0 -> hoursPart                              // e.g., "3h"
        // Only minutes are significant
        minutes > 0 -> minutesPart                            // e.g., "15m"
        // Duration was non-zero but less than 60 seconds
        totalSeconds > 0 -> "1m" // Display durations < 1 min as "1m"
        // Fallback for zero, though already handled above
        else -> "0m"
    }
}

// Helper to get price as Int cents from the raw Price type
fun getPriceAsInteger(priceData: SearchFlightsQuery.Price?): Int? {
    val amountStr = priceData?.amount
    if (amountStr.isNullOrBlank()) return null
    return try {
        BigDecimal(amountStr)
            .setScale(2, RoundingMode.HALF_EVEN) // Ensure 2 decimal places
            .multiply(BigDecimal(100))
            .toInt()
    } catch (e: Exception) {
        println("Error parsing price amount '$amountStr' to integer cents: ${e.message}")
        null
    }
}

// Helper to safely get the *final* destination city name from a raw itinerary's outbound leg
fun getFinalDestinationCityName(itinerary: SearchFlightsQuery.Itinerary?): String {
    // Navigate through the structure to the last segment's destination city
    return itinerary?.tripInfo?.onItineraryReturn
        ?.outbound?.sectorSegments?.lastOrNull() // Get the last segment
        ?.segment?.destination?.station?.city?.name
        ?: "UNKNOWN_DESTINATION" // Fallback if path fails
}

/**
 * Formats the price amount and currency code.
 */
fun formatPrice(priceData: SearchFlightsQuery.Price?): String {
    val amountStr = priceData?.amount
    val currencyCode = priceData?.currency?.code
    if (amountStr == null || currencyCode == null) return "N/A"

    return try {
        val amount = amountStr.toDouble().toInt()
        "$currencyCode $amount"
    } catch (e: NumberFormatException) {
        println("Error formatting price: $amountStr $currencyCode - ${e.message}")
        "N/A"
    }
}