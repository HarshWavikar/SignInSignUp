package com.codewithharsh.loginsignupwithfirebase.domain.use_cases

data class AuthUseCase(
    val signUpUseCase: SignUpUseCase,
    val signInUseCase: SignInUseCase,
    val getAllUsers: GetAllUsers,
    val uploadProfilePicture: UploadProfilePicture
)
