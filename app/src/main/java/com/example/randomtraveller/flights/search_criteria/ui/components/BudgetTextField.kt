package com.example.randomtraveller.flights.search_criteria.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.randomtraveller.R
import com.example.randomtraveller.core.ui.TitledTextField
import com.example.randomtraveller.flights.search_criteria.ui.OnAction
import com.example.randomtraveller.ui.theme.RandomTravellerTheme

@Composable
fun BudgetTextField(
    onAction: (OnAction) -> Unit,
    budgetText: TextFieldValue,
) {
    TitledTextField(
        headerText = stringResource(R.string.budget),
        placeholderText = stringResource(R.string.enter_budget),
        trailingIcon = R.drawable.ic_dollar,
        onValueChange = { onAction(OnAction.OnUpdateBudget(it)) },
        currentText = budgetText,
        keyboardType = KeyboardType.Number,
        modifier = Modifier.padding(top = 24.dp),
    )
}

@PreviewScreenSizes
@PreviewFontScale
@Composable
private fun BudgetTextFieldPreview() {
    RandomTravellerTheme {
        BudgetTextField({}, TextFieldValue())
    }
}