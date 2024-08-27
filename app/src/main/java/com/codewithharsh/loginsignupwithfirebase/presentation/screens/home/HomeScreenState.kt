package com.codewithharsh.loginsignupwithfirebase.presentation.screens.home

import com.codewithharsh.loginsignupwithfirebase.domain.model.User

data class HomeScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val users: List<User> = emptyList()
)
