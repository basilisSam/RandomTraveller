package com.example.randomtraveller.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(modifier: Modifier, onAuthResult: (Boolean) -> Unit) {
    LaunchedEffect(Unit) {
        val isLoggedIn = FirebaseAuth.getInstance().currentUser != null
        onAuthResult(isLoggedIn)
    }

    // Your actual splash screen UI (e.g., logo, background)
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Loading...") // Placeholder UI
    }
}
