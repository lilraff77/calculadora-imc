package com.lilraff.studycardapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton responsável por configurar e fornecer a instância do Retrofit.
 * Usamos 'object' para garantir que exista apenas uma instância em todo o app.
 */
object RetrofitClient {
    // URL base da API hospedada no Azure
    private const val BASE_URL = "https://studycard.azurewebsites.net/"

    val instance: StudyCardApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Converte o JSON da API para objetos Kotlin
            .build()
            .create(StudyCardApi::class.java)
    }
}