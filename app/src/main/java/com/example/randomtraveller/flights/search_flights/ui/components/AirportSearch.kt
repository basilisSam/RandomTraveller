package com.example.randomtraveller.flights.search_flights.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.randomtraveller.R
import com.example.randomtraveller.flights.search_flights.ui.AirportSuggestion
import com.example.randomtraveller.flights.search_flights.ui.OnAction
import com.example.randomtraveller.ui.theme.RandomTravellerTheme

@Composable
fun AirportSearchHint() {
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
fun AirportSuggestions(
    suggestions: List<AirportSuggestion>,
    onAirportSelected: (OnAction) -> Unit
) {
    if (suggestions.isEmpty()) {
        return
    }
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .fillMaxWidth(),
    ) {
        Column(
            Modifier
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .fillMaxSize(),
        ) {
            Text(
                text = stringResource(R.string.choose_airport),
                modifier = Modifier.padding(
                    bottom = 8.dp,
                    top = 4.dp,
                    start = 16.dp,
                ),
                fontWeight = FontWeight.Bold,
            )

            suggestions.forEachIndexed { index, suggestion ->
                if (index > 0) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 4.dp),
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable {
                            onAirportSelected(OnAction.OnAirportSuggestionSelected(suggestion))
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

@PreviewScreenSizes
@PreviewFontScale
@Composable
fun AirportSuggestionsPreview() {
    RandomTravellerTheme {
        val suggestions = listOf(
            AirportSuggestion("id1", "NYC", "John F. Kennedy International Airport", "JFK"),
            AirportSuggestion("id2", "NYC", "John F. Kennedy International Airport", "JFK"),
            AirportSuggestion("id3", "NYC", "John F. Kennedy International Airport", "JFK"),
        )
        AirportSuggestions(
            suggestions = suggestions,
            onAirportSelected = {},
        )
    }
}

@PreviewScreenSizes
@PreviewFontScale
@Composable
fun AirportSearchHintPreview() {
    RandomTravellerTheme {
        AirportSearchHint()
    }
}
