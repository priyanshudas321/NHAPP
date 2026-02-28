package com.nhapp.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nhapp.data.remote.SupabaseClient
import com.nhapp.domain.model.Chat
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> = _chats.asStateFlow()

    init {
        loadChats()
    }

    private fun loadChats() {
        viewModelScope.launch {
            try {
                // Fetch chats using Postgrest
                val result = SupabaseClient.client.postgrest["chats"]
                    .select().decodeList<Chat>()
                _chats.value = result
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val chats by viewModel.chats.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("NHAPP") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(chats) { chat ->
                ListItem(
                    headlineContent = { Text("Chat ID: ${chat.id.take(8)}") },
                    modifier = Modifier.clickable {
                        navController.navigate("chat/${chat.id}")
                    }
                )
                Divider()
            }
        }
    }
}
