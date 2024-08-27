package com.codewithharsh.loginsignupwithfirebase.presentation.screens.auth.sign_in

data class SignInTextFieldState(
    val email: String = "",
    val password: String = ""
)

sealed class SignInEvents{
    data class EmailChanged(val email: String): SignInEvents()
    data class PasswordChanged(val password: String): SignInEvents()
}