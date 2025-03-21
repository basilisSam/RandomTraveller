package com.example.randomtraveller.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.randomtraveller.R
import com.example.randomtraveller.ui.theme.RandomTravellerTheme

@Composable
fun PrimaryButton(
    text: String,
    isEnabled: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = { onClick() },
        modifier =
            modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(48.dp),
        enabled = isEnabled,
        colors =
            ButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContentColor = Color.White,
                disabledContainerColor = Color.LightGray,
            ),
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
        )
    }
}

@Composable
fun TitledTextFieldLikeButton(
    headerText: String = "",
    placeholderText: String = "",
    trailingIcon: Int? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = headerText,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp, lineHeight = 24.sp),
        )

        Spacer(modifier = Modifier.height(4.dp))

        Surface(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable(onClick = onClick),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 13.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = placeholderText,
                    style =
                        TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                )

                trailingIcon?.let {
                    Icon(
                        painterResource(it),
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DisabledPrimaryButtonPreview() {
    RandomTravellerTheme {
        PrimaryButton("Sign-in", false) { }
    }
}

@Preview
@Composable
private fun EnabledPrimaryButtonPreview() {
    RandomTravellerTheme {
        PrimaryButton("Sign-in", true) { }
    }
}

@Preview(showBackground = true)
@Composable
private fun TitledTextFieldLikeButtonPreview() {
    RandomTravellerTheme {
        TitledTextFieldLikeButton(
            headerText = "Starting date",
            placeholderText = "Select a date",
            trailingIcon = R.drawable.ic_calendar,
            onClick = {},
            modifier = Modifier,
        )
    }
}
