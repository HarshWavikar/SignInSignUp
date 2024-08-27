package com.codewithharsh.loginsignupwithfirebase.domain.use_cases

import com.codewithharsh.loginsignupwithfirebase.common.Resource
import com.codewithharsh.loginsignupwithfirebase.domain.repository.AuthRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class SignInUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(email: String, password: String):Flow<Resource<String>> {
        return repository.signInWithEmailAndPassword(email, password)
    }
}