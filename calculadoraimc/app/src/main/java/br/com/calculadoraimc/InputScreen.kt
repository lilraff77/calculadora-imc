package br.com.calculadoraimc

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(
    viewModel: ImcViewModel,
    onCalculateSuccess: () -> Unit
) {
    var weightInput by remember { mutableStateOf("") }
    var heightInput by remember { mutableStateOf("") }
    // Assumindo que seu ViewModel usa StateFlow ou LiveData
    val errorMessage by viewModel.errorMessage.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calculadora de IMC") }, // Corrigido: era } e o correto é )
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally, // Corrigido: Alignment (estava Aligment)
            verticalArrangement = Arrangement.Center // Corrigido: Arrangement (estava veritical)
        ) {
            Text(
                text = "Insira seus dados",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = weightInput,
                onValueChange = { weightInput = it }, // Corrigido: onValueChange (estava onvalueChange)
                label = { Text("Peso (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), // Dica: use Decimal para pesos como 70.5
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = heightInput,
                onValueChange = { heightInput = it },
                label = { Text("Altura (cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            val hasError = !errorMessage.isNullOrEmpty()
            if (hasError) {
                Text(
                    text = errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Button(
                onClick = {
                    val sucess = viewModel.calculateImc(weightInput, heightInput)
                    if (sucess) {
                        onCalculateSuccess()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp), // Faltava uma vírgula aqui
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("CALCULAR IMC", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}















