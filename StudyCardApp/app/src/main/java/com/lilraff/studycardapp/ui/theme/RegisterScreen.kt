package com.lilraff.studycardapp.ui.theme

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegisterScreen(
    viewModel: StudyCardViewModel, // Adicionado para integração com a API
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var showValidationError by rememberSaveable { mutableStateOf(false) }

    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible = true }

    // Alerta para campos locais vazios
    if (showValidationError) {
        AlertDialog(
            onDismissRequest = { showValidationError = false },
            title = { Text("Cadastro Incompleto") },
            text = { Text("Para continuar, preencha seu nome, email e crie uma senha.") },
            confirmButton = {
                TextButton(onClick = { showValidationError = false }) {
                    Text("OK")
                }
            }
        )
    }

    // Alerta para erros de rede ou servidor
    if (viewModel.errorMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.errorMessage = null },
            title = { Text("Erro no Cadastro") },
            text = { Text(viewModel.errorMessage ?: "Não foi possível realizar o cadastro.") },
            confirmButton = {
                TextButton(onClick = { viewModel.errorMessage = null }) {
                    Text("OK")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF001F3F),
                        Color(0xFF003366)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(tween(1000)) + expandVertically(animationSpec = tween(1000))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Study Card",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 20.dp, bottom = 40.dp)
                )

                Text(
                    text = "Cadastre-se",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                CustomInputField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = "Nome",
                    placeholder = "Digite seu nome",
                    imeAction = ImeAction.Next
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomInputField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    placeholder = "Digite seu Email",
                    imeAction = ImeAction.Next
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomInputField(
                    value = senha,
                    onValueChange = { senha = it },
                    label = "Senha",
                    placeholder = "Crie sua senha",
                    isPassword = true,
                    imeAction = ImeAction.Done
                )

                Spacer(modifier = Modifier.height(48.dp))

                if (viewModel.isLoading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    OutlinedButton(
                        onClick = {
                            if (nome.isBlank() || email.isBlank() || senha.isBlank()) {
                                showValidationError = true
                            } else {
                                viewModel.register(nome, email, senha) {
                                    onRegisterSuccess()
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, Color.White),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(0xFF001F3F),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Cadastrar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = onNavigateToLogin) {
                    Text("Já tem uma conta? Faça Login", color = Color.White, fontSize = 14.sp)
                }
            }
        }
    }
}