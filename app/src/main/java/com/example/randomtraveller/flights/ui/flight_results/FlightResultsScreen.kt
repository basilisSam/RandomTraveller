package com.example.randomtraveller.flights.ui.flight_results

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.REPEAT_MODE_ONE
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.randomtraveller.R
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
        if (screenState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                LocalVideoPlayer(Modifier
                    .height(200.dp)
                    .width(150.dp))
            }
        } else {
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
}

@Composable
fun LocalVideoPlayer(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uri = remember {
        "android.resource://${context.packageName}/${R.raw.random_traveller_loading}".toUri()
    }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            playWhenReady = true
            repeatMode = REPEAT_MODE_ONE
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
            }
        },
        modifier = modifier
    )
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