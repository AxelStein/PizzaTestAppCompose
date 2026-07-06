package com.axel_stein.pizzatestappcompose.ui.screens.pizza_order.model

import com.axel_stein.pizzatestappcompose.domain.model.PizzaSize

data class PizzaImage(
    val id: String?,
    val url: String?,
    val size: PizzaSize
)