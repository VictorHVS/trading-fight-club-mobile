package com.victorhvs.tfc.presentation.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victorhvs.tfc.core.Resource
import com.victorhvs.tfc.domain.enums.AuthProvider
import com.victorhvs.tfc.domain.repository.SignInResponse
import com.victorhvs.tfc.domain.use_case.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val useCases: UseCases
): ViewModel() {
    val isUserAuthenticated get() = useCases.isUserAuthenticated()
    var signInResponse by mutableStateOf<SignInResponse>(Resource.Success(false))
        private set

    fun signIn(authProvider: AuthProvider) = viewModelScope.launch {
        signInResponse = Resource.Loading

        signInResponse = when (authProvider) {
            AuthProvider.ANON -> useCases.signInAnonymously()
            AuthProvider.GOOGLE -> TODO()
            AuthProvider.GITHUB -> TODO()
            AuthProvider.EMAIL -> TODO()
        }
    }
}