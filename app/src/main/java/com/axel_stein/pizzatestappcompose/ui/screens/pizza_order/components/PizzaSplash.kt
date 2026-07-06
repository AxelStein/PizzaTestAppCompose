package com.axel_stein.pizzatestappcompose.ui.screens.pizza_order.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.axel_stein.pizzatestappcompose.R
import kotlin.math.roundToInt

enum class PizzaSplashState {
    Running,
    Disappearing,
    Ended
}

@Composable
fun PizzaSplash(
    restartOnEnd: Boolean,
    onStateChanged: (PizzaSplashState) -> Unit
) {
    val sweepAngleAnimatable = remember { Animatable(0f) }
    var restartAnimation by remember { mutableStateOf(false) }
    val path = remember { Path() }
    val disappearAnimatable = remember { Animatable(1f) }
    var disappearAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit, onStateChanged) {
        onStateChanged(PizzaSplashState.Running)
    }
    LaunchedEffect(restartAnimation) {
        sweepAngleAnimatable.snapTo(0f)
        sweepAngleAnimatable.animateTo(
            targetValue = 360f,
            animationSpec = tween(1200, easing = LinearEasing)
        )
    }
    LaunchedEffect(sweepAngleAnimatable.value) {
        if (sweepAngleAnimatable.value == 360f) {
            if (restartOnEnd) {
                restartAnimation = !restartAnimation
            } else {
                disappearAnimation = true
            }
        }
    }
    LaunchedEffect(disappearAnimation, onStateChanged) {
        if (disappearAnimation) {
            onStateChanged(PizzaSplashState.Disappearing)
            disappearAnimatable.animateTo(
                targetValue = 0f,
                animationSpec = tween(500)
            )
            onStateChanged(PizzaSplashState.Ended)
        }
    }

    val steppedAngle = (sweepAngleAnimatable.value / 45f).roundToInt() * 45f

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.splash),
            contentDescription = null,
            modifier = Modifier.drawWithContent {
                val cx = size.width / 2f
                val cy = size.height / 2f
                val radius = size.width / 2f

                val rect = Rect(
                    left = cx - radius,
                    top = cy - radius,
                    right = cx + radius,
                    bottom = cy + radius
                )

                path.reset()
                if (steppedAngle >= 360f) {
                    path.addOval(rect)
                } else {
                    path.moveTo(cx, cy)
                    path.arcTo(
                        rect = rect,
                        startAngleDegrees = 0f,
                        sweepAngleDegrees = steppedAngle,
                        forceMoveTo = false
                    )
                    path.close()
                }
                clipPath(path = path, clipOp = ClipOp.Intersect) {
                    this@drawWithContent.drawContent()
                }
            }.graphicsLayer {
                scaleX = disappearAnimatable.value
                scaleY = disappearAnimatable.value
                alpha = disappearAnimatable.value
            }
        )
    }
}