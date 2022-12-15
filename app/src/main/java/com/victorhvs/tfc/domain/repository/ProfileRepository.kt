package com.victorhvs.tfc.domain.repository

import com.victorhvs.tfc.core.Resource
import com.victorhvs.tfc.domain.enums.FirestoreState
import com.victorhvs.tfc.domain.models.Stock
import com.victorhvs.tfc.domain.models.User
import kotlinx.coroutines.flow.Flow

typealias SignOutResponse = Resource<Boolean>

interface ProfileRepository {
    suspend fun signOut(): SignOutResponse

    suspend fun currentUser(): Flow<FirestoreState<User?>>

//    fun orderHistory(): Flow<FirestoreState<List<>>>
}