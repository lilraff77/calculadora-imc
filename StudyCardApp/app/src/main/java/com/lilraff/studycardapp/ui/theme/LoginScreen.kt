package com.lilraff.studycardapp.ui.theme

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    viewModel: StudyCardViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: (String, String) -> Unit // Agora recebe token e nome
) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var showValidationError by rememberSaveable { mutableStateOf(false) }

    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible = true }

    if (showValidationError) {
        AlertDialog(
            onDismissRequest = { showValidationError = false },
            title = { Text("Campos Vazios") },
            text = { Text("Por favor, digite seu email e senha.") },
            confirmButton = { TextButton(onClick = { showValidationError = false }) { Text("OK") } }
        )
    }

    if (viewModel.errorMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.errorMessage = null },
            title = { Text("Erro no Login") },
            text = { Text(viewModel.errorMessage ?: "Erro desconhecido") },
            confirmButton = { TextButton(onClick = { viewModel.errorMessage = null }) { Text("Tentar novamente") } }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize().background(
            brush = Brush.verticalGradient(colors = listOf(Color(0xFF001F3F), Color(0xFF003366)))
        ),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(visible = isVisible, enter = fadeIn(tween(1000)) + expandVertically(tween(1000))) {
            Column(
                modifier = Modifier.fillMaxWidth(0.9f).padding(16.dp).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Study Card", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 40.dp))

                CustomInputField(email, { email = it }, "Email", "Digite seu Email", imeAction = ImeAction.Next)
                Spacer(Modifier.height(16.dp))
                CustomInputField(senha, { senha = it }, "Senha", "Digite sua senha", isPassword = true, imeAction = ImeAction.Done)

                Spacer(Modifier.height(48.dp))

                if (viewModel.isLoading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    OutlinedButton(
                        onClick = {
                            if (email.isBlank() || senha.isBlank()) showValidationError = true
                            else viewModel.login(email, senha) { token, nome -> onLoginSuccess(token, nome) }
                        },
                        modifier = Modifier.fillMaxWidth().height(55.dp),
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, Color.White),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFF001F3F), contentColor = Color.White)
                    ) {
                        Text("Entrar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
                TextButton(onClick = onNavigateToRegister) {
                    Text("Não tem uma conta? Cadastre-se", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun CustomInputField(value: String, onValueChange: (String) -> Unit, label: String, placeholder: String, isPassword: Boolean = false, imeAction: ImeAction = ImeAction.Default) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, color = Color.White, fontSize = 14.sp)
        OutlinedTextField(
            value = value, onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Email, imeAction = imeAction),
            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White, focusedTextColor = Color.Black, unfocusedTextColor = Color.Black)
        )
    }
}