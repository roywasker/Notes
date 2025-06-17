package com.example.notesmoveo.screen.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.notesmoveo.screen.Login.LoginScreen
import com.example.notesmoveo.screen.Signup.SignupScreen
import com.example.notesmoveo.screen.Home.HomeScreen
import com.example.notesmoveo.screen.Note.NoteScreen

@Composable
fun SetupNavGraph(startDestination: Screen = Screen.Login){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        composable<Screen.Login> {
            LoginScreen(
                navigateToSignup = {
                    navController.navigate(Screen.Signup){
                        popUpTo<Screen.Login>{ inclusive = false}
                    }
                },
                navigateToHome = {
                    navController.navigate(Screen.Home){
                        popUpTo<Screen.Login>{ inclusive = true}
                    }
                }
            )
        }
        composable<Screen.Signup> {
            SignupScreen(
                navigateToLogin = {
                    navController.navigate(Screen.Login){
                        launchSingleTop = true
                        popUpTo<Screen.Signup>{ inclusive = true}
                    }
                },
                navigateToHome = {
                    navController.navigate(Screen.Home){
                        popUpTo<Screen.Signup>{ inclusive = true}
                    }
                }
            )
        }
        composable<Screen.Home> {
            HomeScreen(
                navigateToLogin = {
                    navController.navigate(Screen.Login){
                        popUpTo<Screen.Home>{ inclusive = true}
                    }
                },
                navigateToNote = { id ->
                    navController.navigate(Screen.Note(id)){
                        launchSingleTop = true
                    }
                },
                rootNavController = navController
            )
        }
        composable<Screen.Note> {
            val id = it.toRoute<Screen.Note>().id
            NoteScreen(
                id = id,
                navController = navController
            )
        }
    }
}