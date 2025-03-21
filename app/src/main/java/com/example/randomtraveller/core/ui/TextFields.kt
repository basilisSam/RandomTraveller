package com.example.randomtraveller.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.randomtraveller.R
import com.example.randomtraveller.ui.theme.RandomTravellerTheme

@Composable
fun TitledTextField(
    headerText: String = "",
    placeholderText: String = "",
    trailingIcon: Int? = null,
    currentText: TextFieldValue = TextFieldValue(),
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Unspecified,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = headerText,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp, lineHeight = 24.sp),
        )

        Spacer(modifier = Modifier.height(4.dp))

        TextField(
            value = currentText,
            onValueChange = { onValueChange(it.text) },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            trailingIcon = {
                trailingIcon?.let {
                    Icon(
                        painterResource(trailingIcon),
                        contentDescription = null,
                    )
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            placeholder = { Text(placeholderText) },
            colors =
                TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TitledTextFieldPreview() {
    RandomTravellerTheme {
        TitledTextField(
            headerText = "Leaving from",
            placeholderText = "Enter airport",
            trailingIcon = R.drawable.ic_calendar,
            onValueChange = {},
        )
    }
}
