package com.example.randomtraveller.flights.search_criteria.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.randomtraveller.R
import com.example.randomtraveller.core.ui.TitledTextFieldLikeButton
import com.example.randomtraveller.flights.search_criteria.ui.OnAction
import com.example.randomtraveller.flights.search_criteria.ui.SelectedDateRange
import com.example.randomtraveller.ui.theme.RandomTravellerTheme

@Composable
fun DateRangeButton(
    startDate: SelectedDateRange,
    onAction: (OnAction) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    TitledTextFieldLikeButton(
        headerText = stringResource(R.string.dates),
        placeholderText =
            startDate.startDateText
                ?: stringResource(R.string.select_dates),
        trailingIcon = R.drawable.ic_calendar,
        modifier =
            Modifier
                .padding(top = 24.dp)
                .clickable {
                    focusManager.clearFocus(true)
                    onAction(OnAction.OnShowCalendarPicker)
                },
    )
}

@PreviewScreenSizes
@PreviewFontScale
@Composable
private fun DateRangeButtonPreview() {
    RandomTravellerTheme {
        DateRangeButton(SelectedDateRange(), {})
    }
}