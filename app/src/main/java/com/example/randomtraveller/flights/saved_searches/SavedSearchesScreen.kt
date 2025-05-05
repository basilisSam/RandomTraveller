package com.example.randomtraveller.flights.saved_searches

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.randomtraveller.core.database.SavedSearch
import com.example.randomtraveller.core.utils.toPrettyLocalDate
import com.example.randomtraveller.ui.theme.RandomTravellerTheme

@Composable
fun SavedSearchesScreen(
    modifier: Modifier = Modifier,
    onSavedSearchClicked: (SavedSearch) -> Unit,
    viewModel: SavedSearchesViewModel = hiltViewModel()
) {
    val savedSearches by viewModel.savedSearches.collectAsStateWithLifecycle()

    Content(savedSearches, onSavedSearchClicked)
}

@Composable
fun Content(
    savedSearches: List<SavedSearch>,
    onSavedSearchClicked: (SavedSearch) -> Unit,
    modifier: Modifier = Modifier
) {
    if (savedSearches.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "There are no saved searches",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier.height(16.dp))
            savedSearches.forEach { savedSearch ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable { onSavedSearchClicked(savedSearch) }
                ) {
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "(${savedSearch.iata}) ${savedSearch.airportName}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${savedSearch.outboundStartDate.toPrettyLocalDate()} - ${savedSearch.inboundStartDate.toPrettyLocalDate()}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Max price: ${savedSearch.maxPrice}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            Spacer(modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
private fun SavedSearchesScreenPreview() {
    RandomTravellerTheme {
        Content(
            listOf(
                SavedSearch(
                    id = 0,
                    cityId = "City:thessaloniki_gt",
                    airportName = "Thessaloniki airport",
                    iata = "SKG",
                    maxPrice = 300,
                    outboundStartDate = "2024-07-27T00:00:00Z",
                    outboundEndDate = "2024-07-27T00:00:00Z",
                    inboundStartDate = "2024-07-27T00:00:00Z",
                    inboundEndDate = "2024-07-27T00:00:00Z"
                )
            ),
            {}
        )
    }
}