package com.example.randomtraveller

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.randomtraveller.navigation.NavHost
import com.example.randomtraveller.ui.theme.RandomTravellerTheme
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val signInLauncher =
        this.registerForActivityResult(
            FirebaseAuthUIActivityResultContract(),
        ) { result ->
            this.onSignInResult(result)
        }

    private val signInProviders =
        arrayListOf(
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build(),
        )
    private val signInIntent =
        AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(signInProviders)
            .setTheme(R.style.Theme_RandomTraveller)
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RandomTravellerTheme {
                val navController = rememberNavController()
                NavHost(
                    navController,
                    ::launchLoginFlow,
                    ::logout,
                    Modifier,
                )
            }
        }
    }

    private fun logout() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                Toast.makeText(this, "Signed Out", Toast.LENGTH_LONG).show()
            }
    }

    private fun launchLoginFlow() {
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        Toast.makeText(this, if (result.resultCode == RESULT_OK) "YES" else "NO", Toast.LENGTH_LONG)
            .show()
    }
}
