package com.axel_stein.pizzatestappcompose.ui.screens.pizza_order.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.axel_stein.pizzatestappcompose.R
import com.axel_stein.pizzatestappcompose.domain.model.PizzaSize

@Composable
fun PizzaSizeSelector(
    currentSize: PizzaSize,
    onSizeChanged: (PizzaSize) -> Unit,
    onCoordinatesChanged: (LayoutCoordinates) -> Unit,
    modifier: Modifier = Modifier
) {
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

    Box(
        modifier.graphicsLayer {
            translationY = ty.value
        }
    ) {
        Image(
            painter = painterResource(R.drawable.label_scale),
            contentDescription = null,
            modifier = Modifier
                .padding(end = 13.dp)
                .align(Alignment.TopCenter)
        )
        Image(
            painter = painterResource(R.drawable.banana),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 13.dp)
                .fillMaxWidth(0.2587f)
                .rotate(180f)
                .align(Alignment.TopCenter)
        )

        Row(Modifier.padding(top = 40.dp)) {
            PizzaSizeButton(
                onClick = {
                    onSizeChanged(PizzaSize.Small)
                },
                text = "S",
                isSelected = currentSize == PizzaSize.Small
            )

            Spacer(Modifier.size(50.dp))

            PizzaSizeButton(
                onClick = {
                    onSizeChanged(PizzaSize.Medium)
                },
                text = "M",
                isSelected = currentSize == PizzaSize.Medium,
                modifier = Modifier.padding(top = 16.dp)
                    .onGloballyPositioned(onCoordinatesChanged)
            )

            Spacer(Modifier.size(50.dp))

            PizzaSizeButton(
                onClick = {
                    onSizeChanged(PizzaSize.Large)
                },
                text = "L",
                isSelected = currentSize == PizzaSize.Large
            )
        }
    }
}


@Composable
private fun PizzaSizeButton(
    onClick: () -> Unit,
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    CircleButton(
        onClick = onClick,
        isElevated = !isSelected,
        modifier = modifier
    ) {
        Box(
            Modifier
                .padding(2.dp)
                .size(48.dp)
                .clip(CircleShape)
                .background(if (isSelected) Color.Black else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text,
                color = if (isSelected) Color.White else Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
        }
    }
}