package com.axel_stein.pizzatestappcompose.ui.screens.pizza_order.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.axel_stein.pizzatestappcompose.R
import com.axel_stein.pizzatestappcompose.ui.theme.AppColors
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun PizzaOrderToolbar(
    pizzaName: String?,
    onNavigateUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var textState by remember { mutableStateOf(pizzaName) }

    val windowSize = LocalWindowInfo.current.containerSize
    val windowWidthPx = windowSize.width.toFloat()

    val btnNavigateAnimatable = remember { Animatable(-windowWidthPx) }
    val btnFavoriteAnimatable = remember { Animatable(windowWidthPx) }
    val titleAnimatable = remember { Animatable(-windowWidthPx) }

    LaunchedEffect(pizzaName) {
        textState = pizzaName
    }

    LaunchedEffect(Unit) {
        coroutineScope {
            launch {
                btnNavigateAnimatable.animateTo(
                    targetValue = 0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                )
            }
            launch {
                btnFavoriteAnimatable.animateTo(
                    targetValue = 0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                )
            }
            launch {
                titleAnimatable.animateTo(
                    targetValue = 0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                )
            }
        }
    }

    Row(modifier.padding(horizontal = 24.dp, vertical = 18.dp)) {
        CircleIconButton(
            onClick = onNavigateUpClick,
            iconRes = R.drawable.ic_navigate_up,
            modifier = Modifier.graphicsLayer {
                translationX = btnNavigateAnimatable.value
            }
        )

        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = 24.dp)
                .graphicsLayer {
                    translationY = titleAnimatable.value
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Pizzas",
                fontSize = 10.sp,
                color = AppColors.BaseText
            )
            AnimatedContent(
                targetState = textState,
                transitionSpec = {
                    val enterTransition = fadeIn(animationSpec = tween(durationMillis = 400))
                    val exitTransition = fadeOut(animationSpec = tween(durationMillis = 400))
                    enterTransition togetherWith exitTransition
                }
            ) { text ->
                Text(
                    text.orEmpty(),
                    fontSize = 24.sp,
                    lineHeight = 24.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.SemiBold,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.offset(y = (-5).dp)
                )
            }
        }

        CircleIconButton(
            onClick = {},
            iconRes = R.drawable.ic_favorite_empty,
            modifier = Modifier.graphicsLayer {
                translationX = btnFavoriteAnimatable.value
            }
        )
    }
}