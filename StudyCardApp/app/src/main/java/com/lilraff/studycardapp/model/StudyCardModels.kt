package com.lilraff.studycardapp.model

import com.google.gson.annotations.SerializedName

// --- MODELOS DE AUTENTICAÇÃO ---
data class UserRequest(
    val name: String,
    val email: String,
    @SerializedName("pass")
    val password: String
)

data class LoginRequest(
    val email: String,
    @SerializedName("pass")
    val password: String
)

data class UserResponse(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null
)

data class LoginResponse(
    val token: String,
    val user: UserResponse? = null
)

// --- MODELO DE TAREFA (TASK) ---
data class Task(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("titulo")
    val name: String,

    @SerializedName("materia")
    val theme: String,

    @SerializedName("descricao")
    val text: String,

    @SerializedName("dataEntrega")
    val endDate: String,

    @SerializedName("dataCriacao")
    val createdAt: String? = null,

    @SerializedName("status")
    val status: String = "PENDENTE"
) {
    val isCompleted: Boolean
        get() = status.equals("CONCLUIDO", ignoreCase = true)
}

// Objeto para criação
data class CardRequest(
    @SerializedName("titulo")
    val titulo: String,
    @SerializedName("materia")
    val materia: String,
    @SerializedName("descricao")
    val descricao: String,
    @SerializedName("dataEntrega")
    val dataEntrega: String
)

typealias CardResponse = Task
