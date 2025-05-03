package com.example.randomtraveller.flights.ui.flight_results

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.randomtraveller.flights.ui.flight_results.components.FlightCard
import com.example.randomtraveller.ui.theme.RandomTravellerTheme

@Composable
fun FlightResultsScreen(
    onBackClicked: () -> Unit,
    viewModel: FlightResultsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    Content(onBackClicked, screenState)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Content(
    onBackClicked: () -> Unit,
    screenState: ScreenState
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Flights Results",
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    Icon(
                        Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "",
                        Modifier
                            .padding(start = 8.dp)
                            .clickable {
                                onBackClicked()
                            }
                    )
                }
            )
        }
    ) { innerPadding ->
        when (LocalConfiguration.current.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                LazyColumn(modifier = Modifier.padding(innerPadding)) {
                    items(
                        items = screenState.flights,
                        key = { roundTripFlight -> roundTripFlight.id }) {
                        FlightCard(roundTripFlight = it)
                    }
                }
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.padding(innerPadding)
                ) {
                    items(
                        count = screenState.flights.size,
                        key = { index -> screenState.flights[index].id }
                    ) { index ->
                        FlightCard(roundTripFlight = screenState.flights[index])
                    }
                }
            }
        }
    }
}

@PreviewScreenSizes
@Composable
private fun ContentPreview() {
    RandomTravellerTheme {
        Content({}, createSampleScreenState())
    }
}

@Preview
@Composable
private fun FlightCardPreview() {
    RandomTravellerTheme {
        FlightCard(createSampleScreenState().flights[0])
    }
}