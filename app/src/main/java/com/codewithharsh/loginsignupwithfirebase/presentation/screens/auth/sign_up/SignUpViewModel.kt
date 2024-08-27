package com.codewithharsh.loginsignupwithfirebase.presentation.screens.auth.sign_up

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithharsh.loginsignupwithfirebase.common.Resource
import com.codewithharsh.loginsignupwithfirebase.domain.model.User
import com.codewithharsh.loginsignupwithfirebase.domain.use_cases.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {
    // SignUp --------------------------------------------------------------------------------------
    private val _textFieldState = mutableStateOf(SignUpTextFieldState())
    val textFieldState: State<SignUpTextFieldState> = _textFieldState

    private val _signUpScreenState = MutableStateFlow(SignUpScreenState())
    val signUpScreenState: StateFlow<SignUpScreenState> = _signUpScreenState

    private val _uiEvent = MutableSharedFlow<SignUpUiEvent>()
    val uiEvent: SharedFlow<SignUpUiEvent> = _uiEvent

    fun signUp(user: User, imageUri: Uri?) {
        viewModelScope.launch {
            if (imageUri != null) {
                authUseCase.uploadProfilePicture(imageUri).collect { resource ->
                    when (resource) {
                        is Resource.Error -> {
                            _signUpScreenState.value =
                                SignUpScreenState(error = resource.message, isLoading = false)
                        }

                        Resource.Loading -> {
                            _signUpScreenState.value = SignUpScreenState(isLoading = true)
                        }

                        is Resource.Success -> {
                            _signUpScreenState.value = SignUpScreenState(isSignUpSuccessful = true, isLoading = false)
                            val userWithImageUrl = user.copy(profileImageUri = resource.data)
                            saveUser(userWithImageUrl)
                        }
                    }
                }
            }
        }
    }

    private fun saveUser(user: User) {
        viewModelScope.launch {
            authUseCase.signUpUseCase(user).collect { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _signUpScreenState.value =
                            SignUpScreenState(error = resource.message, isLoading = false)
                        _uiEvent.emit(SignUpUiEvent.ShowToast("SignUp Failed : ${resource.message}"))
                    }

                    Resource.Loading -> {
                        _signUpScreenState.value = SignUpScreenState(isLoading = true)
                    }

                    is Resource.Success -> {
                        _signUpScreenState.value = SignUpScreenState(isSignUpSuccessful = true)
                        _uiEvent.emit(SignUpUiEvent.ShowDialog("SignUp Successful"))
                    }
                }
            }
        }
    }

    fun OnSignUpEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.FirstNameChanged -> {
                _textFieldState.value = _textFieldState.value.copy(
                    firstName = event.firstName
                )
            }

            is SignUpEvent.LastNameChanged -> {
                _textFieldState.value = _textFieldState.value.copy(
                    lastName = event.lastName
                )
            }

            is SignUpEvent.EmailChanged -> {
                _textFieldState.value = _textFieldState.value.copy(
                    email = event.email
                )
            }

            is SignUpEvent.AgeChanged -> {
                _textFieldState.value = _textFieldState.value.copy(
                    age = event.age
                )
            }

            is SignUpEvent.PasswordChanged -> {
                _textFieldState.value = _textFieldState.value.copy(
                    password = event.password
                )
            }

            is SignUpEvent.ConfirmPasswordChanged -> {
                _textFieldState.value = _textFieldState.value.copy(
                    confirmPassword = event.confirmPassword
                )
            }

        }
    }
}

sealed class SignUpUiEvent {
    data class ShowToast(val message: String) : SignUpUiEvent()
    data class ShowDialog(val message: String) : SignUpUiEvent()
}