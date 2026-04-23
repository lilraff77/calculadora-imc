package br.com.calculadoraimc

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationScreen(
    viewModel: ImcViewModel,
    onNavigateBack: () -> Unit // Corrigido: 'B' maiúsculo para manter o padrão camelCase
) {
    val imcResult by viewModel.imcResult.collectAsState()

    val (statusStr, recommendationStr, statusColor) = if (imcResult == null) {
        Triple(
            "Erro", "Valor não calculado",
            MaterialTheme.colorScheme.onBackground
        )
    } else {
        val value = imcResult!!
        when {
            value < 18.5 -> Triple(
                "Abaixo do peso",
                "Você está abaixo do peso ideal. Recomenda-se uma alimentação rica em nutrientes e consulta a um nutricionista.", // Pequena correção gramatical
                MaterialTheme.colorScheme.error
            )

            value < 24.9 -> Triple(
                "Peso normal", // Corrigido: adicionada a vírgula que estava faltando
                "Parabéns! Você está no seu peso ideal. Continue mantendo uma alimentação balanceada e fazendo exercícios.",
                MaterialTheme.colorScheme.primary
            )

            value < 29.9 -> Triple(
                "Sobrepeso",
                "Você está com sobrepeso. É recomendado praticar mais exercícios físicos e observar o consumo calórico.",
                MaterialTheme.colorScheme.error
            )

            else -> Triple(
                "Obesidade",
                "Você está com obesidade. É importante buscar a avaliação de um médico para orientação focada na saúde.",
                MaterialTheme.colorScheme.error
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recomendações") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { // Agora chama a função com o nome correto
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar") // Corrigido: contentDescription
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
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
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Situação Atual",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = statusStr,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = statusColor,
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
            )

            Text(
                text = "Orientações:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = recommendationStr,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}









