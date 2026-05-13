package com.lilraff.studycardapp.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lilraff.studycardapp.model.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: StudyCardViewModel,
    name: String = "Usuário",
    onLogout: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getCards()
    }

    var selectedTab by remember { mutableIntStateOf(0) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }

    val principalBlue = Color(0xFF001A4D)
    val backgroundGray = Color(0xFFF5F5F5)

    if (viewModel.errorMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.errorMessage = null },
            title = { Text("Atenção") },
            text = { Text(viewModel.errorMessage ?: "") },
            confirmButton = { TextButton(onClick = { viewModel.errorMessage = null }) { Text("OK") } }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Study card", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = { showLogoutDialog = true }) {
                            Icon(Icons.AutoMirrored.Filled.ExitToApp, null, tint = Color.White)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = principalBlue)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = principalBlue,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Novo Card")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).background(backgroundGray)) {

            // Header Saudação
            Box(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .background(principalBlue, RoundedCornerShape(50.dp))
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(modifier = Modifier.size(65.dp), shape = CircleShape, color = Color.White) {
                        Icon(Icons.Default.Person, null, tint = principalBlue, modifier = Modifier.padding(12.dp))
                    }
                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text("Bem-Vindo,", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(name, color = Color.White, fontSize = 16.sp)
                    }
                }
            }

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = backgroundGray,
                contentColor = principalBlue,
                indicator = { tabPositions ->
                    if (selectedTab < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = principalBlue
                        )
                    }
                }
            ) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Pendentes") })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Concluídos") })
            }

            if (viewModel.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = principalBlue)
                }
            } else {
                val filteredTasks = viewModel.cards.filter { task ->
                    if (selectedTab == 0) !task.isCompleted else task.isCompleted
                }

                if (filteredTasks.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Info, null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                        Spacer(Modifier.height(16.dp))
                        Text("Nenhum card encontrado.", textAlign = TextAlign.Center, color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(items = filteredTasks, key = { it.id }) { taskItem ->
                            Card(
                                onClick = { if (selectedTab == 0) viewModel.toggleCardCompletion(taskItem.id) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = taskItem.name, fontWeight = FontWeight.Bold, color = principalBlue, modifier = Modifier.weight(1f))
                                        
                                        if (selectedTab == 1) {
                                            IconButton(onClick = { viewModel.reactivateTask(taskItem.id) }, modifier = Modifier.size(28.dp)) {
                                                Icon(Icons.Default.Refresh, "Reativar", tint = principalBlue)
                                            }
                                        }

                                        IconButton(onClick = { viewModel.deleteTask(taskItem.id) }, modifier = Modifier.size(28.dp)) {
                                            Icon(Icons.Default.Delete, "Excluir", tint = Color.Red.copy(alpha = 0.6f))
                                        }
                                    }
                                    Text(text = taskItem.text, fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.AutoMirrored.Filled.Label, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                                        Text(text = " ${taskItem.theme}", fontSize = 12.sp, color = Color.Gray)
                                        Spacer(modifier = Modifier.weight(1f))
                                        Column(horizontalAlignment = Alignment.End) {
                                            if (!taskItem.createdAt.isNullOrEmpty()) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(Icons.Default.EditCalendar, null, modifier = Modifier.size(12.dp), tint = Color.Gray)
                                                    Text(text = " Criado em: ${taskItem.createdAt}", fontSize = 11.sp, color = Color.Gray)
                                                }
                                            }
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.DateRange, null, modifier = Modifier.size(12.dp), tint = Color.Gray)
                                                Text(text = " Entrega: ${taskItem.endDate}", fontSize = 11.sp, color = Color.Gray)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showLogoutDialog) {
            ConfirmationDialog(
                message = "Deseja sair da sua conta?",
                confirmText = "Sair",
                onDismiss = { showLogoutDialog = false },
                onConfirm = { showLogoutDialog = false; onLogout() }
            )
        }

        if (showAddDialog) {
            AddCardDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { n, t, d, e -> viewModel.addTask(n, t, d, e); showAddDialog = false }
            )
        }
    }
}

@Composable
fun ConfirmationDialog(message: String, confirmText: String, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmação") },
        text = { Text(message) },
        confirmButton = {
            Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) { Text(confirmText) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
fun AddCardDialog(onDismiss: () -> Unit, onConfirm: (String, String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var theme by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    var dateValue by remember { mutableStateOf(TextFieldValue("")) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Novo Card") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = theme,
                    onValueChange = { theme = it },
                    label = { Text("Matéria") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Descrição") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = dateValue,
                    onValueChange = { input ->
                        val digits = input.text.filter { it.isDigit() }
                        if (digits.length <= 8) {
                            val formatted = buildString {
                                for (i in digits.indices) {
                                    append(digits[i])
                                    if ((i == 1 || i == 3) && i < digits.length - 1) {
                                        append("/")
                                    }
                                }
                            }
                            // Calcula a nova posição do cursor
                            val newCursorPosition = formatted.length
                            dateValue = TextFieldValue(
                                text = formatted,
                                selection = androidx.compose.ui.text.TextRange(newCursorPosition)
                            )
                        }
                    },
                    label = { Text("Data de Entrega (DD/MM/AAAA)") },
                    placeholder = { Text("00/00/0000") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name, theme, text, dateValue.text) },
                enabled = name.isNotBlank() && theme.isNotBlank() && text.isNotBlank() && dateValue.text.length == 10
            ) {
                Text("Salvar")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}