package com.example.randomtraveller.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.randomtraveller.core.database.SavedSearch
import com.example.randomtraveller.flights.flight_results.ui.SearchFlightsScreen
import com.example.randomtraveller.flights.saved_searches.SavedSearchesScreen
import com.example.randomtraveller.flights.search_criteria.ui.FlightSearchCriteriaScreen
import com.example.randomtraveller.login.LoginScreen
import com.example.randomtraveller.splash.SplashScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = SplashScreen,
        modifier = modifier,
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
    ) {
        composable<Login> {
            LoginScreen({ navController.navigate(SearchFlightCriteria) })
        }
        composable<SplashScreen>(
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
        ) {
            SplashScreen(
                modifier = modifier,
                onAuthResult = { isLoggedIn ->
                    val destination = if (isLoggedIn) SearchFlightCriteria else Login
                    navController.navigate(destination) {
                        popUpTo<SplashScreen> { inclusive = true }
                        launchSingleTop = true
                    }
                })
        }
        composable<SearchFlightCriteria> {
            FlightSearchCriteriaScreen(
                modifier = modifier,
                onSearchFlightsClicked = {
                    navController.navigate(
                        SearchFlights(
                            it.cityId,
                            it.maxPrice,
                            it.outboundStartDate,
                            it.outboundEndDate,
                            it.inboundStartDate,
                            it.inboundEndDate
                        )
                    )
                }
            )
        }
        composable<SearchFlights> {
            SearchFlightsScreen()
        }

        composable<SavedFlightSearches> {
            SavedSearchesScreen(onSavedSearchClicked = {
                navController.navigate(
                    SearchFlights(
                        cityId = it.cityId,
                        maxPrice = it.maxPrice,
                        outboundStartDate = it.outboundStartDate,
                        outboundEndDate = it.outboundEndDate,
                        inboundStartDate = it.inboundStartDate,
                        inboundEndDate = it.inboundEndDate
                    )
                )
            })
        }
    }
}
