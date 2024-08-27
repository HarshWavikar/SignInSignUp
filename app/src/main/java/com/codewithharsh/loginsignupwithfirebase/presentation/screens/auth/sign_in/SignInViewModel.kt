package com.codewithharsh.loginsignupwithfirebase.presentation.screens.auth.sign_in

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithharsh.loginsignupwithfirebase.common.Resource
import com.codewithharsh.loginsignupwithfirebase.domain.use_cases.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _textFieldState = mutableStateOf(SignInTextFieldState())
    val textFieldState: State<SignInTextFieldState> = _textFieldState

    private val _signInScreenState = MutableStateFlow(SignInScreenState())
    val signInScreenState: StateFlow<SignInScreenState> = _signInScreenState

    private val _uiEvent = MutableSharedFlow<SignInUiEvent>()
    val uiEvent: SharedFlow<SignInUiEvent> = _uiEvent

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            authUseCase.signInUseCase(email, password).collect {
                when (it) {
                    is Resource.Error -> {
                        _signInScreenState.value =
                            SignInScreenState(error = it.message, isLoading = false)
                        _uiEvent.emit(SignInUiEvent.ShowToast("SignIn Failed : ${it.message}"))
                    }

                    Resource.Loading -> {
                        _signInScreenState.value = SignInScreenState(isLoading = true)
                    }

                    is Resource.Success -> {
                        _signInScreenState.value = SignInScreenState(isSignInSuccessful = true)
                        _uiEvent.emit(SignInUiEvent.ShowDialog("SignIn Successful"))
                    }
                }
            }
        }
    }

    fun onSignInEvent(event: SignInEvents) {
        when(event){
            is SignInEvents.EmailChanged -> {
                _textFieldState.value = _textFieldState.value.copy(
                    email = event.email
                )
            }
            is SignInEvents.PasswordChanged -> {
                _textFieldState.value = _textFieldState.value.copy(
                    password = event.password
                )
            }
        }
    }
}

sealed class SignInUiEvent {
    data class ShowToast(val message: String) : SignInUiEvent()
    data class ShowDialog(val message: String) : SignInUiEvent()
}
