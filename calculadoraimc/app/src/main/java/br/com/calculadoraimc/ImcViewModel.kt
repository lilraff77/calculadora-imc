package br.com.calculadoraimc

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ImcViewModel : ViewModel() {

    private val _imcResult = MutableStateFlow<Float?>(null)
    val imcResult: StateFlow<Float?> = _imcResult.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun calculateImc(weightStr: String, heightCmStr: String): Boolean {
        return try {
            val weightStrFix = weightStr.replace(",", ".")
            val heightStrFix = heightCmStr.replace(",", ".")

            // ✅ conversão correta
            val weight = weightStrFix.toFloat()
            val heightCm = heightStrFix.toFloat()

            // ✅ validação
            if (weight <= 0f || heightCm <= 0f) {
                _errorMessage.value = "Os valores devem ser maiores que zero"
                return false
            }

            val heightMeters = heightCm / 100f
            val imc = weight / (heightMeters * heightMeters)

            _imcResult.value = imc
            _errorMessage.value = null
            true

        } catch (e: NumberFormatException) {
            _errorMessage.value = "Por favor, insira valores numéricos válidos"
            false
        } catch (e: Exception) {
            _errorMessage.value = "Ocorreu um erro inesperado"
            false
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun reset() {
        _imcResult.value = null
        _errorMessage.value = null
    }
}



