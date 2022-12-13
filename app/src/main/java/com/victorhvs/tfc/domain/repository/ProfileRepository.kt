package com.victorhvs.tfc.domain.repository

import com.victorhvs.tfc.core.Resource

typealias SignOutResponse = Resource<Boolean>

interface ProfileRepository {
    suspend fun signOut(): SignOutResponse
}