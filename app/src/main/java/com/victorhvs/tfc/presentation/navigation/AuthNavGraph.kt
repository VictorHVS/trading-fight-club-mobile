package com.victorhvs.tfc.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.victorhvs.tfc.presentation.screens.auth.AuthScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreen.Login.route
    ) {
        composable(route = AuthScreen.Login.route) {
            AuthScreen(
                navigateToHome = {
                    navController.popBackStack()
                    navController.navigate(Graph.HOME)
                }
            )
        }
//        composable(route = AuthScreen.SignUp.route) {
//        }
//        composable(route = AuthScreen.Forgot.route) {
//        }
    }
}

sealed class AuthScreen(val route: String) {
    object Login : AuthScreen(route = "LOGIN")
//    object SignUp : AuthScreen(route = "SIGN_UP")
//    object Forgot : AuthScreen(route = "FORGOT")
}