package com.victorhvs.tfc.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.victorhvs.tfc.core.Resource
import com.victorhvs.tfc.domain.repository.ProfileRepository
import com.victorhvs.tfc.domain.repository.SignOutResponse
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl  @Inject constructor(
    private val auth: FirebaseAuth
): ProfileRepository {
    override suspend fun signOut(): SignOutResponse {
        return try {
            auth.currentUser?.delete()?.await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }
}