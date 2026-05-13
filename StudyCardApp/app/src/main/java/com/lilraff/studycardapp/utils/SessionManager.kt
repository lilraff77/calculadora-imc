package com.lilraff.studycardapp.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Gerenciador de sessão para salvar e recuperar o token de autenticação (JWT) e dados do usuário.
 */
class SessionManager(context: Context) {

    // Cria o arquivo de preferências chamado "study_card_prefs"
    private val prefs: SharedPreferences =
        context.getSharedPreferences("study_card_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val USER_TOKEN = "user_token"
        private const val USER_NAME = "user_name"
    }

    /**
     * Salva o token de autenticação.
     */
    fun saveAuthToken(token: String) {
        prefs.edit().putString(USER_TOKEN, token).apply()
    }

    /**
     * Salva o nome do usuário para exibição na HomeScreen.
     */
    fun saveUserName(name: String) {
        prefs.edit().putString(USER_NAME, name).apply()
    }

    /**
     * Recupera o token salvo. Retorna null se não houver sessão ativa.
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    /**
     * Recupera o nome do usuário salvo.
     */
    fun fetchUserName(): String? {
        return prefs.getString(USER_NAME, "Usuário")
    }

    /**
     * Limpa todos os dados salvos (usado no Logout).
     */
    fun clearData() {
        prefs.edit().clear().apply()
    }
}