package com.codewithharsh.loginsignupwithfirebase.presentation.screens.auth.sign_in

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.codewithharsh.loginsignupwithfirebase.presentation.auth_navigation.AppNavigation
import com.codewithharsh.loginsignupwithfirebase.presentation.auth_navigation.Routes
import com.codewithharsh.loginsignupwithfirebase.ui.theme.Green1
import com.codewithharsh.loginsignupwithfirebase.ui.theme.Pink


@Composable
fun SignInScreen(
    navController: NavController,
    signInViewModel: SignInViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val screenState by signInViewModel.signInScreenState.collectAsStateWithLifecycle()
    val textFieldState by signInViewModel.textFieldState

    var passwordVisibility by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    LaunchedEffect(key1 = Unit) {
        signInViewModel.uiEvent.collect { event ->
            when (event) {
                is SignInUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is SignInUiEvent.ShowDialog -> {
                    dialogMessage = event.message
                    showDialog = true
                }
            }
        }
    }

    if (showDialog) {
        AnimatedVisibility(
            visible = showDialog,
            enter = fadeIn(animationSpec = tween(durationMillis = 300)),
            exit = fadeOut(animationSpec = tween(durationMillis = 300))
        ) {
            AlertDialog(
                onDismissRequest = { showDialog = true },
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = dialogMessage,
                        color = Green1,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Congratulations😊 You have successfully Signed In",
                            modifier = Modifier.fillMaxWidth(),
                            color = Green1,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                confirmButton = {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            showDialog = false
                            navController.navigate(AppNavigation.InAppScreen) {
                                popUpTo(0)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Pink)
                    ) {
                        Text("Let's Go")
                    }
                }
            )
        }
    }

    if (screenState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    if (screenState.error.toString().isBlank()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = screenState.error.toString()
            )
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {

            Text(
                text = "Sign In",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.SansSerif
            )
            OutlinedTextField(
                value = textFieldState.email,
                onValueChange = {
                    signInViewModel.onSignInEvent(SignInEvents.EmailChanged(it))
                },
                modifier = Modifier.fillMaxWidth(0.9f),
                label = {
                    Text(text = "Email Address")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email"
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                maxLines = 1,
            )

            OutlinedTextField(
                value = textFieldState.password,
                onValueChange = {
                    signInViewModel.onSignInEvent(SignInEvents.PasswordChanged(it))
                },
                modifier = Modifier.fillMaxWidth(0.9f),
                label = {
                    Text(text = "Password")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Password,
                        contentDescription = "Password Icon"
                    )
                },
                visualTransformation = if (passwordVisibility)
                    VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisibility)
                        Icons.Default.Visibility else Icons.Default.VisibilityOff
                    val descriptor = if (passwordVisibility)
                        "Hide Password Icon" else "Show Password Icon"
                    IconButton(
                        onClick = {
                            passwordVisibility = !passwordVisibility
                        }
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = descriptor
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                maxLines = 1,
            )

            Button(
                onClick = {
                    when {
                        textFieldState.email.isBlank() -> {
                            Toast.makeText(
                                context,
                                "Email cannot be empty",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                        textFieldState.password.isBlank() -> {
                            Toast.makeText(
                                context,
                                "Password cannot be empty",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                        else -> {
                            signInViewModel.signIn(
                                textFieldState.email,
                                textFieldState.password
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                Text(
                    text = "Sign Up"
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account? ",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.SansSerif
                )
                Text(
                    text = "Sign Up",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.SansSerif,
                    modifier = Modifier.clickable {
                        navController.navigate(Routes.SignUp) {
                            popUpTo(0)
                        }
                    }
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "*** Developed By ***",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.SansSerif
                )
                Text(
                    text = "Harsh Wavikar",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.SansSerif,
                    modifier = Modifier.clickable {
                    }.offset(y = (-8).dp)
                )
            }
        }
    }
}