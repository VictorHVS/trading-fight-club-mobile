package com.victorhvs.tfc.domain.repository

import com.victorhvs.tfc.core.Resource

typealias SignInResponse = Resource<Boolean>

interface AuthRepository {
    val isUserAuthenticatedInFirebase: Boolean

    suspend fun firebaseSignInAnonymously(): SignInResponse
}