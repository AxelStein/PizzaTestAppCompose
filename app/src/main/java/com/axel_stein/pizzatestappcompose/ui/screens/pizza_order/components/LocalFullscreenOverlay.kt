package com.axel_stein.pizzatestappcompose.ui.screens.pizza_order.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

data class FullscreenOverlayState(
    val show: (@Composable () -> Unit) -> Unit,
    val hide: () -> Unit
)

val LocalFullscreenOverlay = staticCompositionLocalOf {
    FullscreenOverlayState(
        show = {},
        hide = {}
    )
}