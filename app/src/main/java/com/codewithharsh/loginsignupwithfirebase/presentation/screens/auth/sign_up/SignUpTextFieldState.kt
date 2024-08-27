package com.codewithharsh.loginsignupwithfirebase.presentation.screens.auth.sign_up

data class SignUpTextFieldState(
    val firstName: String = "",
    val lastName: String = "",
    val age: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
)

sealed class SignUpEvent{
    data class FirstNameChanged(val firstName: String): SignUpEvent()
    data class LastNameChanged(val lastName: String): SignUpEvent()
    data class AgeChanged(val age: String): SignUpEvent()
    data class EmailChanged(val email: String): SignUpEvent()
    data class PasswordChanged(val password: String): SignUpEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String): SignUpEvent()
}