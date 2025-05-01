package com.example.randomtraveller.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DashedHorizontalDivider(
    color: Color = Color.Gray,
    thickness: Dp = 1.dp,
    dashLength: Dp = 2.dp,
    gapLength: Dp = 2.dp,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness)
    ) {
        val canvasWidth = size.width
        val dashPathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(dashLength.toPx(), gapLength.toPx()),
            phase = 0f
        )

        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(canvasWidth, 0f),
            pathEffect = dashPathEffect
        )
    }
}