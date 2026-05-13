package com.lilraff.studycardapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lilraff.studycardapp.ui.theme.HomeScreen
import com.lilraff.studycardapp.ui.theme.LoginScreen
import com.lilraff.studycardapp.ui.theme.RegisterScreen
import com.lilraff.studycardapp.ui.theme.StudyCardViewModel
import com.lilraff.studycardapp.ui.theme.StudyCardViewModelFactory
import com.lilraff.studycardapp.repository.StudyCardRepository
import com.lilraff.studycardapp.network.RetrofitClient
import com.lilraff.studycardapp.utils.SessionManager

class MainActivity : ComponentActivity() {

    private val viewModel: StudyCardViewModel by viewModels {
        StudyCardViewModelFactory(StudyCardRepository(RetrofitClient.instance))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sessionManager = SessionManager(this)

        setContent {
            val navController = rememberNavController()
            val savedToken = sessionManager.fetchAuthToken()
            val savedName = sessionManager.fetchUserName() ?: "Usuário"
            val startRoute = if (!savedToken.isNullOrEmpty()) "home" else "login"

            NavHost(navController = navController, startDestination = startRoute) {
                composable("login") {
                    LoginScreen(
                        viewModel = viewModel,
                        onLoginSuccess = { token, name ->
                            sessionManager.saveAuthToken(token)
                            sessionManager.saveUserName(name)
                            viewModel.setAuthToken(token)
                            viewModel.updateUserName(name)
                            navController.navigate("home") { 
                                popUpTo("login") { inclusive = true } 
                            }
                        },
                        onNavigateToRegister = { navController.navigate("register") }
                    )
                }

                composable("register") {
                    RegisterScreen(
                        viewModel = viewModel,
                        onRegisterSuccess = {
                            navController.navigate("login") { 
                                popUpTo("register") { inclusive = true } 
                            }
                        },
                        onNavigateToLogin = { navController.popBackStack() }
                    )
                }

                composable("home") {
                    LaunchedEffect(Unit) {
                        if (!savedToken.isNullOrEmpty()) {
                            viewModel.setAuthToken(savedToken)
                        }
                        viewModel.updateUserName(savedName)
                    }

                    HomeScreen(
                        viewModel = viewModel,
                        name = viewModel.userName,
                        onLogout = {
                            sessionManager.clearData()
                            navController.navigate("login") { 
                                popUpTo("home") { inclusive = true } 
                            }
                        }
                    )
                }
            }
        }
    }
}
