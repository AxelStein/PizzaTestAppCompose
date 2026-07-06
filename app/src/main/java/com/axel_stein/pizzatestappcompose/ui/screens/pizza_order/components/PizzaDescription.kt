package com.axel_stein.pizzatestappcompose.ui.screens.pizza_order.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.sp

@Composable
fun PizzaDescription(description: String, modifier: Modifier = Modifier) {
    var descriptionState by remember { mutableStateOf(description) }

    val windowSize = LocalWindowInfo.current.containerSize
    val windowHeightPx = windowSize.height.toFloat()

    val ty = remember { Animatable(windowHeightPx) }

    LaunchedEffect(Unit) {
        ty.animateTo(
            targetValue = 0f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        )
    }

    LaunchedEffect(description) {
        descriptionState = description
    }

    AnimatedContent(
        targetState = descriptionState,
        transitionSpec = {
            val enterTransition = fadeIn(animationSpec = tween(durationMillis = 400))
            val exitTransition = fadeOut(animationSpec = tween(durationMillis = 400))
            enterTransition togetherWith exitTransition
        },
        modifier = modifier
            .graphicsLayer {
                translationY = ty.value
            }
    ) { text ->
        Text(
            text,
            fontSize = 14.sp
        )
    }
}