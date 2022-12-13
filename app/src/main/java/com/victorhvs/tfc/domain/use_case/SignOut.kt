package com.victorhvs.tfc.domain.use_case

import com.victorhvs.tfc.domain.repository.ProfileRepository

class SignOut(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke() = repository.signOut()
}