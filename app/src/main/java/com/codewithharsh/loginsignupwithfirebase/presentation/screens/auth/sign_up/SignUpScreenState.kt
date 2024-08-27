package com.codewithharsh.loginsignupwithfirebase.presentation.screens.auth.sign_up

data class SignUpScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSignUpSuccessful: Boolean = false
)
