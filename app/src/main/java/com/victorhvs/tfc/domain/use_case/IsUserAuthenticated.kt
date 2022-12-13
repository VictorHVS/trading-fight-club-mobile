package com.victorhvs.tfc.domain.use_case

import com.victorhvs.tfc.domain.repository.AuthRepository

class IsUserAuthenticated(
    private val repository: AuthRepository
) {
    operator fun invoke() = repository.isUserAuthenticatedInFirebase
}