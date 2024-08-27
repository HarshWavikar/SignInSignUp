package com.codewithharsh.loginsignupwithfirebase.domain.use_cases

import android.net.Uri
import com.codewithharsh.loginsignupwithfirebase.common.Resource
import com.codewithharsh.loginsignupwithfirebase.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UploadProfilePicture @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(imageUri: Uri): Flow<Resource<String>> {
        return repository.uploadProfileImage(imageUri)
    }
}