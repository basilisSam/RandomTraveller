package com.example.randomtraveller.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.randomtraveller.flights.ui.flight_results.FlightResults
import com.example.randomtraveller.flights.ui.search_flights.SearchFlightScreen
import com.example.randomtraveller.login.LoginScreen
import com.example.randomtraveller.splash.SplashScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavHost(
    navController: NavHostController,
    modifier: Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = SplashScreen,
        modifier = modifier,
    ) {
        FirebaseAuth.getInstance().addAuthStateListener {
            if (it.currentUser == null) {
                navController.navigate(Login) {
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            } else {
                navController.navigate(SearchFlight) {
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            }
        }

        composable<Login> {
            LoginScreen(modifier)
        }
        composable<SplashScreen> {
            SplashScreen(modifier)
        }
        composable<SearchFlight> {
            SearchFlightScreen(
                modifier = modifier,
                onSearchFlightsClicked = {
                    navController.navigate(
                        FlightResults(
                            maxBudget = it.maxBudget,
                            departureAirportIata = it.departureAirportIata,
                            inboundDateMillis = it.inboundDateMillis,
                            outboundDateMillis = it.outboundDateMillis
                        )
                    )
                }
            )
        }
        composable<FlightResults> {
            FlightResults(onBackClicked = { navController.popBackStack() })
        }
    }
}
