package com.axel_stein.pizzatestappcompose.ui.screens.pizza_order.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.axel_stein.pizzatestappcompose.R
import com.axel_stein.pizzatestappcompose.ui.theme.AppColors

@Composable
fun QuantityStepper(
    quantity: Int,
    price: String,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    val alphaAnimatable = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alphaAnimatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500)
        )
    }

    Row(
        modifier.graphicsLayer {
            alpha = alphaAnimatable.value
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            Modifier
                .background(AppColors.Highlight, RoundedCornerShape(24.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleIconButton(onClick = onDecrement, iconRes = R.drawable.ic_minus)

            Text(
                "$quantity",
                modifier = Modifier.widthIn(min = 48.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
            )

            CircleIconButton(onClick = onIncrement, iconRes = R.drawable.ic_plus)
        }

        Text(
            price,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )

        PrimaryButton(
            onClick = {},
            label = "Add"
        )
    }
}