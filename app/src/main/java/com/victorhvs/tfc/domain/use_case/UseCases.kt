package com.victorhvs.tfc.domain.use_case

data class UseCases(
    val isUserAuthenticated: IsUserAuthenticated,
    val signInAnonymously: SignInAnonymously,
    val signOut: SignOut
)