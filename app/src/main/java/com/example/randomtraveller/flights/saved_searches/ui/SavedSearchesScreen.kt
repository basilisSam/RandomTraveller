package com.example.randomtraveller.flights.saved_searches.ui

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
import com.example.randomtraveller.flights.common.model.SavedSearchUi
import com.example.randomtraveller.flights.common.model.SearchFlightsNavigationParams
import com.example.randomtraveller.flights.common.ui.components.SavedSearchItem
import com.example.randomtraveller.ui.theme.RandomTravellerTheme
import kotlinx.coroutines.delay

@Composable
fun SavedSearchesScreen(
    modifier: Modifier = Modifier,
    onSavedSearchClicked: (SearchFlightsNavigationParams) -> Unit,
    viewModel: SavedSearchesViewModel = hiltViewModel()
) {
    val savedSearches by viewModel.savedSearches.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect {
            if (it == null) return@collect
            onSavedSearchClicked(it)
        }
    }

    Content(
        savedSearches,
        { viewModel.deleteSavedSearch(it) },
        { viewModel.onSavedSearchClicked(it) })
}

@Composable
fun Content(
    savedSearches: List<SavedSearchUi>,
    onDeleteSearch: (SavedSearchUi) -> Unit,
    onSavedSearchClicked: (Int) -> Unit,
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
                        SavedSearchItem(
                            savedSearch = savedSearch,
                            onClick = { onSavedSearchClicked(savedSearch.id) }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SavedSearchesScreenPreview() {
    RandomTravellerTheme {
        Content(
            listOf(

            ),
            {},
            {}
        )
    }
}