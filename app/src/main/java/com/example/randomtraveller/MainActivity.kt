package com.example.randomtraveller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.randomtraveller.navigation.NavHost
import com.example.randomtraveller.ui.theme.RandomTravellerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RandomTravellerTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    modifier = Modifier,
                )
            }
        }
    }
}
