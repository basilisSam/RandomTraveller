package com.example.randomtraveller.flights.ui

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.randomtraveller.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePicker(
    selectedDateInLong: Long? = null,
    onDateSelected: (OnAction.OnDateSelected) -> Unit,
    onDismiss: () -> Unit
) {
    val dateState = rememberDatePickerState(initialSelectedDateMillis = selectedDateInLong)
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(OnAction.OnDateSelected(dateState.selectedDateMillis))
                onDismiss()
            }) {
                Text(stringResource(R.string.select))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
    {
        DatePicker(state = dateState)
    }
}