package com.example.randomtraveller.login

import android.app.Activity.RESULT_OK
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.randomtraveller.R
import com.example.randomtraveller.ui.theme.RandomTravellerTheme
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult

private val signInProviders =
    arrayListOf(
        AuthUI.IdpConfig.PhoneBuilder().build(),
        AuthUI.IdpConfig.EmailBuilder().build(),
    )
private val signInIntent =
    AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(signInProviders)
        .setTheme(R.style.SigninTheme)
        .build()

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
) {
    RandomTravellerTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = modifier.padding(innerPadding)) {
                    LoginButton()
                }
            }
        }
    }
}

@Composable
fun LoginButton() {
    val context = LocalContext.current
    val sigInInLauncher = getSigInInLauncher(context)

    Button(onClick = { sigInInLauncher.launch(signInIntent) }) {
        Text("Sign In")
    }
}

@Composable
private fun getSigInInLauncher(
    context: Context,
) = rememberLauncherForActivityResult(
    contract = FirebaseAuthUIActivityResultContract(),
) { result ->
    onSignInResult(context, result)
}

private fun onSignInResult(
    context: Context,
    result: FirebaseAuthUIAuthenticationResult,
) {
    if (result.resultCode != RESULT_OK) {
        Toast.makeText(context, "Couldn't login ${result.idpResponse?.error}", Toast.LENGTH_LONG)
            .show()
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    RandomTravellerTheme {
        LoginScreen(Modifier)
    }
}
