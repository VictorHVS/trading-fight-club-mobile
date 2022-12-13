package com.victorhvs.tfc.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.victorhvs.tfc.core.Resource
import com.victorhvs.tfc.domain.repository.AuthRepository
import com.victorhvs.tfc.domain.repository.SignInResponse
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl  @Inject constructor(
    private val auth: FirebaseAuth
): AuthRepository {
    override val isUserAuthenticatedInFirebase = auth.currentUser != null

    override suspend fun firebaseSignInAnonymously(): SignInResponse {
        return try {
            auth.signInAnonymously().await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }
}