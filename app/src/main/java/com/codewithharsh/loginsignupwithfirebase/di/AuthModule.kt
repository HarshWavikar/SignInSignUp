package com.codewithharsh.loginsignupwithfirebase.di

import com.codewithharsh.loginsignupwithfirebase.data.repository.AuthRepositoryImpl
import com.codewithharsh.loginsignupwithfirebase.domain.repository.AuthRepository
import com.codewithharsh.loginsignupwithfirebase.domain.use_cases.AuthUseCase
import com.codewithharsh.loginsignupwithfirebase.domain.use_cases.GetAllUsers
import com.codewithharsh.loginsignupwithfirebase.domain.use_cases.SignInUseCase
import com.codewithharsh.loginsignupwithfirebase.domain.use_cases.SignUpUseCase
import com.codewithharsh.loginsignupwithfirebase.domain.use_cases.UploadProfilePicture
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AuthModule {

    @Provides
    @Singleton
    fun providesFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun providesFirebaseFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun providesFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun providesAuthRepository(
        auth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore,
        firebaseStorage: FirebaseStorage
    ): AuthRepository {
        return AuthRepositoryImpl(auth, firebaseFirestore, firebaseStorage)
    }

    @Provides
    @Singleton
    fun providesAuthUseCase(authRepository: AuthRepository): AuthUseCase {
        return AuthUseCase(
            signUpUseCase = SignUpUseCase(repository = authRepository),
            signInUseCase = SignInUseCase(repository= authRepository),
            getAllUsers = GetAllUsers(repository = authRepository),
            uploadProfilePicture = UploadProfilePicture(repository = authRepository)
        )
    }
}