package com.codewithharsh.loginsignupwithfirebase.domain.use_cases

import com.codewithharsh.loginsignupwithfirebase.common.Resource
import com.codewithharsh.loginsignupwithfirebase.domain.model.User
import com.codewithharsh.loginsignupwithfirebase.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
  private val repository: AuthRepository
)  {
    operator fun invoke(user: User): Flow<Resource<String>> {
        return repository.signUpWithEmailAndPassword(user)
    }
}