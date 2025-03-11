package com.example.randomtraveller.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.randomtraveller.ui.theme.RandomTravellerTheme

@Composable
fun LoginScreen(
    onLoginClicked: () -> Unit,
    onLogoutClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    RandomTravellerTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(modifier = modifier.padding(innerPadding)) {
                    LoginButton(onLoginClicked)
                    LogoutButton(onLogoutClicked)
                }
            }
        }
    }
}

@Composable
fun LoginButton(onLoginClicked: () -> Unit) {
    Button(onClick = {
        onLoginClicked()
    }) {
        Text("Sign In")
    }
}

@Composable
fun LogoutButton(onLogoutClicked: () -> Unit) {
    Button(onClick = { onLogoutClicked() }) {
        Text("Sign out")
    }
}
