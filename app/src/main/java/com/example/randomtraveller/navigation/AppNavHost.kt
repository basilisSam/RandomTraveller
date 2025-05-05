package com.example.randomtraveller.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.randomtraveller.flights.flight_results.ui.SearchFlightsScreen
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
    ) {
        composable<Login> {
            LoginScreen({ navController.navigate(SearchFlightCriteria) })
        }
        composable<SplashScreen> {
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
    }
}
