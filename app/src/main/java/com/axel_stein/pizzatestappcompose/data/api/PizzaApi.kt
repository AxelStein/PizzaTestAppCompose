package com.axel_stein.pizzatestappcompose.data.api

import com.axel_stein.pizzatestappcompose.data.model.PizzasListApiModel
import retrofit2.Response
import retrofit2.http.GET

interface PizzaApi {

    @GET("pizzas")
    suspend fun getPizzas(): Response<PizzasListApiModel>
}