package com.lilraff.studycardapp.repository

import com.lilraff.studycardapp.model.*
import com.lilraff.studycardapp.network.StudyCardApi

class StudyCardRepository(private val api: StudyCardApi) {

    suspend fun login(req: LoginRequest): Result<LoginResponse> {
        return try {
            val response = api.login(req)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Falha no login: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(req: UserRequest): Result<Unit> {
        return try {
            val response = api.register(req)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Erro no cadastro: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchCards(token: String): Result<List<Task>> {
        return try {
            val formattedToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            val response = api.getCards(formattedToken)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Erro ao buscar cards: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createCard(token: String, task: Task): Result<Task> {
        return try {
            val formattedToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            val request = CardRequest(
                titulo = task.name,
                materia = task.theme,
                descricao = task.text,
                dataEntrega = task.endDate
            )
            val response = api.createCard(formattedToken, request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Erro ao criar card: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun completeCard(token: String, cardId: String): Result<Unit> {
        return try {
            val formattedToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            val response = api.completeCard(formattedToken, cardId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Erro ao completar card: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteCard(token: String, cardId: String): Result<Unit> {
        return try {
            val formattedToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            val response = api.deleteCard(formattedToken, cardId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Erro ao excluir card: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun reactivateCard(token: String, cardId: String): Result<Unit> {
        return try {
            val formattedToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            val response = api.reactivateCard(formattedToken, cardId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Erro ao reativar card: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
