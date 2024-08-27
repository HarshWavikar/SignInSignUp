package com.codewithharsh.loginsignupwithfirebase.presentation.auth_navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.codewithharsh.loginsignupwithfirebase.presentation.screens.auth.sign_in.SignInScreen
import com.codewithharsh.loginsignupwithfirebase.presentation.screens.auth.sign_up.SignUpScreen
import com.codewithharsh.loginsignupwithfirebase.presentation.screens.home.HomeScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavGraph(
firebaseAuth: FirebaseAuth
) {
    val navController = rememberNavController()

    val startDestination = if (firebaseAuth.currentUser == null) {
        AppNavigation.AuthScreen
    } else {
        AppNavigation.InAppScreen
    }

    NavHost(navController = navController, startDestination = startDestination) {
        navigation<AppNavigation.AuthScreen>(startDestination = Routes.SignIn) {
            composable<Routes.SignUp> {
                SignUpScreen(navController= navController)
            }
            composable<Routes.SignIn> {
                SignInScreen(navController = navController)
            }
        }
        navigation<AppNavigation.InAppScreen>(startDestination = Routes.InApp) {
            composable<Routes.InApp> {
                    HomeScreen()
            }
        }
    }
}