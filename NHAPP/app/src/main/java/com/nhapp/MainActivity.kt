package com.nhapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nhapp.data.remote.SupabaseClient
import com.nhapp.presentation.auth.AuthScreen
import com.nhapp.presentation.theme.NHAPPTheme
import io.github.jan.supabase.gotrue.auth

import com.nhapp.presentation.home.HomeScreen
import com.nhapp.presentation.chat.ChatScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NHAPPTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val currentUser = SupabaseClient.client.auth.currentSessionOrNull()
                    
                    val startDestination = if (currentUser != null) "home" else "auth"

                    NavHost(navController = navController, startDestination = startDestination) {
                        composable("auth") {
                            AuthScreen(
                                onAuthSuccess = {
                                    navController.navigate("home") {
                                        popUpTo("auth") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("home") {
                            HomeScreen(navController)
                        }
                        composable("chat/{chatId}") { backStackEntry ->
                            val chatId = backStackEntry.arguments?.getString("chatId") ?: return@composable
                            ChatScreen(chatId)
                        }
                    }
                }
            }
        }
    }
}
