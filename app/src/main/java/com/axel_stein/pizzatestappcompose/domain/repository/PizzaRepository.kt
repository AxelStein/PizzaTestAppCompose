package com.axel_stein.pizzatestappcompose.domain.repository

import com.axel_stein.pizzatestappcompose.domain.model.Pizza

interface PizzaRepository {

    suspend fun getPizzas(): Result<List<Pizza>>
}