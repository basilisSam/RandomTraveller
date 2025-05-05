package com.example.randomtraveller.flights.saved_searches

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.delay

@Composable
fun SavedSearchesScreen(
    modifier: Modifier = Modifier,
    onSavedSearchClicked: (SavedSearch) -> Unit,
    viewModel: SavedSearchesViewModel = hiltViewModel()
) {
    val savedSearches by viewModel.savedSearches.collectAsStateWithLifecycle()

    Content(savedSearches, { viewModel.deleteSavedSearch(it) }, onSavedSearchClicked)
}

@Composable
fun Content(
    savedSearches: List<SavedSearch>,
    onDeleteSearch: (SavedSearch) -> Unit,
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
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = savedSearches,
                key = { it.id }
            ) { savedSearch ->

                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = { dismissValue ->
                        dismissValue == SwipeToDismissBoxValue.EndToStart
                    },
                    positionalThreshold = { totalDistance -> totalDistance * 0.5f }
                )

                var showItem by remember { mutableStateOf(true) }
                LaunchedEffect(showItem) {
                    if (!showItem) {
                        // Add a small delay matching the animation to allow VM to process delete
                        // before the item fully disappears compositionally
                        delay(500L) // Adjust delay based on animation duration
                        onDeleteSearch(savedSearch)
                    }
                }
                
                LaunchedEffect(dismissState.currentValue) {
                    if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                        showItem = false
                    }
                }

                AnimatedVisibility(
                    visible = showItem,
                    exit = fadeOut(animationSpec = tween(500)) + shrinkVertically(
                        animationSpec = tween(
                            500
                        )
                    ),
                    modifier = Modifier.animateItem()
                ) {
                    SwipeToDismissBox(
                        state = dismissState,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .animateItem(),
                        enableDismissFromStartToEnd = false,
                        enableDismissFromEndToStart = true,
                        backgroundContent = {
                            val color = when (dismissState.dismissDirection) {
                                SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.errorContainer
                                else -> Color.Transparent
                            }
                            val alignment = Alignment.CenterEnd

                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(color, shape = RoundedCornerShape(16.dp))
                                    .padding(horizontal = 20.dp),
                                contentAlignment = alignment
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete Icon",
                                    tint = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    ) {
                        SavedSearchCard(
                            savedSearch = savedSearch,
                            onClick = { onSavedSearchClicked(savedSearch) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SavedSearchCard(
    savedSearch: SavedSearch,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
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
            {},
            {}
        )
    }
}