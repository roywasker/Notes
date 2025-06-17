package com.example.notesmoveo.screen.Signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesmoveo.data.model.SignupUser
import com.example.notesmoveo.domain.repository.AuthRepository
import kotlinx.coroutines.launch

class SighupViewModel (
    private val authRepository: AuthRepository
): ViewModel() {

    var signupState: SignupUser by mutableStateOf(SignupUser())
        private set

    val isFormValid: Boolean
        get() = with(signupState){
            val nameRegex = Regex("^[A-Za-z]+$")
            val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
                    firstName.length in 3..10 &&
                    lastName.length in 3..10 &&
                    nameRegex.matches(firstName) &&
                    nameRegex.matches(lastName) &&
                    email.isNotBlank() &&
                    emailRegex.matches(email) &&
                    password.length >= 6
        }

    fun updateEmail(value: String) {
        signupState = signupState.copy(email = value)
    }

    fun updatePassword(value: String) {
        signupState = signupState.copy(password = value)
    }

    fun updateFirstName(value: String) {
        if (value.all { it.isLetter() }) {
            signupState = signupState.copy(firstName = value)
        }
    }

    fun updateLastName(value: String) {
        if (value.all { it.isLetter() }) {
            signupState = signupState.copy(lastName = value)
        }
    }

    fun register(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        viewModelScope.launch {
            authRepository.register(
                email = signupState.email,
                password = signupState.password,
                firstName = signupState.firstName,
                lastName = signupState.lastName,
                onSuccess = onSuccess,
                onError = onError
            )
        }
    }
}