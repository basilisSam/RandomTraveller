package com.example.randomtraveller.flights

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.randomtraveller.ui.theme.RandomTravellerTheme
import com.firebase.ui.auth.AuthUI

@Composable
fun SearchFlightScreen(modifier: Modifier) {
    RandomTravellerTheme {
        Scaffold(modifier = Modifier) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Text("Search Flight")
                    Spacer(Modifier.size(40.dp))

                    val context = LocalContext.current
                    Button(onClick = {
                        AuthUI.getInstance().signOut(context)
                    }) {
                        Text("Sign Out")
                    }
                }
            }
        }
    }
}