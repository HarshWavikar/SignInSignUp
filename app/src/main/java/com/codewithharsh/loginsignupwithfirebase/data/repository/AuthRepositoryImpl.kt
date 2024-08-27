package com.codewithharsh.loginsignupwithfirebase.data.repository

import android.net.Uri
import android.util.Log
import com.codewithharsh.loginsignupwithfirebase.common.Resource
import com.codewithharsh.loginsignupwithfirebase.domain.model.User
import com.codewithharsh.loginsignupwithfirebase.domain.model.UserNode
import com.codewithharsh.loginsignupwithfirebase.domain.repository.AuthRepository
import com.google.api.ResourceProto.resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : AuthRepository {
    override fun signUpWithEmailAndPassword(user: User): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading)
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(Resource.Success("User Created Successfully")).isSuccess
                    firebaseFirestore.collection("USERS").document(task.result.user!!.uid)
                        .set(user).addOnCompleteListener {
                            if (it.isSuccessful) {
                                trySend(Resource.Success("User Created Successfully")).isSuccess
                            } else {
                                val errorMessage =
                                    it.exception?.message ?: "Unable to save user"
                                trySend(Resource.Error(errorMessage)).isSuccess
                            }
                        }
                } else {
                    trySend(Resource.Error(task.exception?.message.toString()))
                }
                close()
            }
        awaitClose {
            close()
        }
    }

    override fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Resource<String>> =
        callbackFlow {
            trySend(Resource.Loading)
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(Resource.Success("User Signed In Successfully")).isSuccess
                } else {
                    val errorMessage = task.exception?.message ?: "Unable to sign in user"
                    trySend(Resource.Error(errorMessage)).isSuccess
                }
                close()
            }
            awaitClose {
                close()
            }
        }

    override fun getUserByUid(uid: String): Flow<Resource<UserNode>> {
        TODO("Not yet implemented")
    }

    override fun getAllUsers(): Flow<Resource<List<User>>> = callbackFlow {
        trySend(Resource.Loading)

        firebaseFirestore.collection("USERS")
            .get()
            .addOnSuccessListener {
                val users = it.toObjects(User::class.java)
                trySend(Resource.Success(users)).isSuccess
            }.addOnFailureListener {
                trySend(Resource.Error(it.message.toString()))
            }
        awaitClose {
            close()
        }
    }

    override fun uploadProfileImage(imageUri: Uri): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading)
        val storageRef = storage.reference.child("profile_images/${UUID.randomUUID()}")
        val upload = storageRef.putFile(imageUri)

        upload.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener {
                trySend(Resource.Success(it.toString()))
            }.addOnFailureListener {
                trySend(Resource.Error(it.message.toString()))
            }
        }.addOnFailureListener{
            val errorMessage = it.message ?: "Unable to upload image"
            trySend(Resource.Error(errorMessage))
        }
        awaitClose {
            close()
        }
    }
}
