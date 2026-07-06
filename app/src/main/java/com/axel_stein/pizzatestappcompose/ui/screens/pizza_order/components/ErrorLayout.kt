package com.axel_stein.pizzatestappcompose.ui.screens.pizza_order.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ErrorLayout(error: Throwable, onRetryClick: () -> Unit) {
    Box(
        Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                error.message ?: "Error",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )

            PrimaryButton(
                onRetryClick,
                label = "Try again",
                modifier = Modifier.padding(top = 20.dp)
            )
        }
    }
}