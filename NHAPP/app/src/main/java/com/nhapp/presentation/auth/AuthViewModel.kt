package com.nhapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nhapp.data.remote.SupabaseClient
import io.github.jan.supabase.gotrue.OtpType
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.OTP
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object OtpSent : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun sendOtp(email: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                SupabaseClient.client.auth.signInWith(OTP) {
                    this.email = email
                    this.createUser = true
                }
                _authState.value = AuthState.OtpSent
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Failed to send OTP")
            }
        }
    }

    fun verifyOtp(email: String, otp: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                SupabaseClient.client.auth.verifyEmailOtp(
                    type = OtpType.Email.MAGIC_LINK,
                    email = email,
                    token = otp
                )
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Failed to verify OTP")
            }
        }
    }
}
