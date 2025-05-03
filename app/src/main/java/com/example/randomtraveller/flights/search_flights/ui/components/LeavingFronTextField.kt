package com.example.randomtraveller.flights.search_flights.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.example.randomtraveller.R
import com.example.randomtraveller.core.ui.TitledTextField
import com.example.randomtraveller.flights.search_flights.ui.OnAction
import com.example.randomtraveller.ui.theme.RandomTravellerTheme

@Composable
fun LeavingFromTextField(
    onAction: (OnAction) -> Unit,
    airportText: TextFieldValue,
) {
    TitledTextField(
        headerText = stringResource(R.string.leaving_from),
        placeholderText = stringResource(R.string.enter_airport),
        trailingIcon = R.drawable.ic_airplane,
        onValueChange = { onAction(OnAction.OnUpdateAirportText(it)) },
        currentText = airportText,
    )
}

@PreviewScreenSizes
@PreviewFontScale
@Composable
private fun LeavingFromTextFieldPreview() {
    RandomTravellerTheme {
        LeavingFromTextField({}, TextFieldValue())
    }
}