package com.axel_stein.pizzatestappcompose.ui.screens.pizza_order.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource


@Composable
fun CircleIconButton(
    onClick: () -> Unit,
    iconRes: Int,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    CircleButton(onClick, modifier = modifier) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription
        )
    }
}