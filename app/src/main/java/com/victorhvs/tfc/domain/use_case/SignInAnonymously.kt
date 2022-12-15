package com.victorhvs.tfc.domain.use_case

import com.victorhvs.tfc.domain.repository.AuthRepository

class SignInAnonymously(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() = repository.firebaseSignInAnonymously()
}