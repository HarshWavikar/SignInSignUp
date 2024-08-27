package com.codewithharsh.loginsignupwithfirebase.domain.use_cases

import com.codewithharsh.loginsignupwithfirebase.common.Resource
import com.codewithharsh.loginsignupwithfirebase.domain.model.User
import com.codewithharsh.loginsignupwithfirebase.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllUsers @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<Resource<List<User>>> {
        return repository.getAllUsers()
    }
}