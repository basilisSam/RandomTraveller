package com.example.randomtraveller.flights.ui.flight_results.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.randomtraveller.core.ui.DashedHorizontalDivider
import com.example.randomtraveller.flights.ui.flight_results.FlightDetails
import com.example.randomtraveller.flights.ui.flight_results.FlightDirection
import com.example.randomtraveller.flights.ui.flight_results.RoundTripFlight
import com.example.randomtraveller.ui.theme.RandomTravellerTheme

@Composable
fun FlightCard(
    roundTripFlight: RoundTripFlight,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            FlightCardSector(roundTripFlight.outbound)
            DashedHorizontalDivider()
            FlightCardSector(roundTripFlight.inbound)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(roundTripFlight.price, fontWeight = FontWeight.Bold)
            }
        }
    }

}

@Composable
fun FlightCardSector(
    flightDetails: FlightDetails,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Text(
            text = "${flightDetails.date} - ${
                flightDetails.flightDirection.value.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase() else it.toString()
                }
            }",
            fontSize = 11.sp, color = Color.DarkGray
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = flightDetails.departureTime,
                fontWeight = FontWeight.Bold
            )

            HorizontalDivider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceContainer
            ) {
                Text(
                    text = flightDetails.duration,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            if (flightDetails.carrierCode != null) {
                AsyncImage(
                    model = "https://images.kiwi.com/airlines/64/${flightDetails.carrierCode}.png",
                    contentDescription = null,
                    modifier = modifier
                        .size(24.dp)
                        .padding(start = 4.dp)
                )
            }

            HorizontalDivider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )

            Text(
                text = flightDetails.arrivalTime,
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = flightDetails.departureAirport,
                fontSize = 14.sp
            )
            Text(
                text = "Direct",
                fontSize = 11.sp
            )
            Text(
                text = flightDetails.arrivalAirport,
                fontSize = 14.sp
            )
        }
    }
}

@PreviewScreenSizes()
@Composable
private fun FlightCardPreview() {
    RandomTravellerTheme {
        FlightCard(RoundTripFlight("id", "123$", inboundFlight, outboundFlight))
    }
}

private val outboundFlight = FlightDetails(
    date = "10 Apr",
    flightDirection = FlightDirection.OUTBOUND,
    departureTime = "06:25",
    departureAirport = "SKG",
    duration = "1h 00m",
    arrivalTime = "07:25",
    arrivalAirport = "ATH",
    ""
)

private val inboundFlight = FlightDetails(
    date = "15 Apr",
    flightDirection = FlightDirection.INBOUND,
    departureTime = "06:25",
    departureAirport = "ATH",
    duration = "1h 00m",
    arrivalTime = "07:25",
    arrivalAirport = "SKG",
    ""
)