package com.codewithharsh.loginsignupwithfirebase.presentation.screens.auth.sign_up

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.TextFormat
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.codewithharsh.loginsignupwithfirebase.R
import com.codewithharsh.loginsignupwithfirebase.domain.model.User
import com.codewithharsh.loginsignupwithfirebase.presentation.auth_navigation.Routes
import com.codewithharsh.loginsignupwithfirebase.ui.theme.Green1
import com.codewithharsh.loginsignupwithfirebase.ui.theme.Pink

//@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SignUpScreen(
    navController: NavController,
    signUpViewModel: SignUpViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val screenState by signUpViewModel.signUpScreenState.collectAsStateWithLifecycle()
    val textFieldState by signUpViewModel.textFieldState

    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisibility by rememberSaveable { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
    }

    LaunchedEffect(key1 = Unit) {
        signUpViewModel.uiEvent.collect { event ->
            when (event) {
                is SignUpUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is SignUpUiEvent.ShowDialog -> {
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
                onDismissRequest = { showDialog = false },
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
                            text = "Congratulations😊 You have successfully signed up",
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
                            navController.navigate(Routes.SignIn) {
                                popUpTo(0)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Pink)
                    ) {
                        Text("SIGN-IN NOW")
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
            verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.SansSerif
            )

            if (imageUri == null) {
                Image(
                    painter = painterResource(R.drawable.default_image),
                    contentDescription = "Default Image",
                    modifier = Modifier
                        .size(150.dp)
                        .clickable {
                            launcher.launch("image/*")
                        }
                )
            }else{
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(180.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.02f)),
                    contentScale = ContentScale.Fit
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(0.9f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = textFieldState.firstName,
                    onValueChange = { firstName ->
                        signUpViewModel.OnSignUpEvent(SignUpEvent.FirstNameChanged(firstName = firstName))
                    },
                    modifier = Modifier.weight(1f),
                    label = {
                        Text(text = "First Name")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.TextFormat,
                            contentDescription = "Email"
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    maxLines = 1,
                )

                OutlinedTextField(
                    value = textFieldState.lastName,
                    onValueChange = { lastName ->
                        signUpViewModel.OnSignUpEvent(SignUpEvent.LastNameChanged(lastName = lastName))
                    },
                    modifier = Modifier.weight(1f),
                    label = {
                        Text(text = "Last Name")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.TextFormat,
                            contentDescription = "Email"
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    maxLines = 1,
                )
            }
            OutlinedTextField(
                value = textFieldState.age,
                onValueChange = { age ->
                    signUpViewModel.OnSignUpEvent(SignUpEvent.AgeChanged(age = age))
                },
                modifier = Modifier.fillMaxWidth(0.9f),
                label = {
                    Text(text = "Your Age")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Numbers,
                        contentDescription = "Number Icon"
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                maxLines = 1,
            )

            OutlinedTextField(
                value = textFieldState.email,
                onValueChange = { email ->
                    signUpViewModel.OnSignUpEvent(SignUpEvent.EmailChanged(email = email))
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
                    signUpViewModel.OnSignUpEvent(SignUpEvent.PasswordChanged(password = it))
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
                    val image =
                        if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description =
                        if (passwordVisibility) "Hide Password Icon" else "Show Password Icon"
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(
                            imageVector = image,
                            contentDescription = description
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                maxLines = 1,
            )

            OutlinedTextField(
                value = textFieldState.confirmPassword,
                onValueChange = {
                    signUpViewModel.OnSignUpEvent(
                        SignUpEvent.ConfirmPasswordChanged(
                            confirmPassword = it
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(0.9f),
                label = {
                    Text(text = "Confirm Password")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Password,
                        contentDescription = "Password Icon"
                    )
                },
                visualTransformation = if (confirmPasswordVisibility)
                    VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image =
                        if (confirmPasswordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description =
                        if (confirmPasswordVisibility) "Hide Password Icon" else "Show Password Icon"
                    IconButton(onClick = {
                        confirmPasswordVisibility = !confirmPasswordVisibility
                    }) {
                        Icon(
                            imageVector = image,
                            contentDescription = description
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                maxLines = 1,
            )

            Button(
                onClick = {
                    when {
                        textFieldState.firstName.isBlank() -> {
                            Toast.makeText(
                                context,
                                "First Name cannot be empty",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        textFieldState.lastName.isBlank() -> {
                            Toast.makeText(
                                context,
                                "Last Name  cannot be empty",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        textFieldState.age.isBlank() -> {
                            Toast.makeText(context, "Age cannot be empty", Toast.LENGTH_SHORT)
                                .show()
                        }

                        textFieldState.email.isBlank() -> {
                            Toast.makeText(context, "Email cannot be empty", Toast.LENGTH_SHORT)
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

                        textFieldState.confirmPassword.isBlank() -> {
                            Toast.makeText(
                                context,
                                "Confirm cannot be empty",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                        textFieldState.password != textFieldState.confirmPassword -> {
                            Toast.makeText(
                                context,
                                "Password and Confirm Password do not match",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                            val name = "${textFieldState.firstName} ${textFieldState.lastName}"
                            val user = User(
                                name = name,
                                age = textFieldState.age.toInt(),
                                email = textFieldState.email,
                                password = textFieldState.password,
                                profileImageUri = imageUri.toString()

                            )
                            signUpViewModel.signUp(user, imageUri = imageUri)
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
                    text = "Already Have an Account? ",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.SansSerif
                )
                Text(
                    text = "Sign In",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.SansSerif,
                    modifier = Modifier.clickable {
                        navController.navigate(Routes.SignIn) {
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