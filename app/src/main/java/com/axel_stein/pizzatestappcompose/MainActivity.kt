package com.axel_stein.pizzatestappcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.axel_stein.pizzatestappcompose.ui.screens.pizza_order.PizzaOrderScreen
import com.axel_stein.pizzatestappcompose.ui.screens.pizza_order.components.FullscreenOverlayState
import com.axel_stein.pizzatestappcompose.ui.screens.pizza_order.components.LocalFullscreenOverlay
import com.axel_stein.pizzatestappcompose.ui.theme.PizzaTestAppComposeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var fullscreenOverlay by remember {
                mutableStateOf<(@Composable () -> Unit)?>(null)
            }
            val fullscreenOverlayState = remember {
                FullscreenOverlayState(
                    show = { content ->
                        fullscreenOverlay = content
                    },
                    hide = {
                        fullscreenOverlay = null
                    }
                )
            }
            CompositionLocalProvider(LocalFullscreenOverlay provides fullscreenOverlayState) {
                PizzaTestAppComposeTheme {
                    Box(Modifier.fillMaxSize()) {
                        PizzaOrderScreen()
                        fullscreenOverlay?.let { content ->
                            content()
                        }
                    }
                }
            }

        }
    }
}