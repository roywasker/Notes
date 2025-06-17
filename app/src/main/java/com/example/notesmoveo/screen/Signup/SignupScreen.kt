package com.example.notesmoveo.screen.Signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.example.notesmoveo.screen.component.CustomTextField
import com.example.notesmoveo.screen.component.CustomButton
import com.example.notesmoveo.ui.theme.Orange
import com.example.notesmoveo.utils.Resources
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignupScreen(
    navigateToLogin: () -> Unit,
    navigateToHome: () -> Unit
){

    val viewModel = koinViewModel<SighupViewModel>()
    val isFormValid = viewModel.isFormValid
    val signupState = viewModel.signupState
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    if (isLoading) {
        Popup(
            alignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Orange)
        }
    }

    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            delay(3000)
            errorMessage = null
        }
    }
    Scaffold(
        bottomBar = {
            CustomButton(
                text = Resources.Register.buttonText,
                enable = isFormValid,
                onClick = {
                    isLoading = true
                    viewModel.register(
                        onSuccess = {
                            isLoading = false
                            navigateToHome()
                        },
                        onError = { message ->
                            isLoading = false
                            errorMessage = message
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .padding(bottom = 20.dp)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            errorMessage?.let {
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 50.dp),
                    containerColor = Color.Gray,
                ) {
                    Text(text = errorMessage!!, color = Color.White)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 90.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = Resources.Register.createAccount,
                    textAlign = TextAlign.Center,
                    fontSize = 46.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Orange
                )

                Spacer(modifier = Modifier.height(36.dp))

                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(24.dp),

                    ) {
                    CustomTextField(
                        value = signupState.email,
                        onValueChange = viewModel::updateEmail,
                        placeholder = Resources.Register.email,
                    )

                    CustomTextField(
                        value = signupState.password,
                        onValueChange = viewModel::updatePassword,
                        placeholder = Resources.Register.password,
                        isPassword = true
                    )
                    CustomTextField(
                        value = signupState.firstName,
                        onValueChange = viewModel::updateFirstName,
                        placeholder = Resources.Register.firstName,
                    )

                    CustomTextField(
                        value = signupState.lastName,
                        onValueChange = viewModel::updateLastName,
                        placeholder = Resources.Register.lastName,
                    )

                    TextButton(
                        onClick = navigateToLogin
                    ) {
                        Text(
                            text = Resources.Register.toLogin,
                            fontSize = 16.sp,
                            color = Color.Black.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}