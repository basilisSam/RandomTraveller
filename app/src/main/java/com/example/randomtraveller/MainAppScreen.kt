package com.example.randomtraveller

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.randomtraveller.navigation.AppNavHost
import com.example.randomtraveller.navigation.Login
import com.example.randomtraveller.navigation.SavedFlightSearches
import com.example.randomtraveller.navigation.SearchFlightCriteria
import com.example.randomtraveller.navigation.SearchFlights
import com.example.randomtraveller.navigation.SplashScreen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

val darkCharcoal = Color(0xFF263238) // Dark background
val lightTextColor = Color.White.copy(alpha = 0.87f) // Primary text color on dark bg
val lightSecondaryTextColor = Color.White.copy(alpha = 0.6f) // Secondary text color
val selectedItemContainerColor =
    Color.White.copy(alpha = 0.1f) // Subtle selection highlight on dark bg
val selectedItemIconColor = Color.White // White icon when selected on dark bg
val selectedItemTextColor = Color.White // White text when selected on dark bg

@Composable
fun MainAppScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentNavBackStackEntry?.destination

    val showDrawer = currentDestination?.route in listOf(
        SearchFlightCriteria::class.qualifiedName,
        SavedFlightSearches::class.qualifiedName
    )

    val isSearchFlightsScreen = remember(currentDestination) {
        currentDestination?.route?.startsWith(
            SearchFlights::class.qualifiedName ?: "SearchFlights"
        ) == true
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = showDrawer,
        drawerContent = {
            AppDrawerContent(
                currentRoute = currentDestination?.route,
                onNavigate = { destination ->
                    navController.navigate(destination) {
                        launchSingleTop = true
                        restoreState = true
                    }
                    scope.launch { drawerState.close() }
                },
                onCloseDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                AppTopBar(
                    currentDestination = currentDestination,
                    showDrawer = showDrawer,
                    showBackArrow = isSearchFlightsScreen,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onBackClick = { navController.popBackStack() }
                )
            }
        ) { innerPadding ->
            AppNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    currentDestination: androidx.navigation.NavDestination?,
    showDrawer: Boolean,
    showBackArrow: Boolean,
    onMenuClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val route = currentDestination?.route
    if (route == SplashScreen::class.qualifiedName || route == Login::class.qualifiedName) {
        return
    }

    CenterAlignedTopAppBar(
        title = {
            Text(text = route?.let { getTitleForRoute(it) } ?: "App", fontWeight = FontWeight.Bold)
        },
        navigationIcon = {
            if (showDrawer) {
                IconButton(onClick = onMenuClick) {
                    Icon(Icons.Filled.Menu, contentDescription = "Open Drawer")
                }
            } else if (showBackArrow) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        }
    )
}

fun getTitleForRoute(route: String): String {
    return when {
        route == SearchFlightCriteria::class.qualifiedName -> "Flight Search"
        route == SavedFlightSearches::class.qualifiedName -> "Saved Searches"
        route.startsWith(SearchFlights::class.qualifiedName ?: "SearchFlights") -> "Search Results"
        // Add other titles if needed
        else -> ""
    }
}

@Composable
fun AppDrawerContent(
    currentRoute: String?,
    onNavigate: (Any) -> Unit,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet(drawerContainerColor = darkCharcoal) {
        Column(modifier = Modifier.fillMaxHeight()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                contentAlignment = Alignment.Center,

                ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_random_traveller_logo),
                    contentDescription = "Drawer Header Background",
                    contentScale = ContentScale.Crop
                )
            }
            NavigationDrawerItem(
                icon = { Icon(Icons.Filled.Search, contentDescription = null) },
                label = { Text("Flight Search") },
                selected = currentRoute == SearchFlightCriteria::class.qualifiedName,
                onClick = { onNavigate(SearchFlightCriteria) },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = selectedItemContainerColor,
                    selectedIconColor = selectedItemIconColor,
                    selectedTextColor = selectedItemTextColor,
                    unselectedContainerColor = Color.Transparent,
                    unselectedIconColor = lightSecondaryTextColor,
                    unselectedTextColor = lightTextColor
                )
            )
            NavigationDrawerItem(
                icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                label = { Text("Saved Searches") },
                selected = currentRoute == SavedFlightSearches::class.qualifiedName,
                onClick = { onNavigate(SavedFlightSearches) },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = selectedItemContainerColor,
                    selectedIconColor = selectedItemIconColor,
                    selectedTextColor = selectedItemTextColor,
                    unselectedContainerColor = Color.Transparent,
                    unselectedIconColor = lightSecondaryTextColor,
                    unselectedTextColor = lightTextColor
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            NavigationDrawerItem(
                icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
                label = { Text("Logout", fontWeight = FontWeight.Bold) },
                selected = false,
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    onNavigate(Login)
                    onCloseDrawer()
                },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = selectedItemContainerColor,
                    selectedIconColor = selectedItemIconColor,
                    selectedTextColor = selectedItemTextColor,
                    unselectedContainerColor = Color.Transparent,
                    unselectedIconColor = lightSecondaryTextColor,
                    unselectedTextColor = lightTextColor
                )
            )
        }
    }
}