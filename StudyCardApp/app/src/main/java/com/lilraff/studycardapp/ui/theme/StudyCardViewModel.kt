package com.lilraff.studycardapp.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lilraff.studycardapp.model.*
import com.lilraff.studycardapp.repository.StudyCardRepository
git add .import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.launch

class StudyCardViewModel(private val repository: StudyCardRepository) : ViewModel() {

    var cards by mutableStateOf<List<Task>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)

    var token by mutableStateOf("")
        private set

    var userName by mutableStateOf("Usuário")
        private set

    fun login(email: String, pass: String, onSuccess: (String, String) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            repository.login(LoginRequest(email, pass)).onSuccess { response ->
                token = response.token
                val nameToSet = response.user?.name ?: "Usuário"
                userName = nameToSet
                isLoading = false
                onSuccess(response.token, nameToSet)
            }.onFailure { error ->
                isLoading = false
                errorMessage = "Erro ao logar: ${error.message}"
            }
        }
    }

    fun register(name: String, email: String, pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            repository.register(UserRequest(name, email, pass)).onSuccess {
                isLoading = false
                onSuccess()
            }.onFailure { error ->
                isLoading = false
                errorMessage = "Erro ao cadastrar: ${error.message}"
            }
        }
    }

    fun getCards(authToken: String? = null) {
        authToken?.let { this.token = it }
        if (token.isEmpty()) return

        viewModelScope.launch {
            isLoading = true
            repository.fetchCards(token).onSuccess { list ->
                cards = list
                errorMessage = null
            }.onFailure { error ->
                errorMessage = "Erro ao buscar cards: ${error.message}"
            }
            isLoading = false
        }
    }

    fun addTask(name: String, theme: String, text: String, endDate: String) {
        if (token.isEmpty()) return

        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val newTask = Task(
            name = name,
            theme = theme,
            text = text,
            endDate = endDate,
            createdAt = currentDate,
            status = "PENDENTE"
        )

        viewModelScope.launch {
            isLoading = true
            repository.createCard(token, newTask).onSuccess {
                getCards() 
            }.onFailure { error ->
                errorMessage = "Falha ao criar card: ${error.message}"
            }
            isLoading = false
        }
    }

    fun setAuthToken(newToken: String) {
        this.token = newToken
        if (newToken.isNotEmpty()) getCards()
    }

    fun updateUserName(newName: String) {
        this.userName = newName
    }

    fun toggleCardCompletion(cardId: String) {
        if (token.isEmpty()) return
        viewModelScope.launch {
            repository.completeCard(token, cardId).onSuccess {
                getCards()
            }.onFailure { error ->
                errorMessage = "Falha ao concluir: ${error.message}"
            }
        }
    }

    fun reactivateTask(cardId: String) {
        if (token.isEmpty()) return
        viewModelScope.launch {
            repository.reactivateCard(token, cardId).onSuccess {
                getCards()
            }.onFailure { error ->
                errorMessage = "Falha ao reativar: ${error.message}"
            }
        }
    }

    fun deleteTask(cardId: String) {
        if (token.isEmpty()) return
        viewModelScope.launch {
            isLoading = true
            repository.deleteCard(token, cardId).onSuccess {
                getCards()
            }.onFailure { error ->
                errorMessage = "Falha ao excluir: ${error.message}"
            }
            isLoading = false
        }
    }
}
