package com.example.notesmoveo.screen.Login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesmoveo.data.model.LoginUser
import com.example.notesmoveo.domain.repository.AuthRepository
import kotlinx.coroutines.launch


class LoginViewModel(
    private val authRepository: AuthRepository
): ViewModel() {

    var loginState: LoginUser by mutableStateOf(LoginUser())
        private set

    val isFormValid: Boolean
        get() = with(loginState){
            val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
                    email.isNotBlank() &&
                    emailRegex.matches(email) &&
                    password.length >= 6
        }

    fun updateEmail(value: String) {
        loginState = loginState.copy(email = value)
    }

    fun updatePassword(value: String) {
        loginState = loginState.copy(password = value)
    }

    fun login(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        viewModelScope.launch {
            authRepository.login(
                email = loginState.email,
                password = loginState.password,
                onSuccess = onSuccess,
                onError = onError
            )
        }
    }
}