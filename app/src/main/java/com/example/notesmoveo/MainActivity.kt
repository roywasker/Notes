package com.example.notesmoveo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import com.example.notesmoveo.domain.repository.AuthRepository
import com.example.notesmoveo.screen.navigation.Screen
import com.example.notesmoveo.screen.navigation.SetupNavGraph
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val authRepository = koinInject<AuthRepository>()

            val isUserAuthenticated = remember { authRepository.getCurrentUserId() != null }
            val startDestination = remember {
                if (isUserAuthenticated){
                    Screen.Home
                }else{
                    Screen.Login
                }
            }

            SetupNavGraph(
                startDestination = startDestination
            )
        }
    }
}