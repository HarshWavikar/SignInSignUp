package com.codewithharsh.loginsignupwithfirebase.presentation.screens.auth.sign_in

data class SignInScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSignInSuccessful: Boolean = false,
)
