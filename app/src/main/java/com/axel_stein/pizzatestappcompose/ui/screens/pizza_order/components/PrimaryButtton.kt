package com.axel_stein.pizzatestappcompose.ui.screens.pizza_order.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick,
        modifier = modifier.height(48.dp),
        contentPadding = PaddingValues(horizontal = 18.dp),
        enabled = enabled
    ) {
        Text(
            label,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp,
            lineHeight = 24.sp
        )
    }
}