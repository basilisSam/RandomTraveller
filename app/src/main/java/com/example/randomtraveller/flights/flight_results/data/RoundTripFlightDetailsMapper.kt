package com.example.randomtraveller.flights.flight_results.data// Located in: flights/data/mapper/FlightOfferMapper.kt
import com.example.randomtraveller.flights.flight_results.domain.model.FlightLegDetails
import com.example.randomtraveller.flights.flight_results.domain.model.RoundTripFlightDetails
import com.example.randomtraveller.umbrella.SearchFlightsQuery
import com.example.randomtraveller.umbrella.fragment.TripInfo
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class RoundTripFlightDetailsMapper @Inject constructor() {

    fun mapDtoToDomain(dto: SearchFlightsQuery.Data?): List<RoundTripFlightDetails> {
        val rawItineraries = dto?.returnItineraries?.onItineraries?.itineraries
            ?.filterNotNull()
            ?: return emptyList()

        return rawItineraries.mapNotNull { mapSingleItineraryToDomain(it) }
    }

    private fun mapSingleItineraryToDomain(itineraryDto: SearchFlightsQuery.Itinerary): RoundTripFlightDetails? {
        val itineraryId = itineraryDto.id
        val returnInfo = itineraryDto.tripInfo.onItineraryReturn ?: return null

        val outboundDomainLeg = returnInfo.outbound?.toRoundTripFlight() ?: return null
        val inboundDomainLeg = returnInfo.inbound?.toRoundTripFlight() ?: return null

        val priceAmountDomain = parsePriceToBigDecimal(itineraryDto.price)
        val currencyCodeDomain = itineraryDto.price?.currency?.code

        val bookingUrlPathDomain =
            itineraryDto.bookingOptions?.edges?.firstOrNull()?.node?.bookingUrl

        return RoundTripFlightDetails(
            id = itineraryId,
            priceAmount = priceAmountDomain,
            currencyCode = currencyCodeDomain,
            bookingUrlPath = bookingUrlPathDomain,
            outboundLeg = outboundDomainLeg,
            inboundLeg = inboundDomainLeg
        )
    }

    fun TripInfo.Outbound.toRoundTripFlight(): FlightLegDetails? {
        val sectorSegments = this.sectorSegments?.filterNotNull()
        val duration = this.duration

        val firstSegment = sectorSegments?.firstOrNull()?.segment ?: return null
        val lastSegment = sectorSegments.lastOrNull()?.segment ?: return null

        val departureTime = parseGraphQLDateTime(firstSegment.source?.localTime as? String)
        val arrivalTime = parseGraphQLDateTime(lastSegment.destination?.localTime as? String)

        val originAirport = firstSegment.source?.station?.code
        val destAirport = lastSegment.destination?.station?.code
        val originCity = firstSegment.source?.station?.city?.name
        val destinationCity = lastSegment.destination?.station?.city?.name
        val carrier = firstSegment.carrier?.code

        return FlightLegDetails(
            departureTimeLocal = departureTime,
            arrivalTimeLocal = arrivalTime,
            durationSeconds = duration,
            originAirportCode = originAirport,
            destinationAirportCode = destAirport,
            originCityName = originCity,
            finalDestinationCityName = destinationCity,
            carrierCode = carrier
        )
    }

    fun TripInfo.Inbound.toRoundTripFlight(): FlightLegDetails? {
        val sectorSegments = this.sectorSegments?.filterNotNull()
        val duration = this.duration

        val firstSegment = sectorSegments?.firstOrNull()?.segment ?: return null
        val lastSegment = sectorSegments.lastOrNull()?.segment ?: return null

        val departureTime = parseGraphQLDateTime(firstSegment.source?.localTime as? String)
        val arrivalTime = parseGraphQLDateTime(lastSegment.destination?.localTime as? String)

        val originAirport = firstSegment.source?.station?.code
        val destAirport = lastSegment.destination?.station?.code
        val originCity = firstSegment.source?.station?.city?.name
        val destinationCity = lastSegment.destination?.station?.city?.name
        val carrier = firstSegment.carrier?.code

        return FlightLegDetails(
            departureTimeLocal = departureTime,
            arrivalTimeLocal = arrivalTime,
            durationSeconds = duration,
            originAirportCode = originAirport,
            destinationAirportCode = destAirport,
            originCityName = originCity,
            finalDestinationCityName = destinationCity,
            carrierCode = carrier
        )
    }

    private fun parseGraphQLDateTime(dateTimeString: String?): LocalDateTime? {
        if (dateTimeString == null) return null
        return try {
            LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        } catch (e: Exception) {
            println("DataMapper: Error parsing date-time '$dateTimeString': ${e.message}")
            null
        }
    }

    private fun parsePriceToBigDecimal(priceDto: SearchFlightsQuery.Price?): BigDecimal? {
        val amountStr = priceDto?.amount
        if (amountStr.isNullOrBlank()) return null
        return try {
            BigDecimal(amountStr)
        } catch (e: Exception) {
            println("DataMapper: Error parsing price amount '$amountStr' to BigDecimal: ${e.message}")
            null
        }
    }
}