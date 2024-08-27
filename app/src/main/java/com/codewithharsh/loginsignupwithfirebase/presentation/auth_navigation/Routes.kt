package com.codewithharsh.loginsignupwithfirebase.presentation.auth_navigation

import kotlinx.serialization.Serializable

sealed class AppNavigation{
    @Serializable
    data object AuthScreen : AppNavigation()

    @Serializable
    data object InAppScreen : AppNavigation()
}

sealed class Routes {
    @Serializable
    data object SignIn : Routes()

    @Serializable
    data object SignUp : Routes()

    @Serializable
    data object InApp : Routes()
}