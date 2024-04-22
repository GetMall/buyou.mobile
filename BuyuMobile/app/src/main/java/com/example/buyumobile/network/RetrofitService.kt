package com.example.buyumobile.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {
    const val BASE_URL = "http://localhost:8080/api"

    fun getApiUsuarios(): ApiUsuarios {
        val cliente = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiUsuarios::class.java)

        return cliente
    }
}