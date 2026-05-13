package com.lilraff.studycardapp.network

import com.lilraff.studycardapp.model.*
import retrofit2.Response
import retrofit2.http.*

interface StudyCardApi {

    // --- USUÁRIOS & AUTH ---

    @POST("users")
    suspend fun register(@Body request: UserRequest): Response<Unit>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // --- CARDS (CRUD) ---

    @GET("cards")
    suspend fun getCards(
        @Header("Authorization") token: String
    ): Response<List<CardResponse>> // Usando CardResponse para bater com Repository e ViewModel

    @POST("cards")
    suspend fun createCard(
        @Header("Authorization") token: String,
        @Body request: CardRequest
    ): Response<CardResponse>

    @PUT("cards/{id}")
    suspend fun updateCard(
        @Header("Authorization") token: String,
        @Path("id") id: String, // Mudado para String conforme seu CardResponse
        @Body request: CardRequest
    ): Response<CardResponse>

    @DELETE("cards/{id}")
    suspend fun deleteCard(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Unit>

    // --- AÇÕES ESPECÍFICAS (PATCH) ---

    @PATCH("cards/{id}/concluir")
    suspend fun completeCard(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Unit>

    @PATCH("cards/{id}/cancelar")
    suspend fun cancelCard(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Unit>

    @PATCH("cards/{id}/reativar")
    suspend fun reactivateCard(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Unit>
}