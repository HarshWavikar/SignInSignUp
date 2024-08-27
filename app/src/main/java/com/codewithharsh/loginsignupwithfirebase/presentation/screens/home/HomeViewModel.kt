package com.codewithharsh.loginsignupwithfirebase.presentation.screens.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithharsh.loginsignupwithfirebase.common.Resource
import com.codewithharsh.loginsignupwithfirebase.domain.model.User
import com.codewithharsh.loginsignupwithfirebase.domain.use_cases.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState: StateFlow<HomeScreenState> = _homeScreenState

    private val _users = mutableStateOf<List<User>>(emptyList())
    val users: State<List<User>> = _users

    init {
        getAllUsers()
    }

    fun getAllUsers() {
        viewModelScope.launch {
            authUseCase.getAllUsers().collectLatest { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _homeScreenState.value =
                            HomeScreenState(error = resource.message, isLoading = false)
                    }

                    Resource.Loading -> {
                        _homeScreenState.value = HomeScreenState(isLoading = true)
                    }

                    is Resource.Success -> {
                        _homeScreenState.value =
                            HomeScreenState(users = resource.data, isLoading = false)
                        _users.value = resource.data
                    }
                }
            }
        }
    }
}

