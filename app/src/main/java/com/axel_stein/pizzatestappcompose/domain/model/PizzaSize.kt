package com.axel_stein.pizzatestappcompose.domain.model

enum class PizzaSize {
    Small,
    Medium,
    Large
}

val String.toPizzaSize: PizzaSize?
    get() = when (this) {
        "S" -> PizzaSize.Small
        "M" -> PizzaSize.Medium
        "L" -> PizzaSize.Large
        else -> null
    }