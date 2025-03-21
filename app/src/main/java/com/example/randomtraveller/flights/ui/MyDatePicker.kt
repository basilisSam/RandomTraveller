package com.example.randomtraveller.flights.ui

import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.randomtraveller.R
import com.example.randomtraveller.core.utils.toLocalDate
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePicker(
    selectedStartDate: Long? = null,
    selectedEndDate: Long? = null,
    onAction: (OnAction) -> Unit,
) {
    val dateState =
        rememberDateRangePickerState(
            initialSelectedStartDateMillis = selectedStartDate,
            initialSelectedEndDateMillis = selectedEndDate,
            selectableDates =
                object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        return !utcTimeMillis.toLocalDate().isBefore(LocalDate.now())
                    }
                },
        )
    DatePickerDialog(
        onDismissRequest = { onAction(OnAction.OnDismissDatePicker) },
        confirmButton = {
            TextButton(onClick = {
                onAction(
                    OnAction.OnDateRangeSelected(
                        dateState.selectedStartDateMillis,
                        dateState.selectedEndDateMillis,
                    ),
                )
                onAction(OnAction.OnDismissDatePicker)
            }) {
                Text(stringResource(R.string.select))
            }
        },
        dismissButton = {
            TextButton(onClick = { onAction(OnAction.OnDismissDatePicker) }) {
                Text(stringResource(R.string.cancel))
            }
        },
    ) {
        DateRangePicker(state = dateState)
    }
}
