package br.com.calculadoraimc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text // Exemplo de importação necessária
import androidx.compose.runtime.Composable
import br.com.calculadoraimc.ui.theme.CalculadoraimcTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // O Theme deve envolver toda a aplicação para aplicar cores e fontes
            CalculadoraimcTheme {
                AppNavigation()
            }
        }
    }
}

