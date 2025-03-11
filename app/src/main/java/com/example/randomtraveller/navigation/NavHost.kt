package com.example.randomtraveller.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.randomtraveller.login.LoginScreen

@Composable
fun NavHost(
    navController: NavHostController,
    onLoginClicked: () -> Unit,
    onLogoutClicked: () -> Unit,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Login,
        modifier = modifier
    ) {
        composable<Login> {
            LoginScreen(onLoginClicked, onLogoutClicked, modifier)
        }
    }
}