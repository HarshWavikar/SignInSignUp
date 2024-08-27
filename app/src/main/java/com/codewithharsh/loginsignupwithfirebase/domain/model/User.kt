package com.codewithharsh.loginsignupwithfirebase.domain.model

data class User(
    val name : String = "",
    val email: String = "",
    val password: String = "",
    val age: Int = 0,
    val profileImageUri: String = ""
)

data class UserNode(
    val nodeId: String = "",
    val user: User? = null,
)
