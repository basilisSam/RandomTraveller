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
                navController.navigate(SearchFlightCriteria) {
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
            SearchFlightsScreen(onBackClicked = { navController.popBackStack() })
        }
    }
}
