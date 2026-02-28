package com.nhapp.presentation.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nhapp.data.remote.SupabaseClient
import com.nhapp.domain.model.Message
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.decodeRecord
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID

class ChatViewModel(private val chatId: String) : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val currentUserId: String
        get() = SupabaseClient.client.auth.currentUserOrNull()?.id ?: ""

    init {
        loadMessages()
        subscribeToMessages()
    }

    private fun loadMessages() {
        viewModelScope.launch {
            try {
                val result = SupabaseClient.client.postgrest["messages"]
                    .select {
                        filter { eq("chat_id", chatId) }
                        order("created_at", Order.ASCENDING)
                    }.decodeList<Message>()
                _messages.value = result
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun subscribeToMessages() {
        viewModelScope.launch {
            val channel = SupabaseClient.client.realtime.channel("public:messages:$chatId")
            
            val messageFlow = channel.postgresChangeFlow<PostgresAction.Insert>(schema = "public") {
                table = "messages"
                filter = "chat_id=eq.$chatId"
            }

            channel.subscribe()

            messageFlow.collect { action ->
                val newMessage = action.decodeRecord<Message>()
                _messages.value = _messages.value + newMessage
            }
        }
    }

    fun sendMessage(content: String) {
        val userId = currentUserId
        if (content.isBlank() || userId.isEmpty()) return
        viewModelScope.launch {
            try {
                val newMessage = Message(
                    id = UUID.randomUUID().toString(),
                    chatId = chatId,
                    senderId = userId,
                    content = content,
                    createdAt = Instant.now().toString()
                )
                SupabaseClient.client.postgrest["messages"].insert(newMessage)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class ChatViewModelFactory(private val chatId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatViewModel(chatId) as T
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatId: String,
    viewModel: ChatViewModel = viewModel(factory = ChatViewModelFactory(chatId))
) {
    val messages by viewModel.messages.collectAsState()
    var currentMessage by remember { mutableStateOf("") }
    val currentUserId = SupabaseClient.client.auth.currentUserOrNull()?.id
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chat") }
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = currentMessage,
                        onValueChange = { currentMessage = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Message") },
                        maxLines = 4
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (currentMessage.isNotBlank()) {
                                viewModel.sendMessage(currentMessage)
                                currentMessage = ""
                            }
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Icon(Icons.Filled.Send, contentDescription = "Send")
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(messages) { message ->
                val isMine = message.senderId == currentUserId
                ChatBubble(message = message, isMine = isMine)
            }
        }
    }
}

@Composable
fun ChatBubble(message: Message, isMine: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            color = if (isMine) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = message.content,
                    color = if (isMine) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
