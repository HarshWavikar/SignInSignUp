package com.codewithharsh.loginsignupwithfirebase.domain.repository

import android.net.Uri
import com.codewithharsh.loginsignupwithfirebase.common.Resource
import com.codewithharsh.loginsignupwithfirebase.domain.model.User
import com.codewithharsh.loginsignupwithfirebase.domain.model.UserNode
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun signUpWithEmailAndPassword(user: User): Flow<Resource<String>>
    fun signInWithEmailAndPassword(email: String, password: String): Flow<Resource<String>>
    fun getUserByUid(uid: String): Flow<Resource<UserNode>>
    fun getAllUsers(): Flow<Resource<List<User>>>
    fun uploadProfileImage(imageUri: Uri): Flow<Resource<String>>
}