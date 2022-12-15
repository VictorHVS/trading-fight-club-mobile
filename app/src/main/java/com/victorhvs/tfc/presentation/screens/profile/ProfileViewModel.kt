package com.victorhvs.tfc.presentation.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victorhvs.tfc.core.Resource
import com.victorhvs.tfc.domain.enums.FirestoreState
import com.victorhvs.tfc.domain.models.User
import com.victorhvs.tfc.domain.repository.ProfileRepository
import com.victorhvs.tfc.domain.repository.SignOutResponse
import com.victorhvs.tfc.domain.use_case.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val useCases: UseCases
) : ViewModel() {

    private val _userState = MutableStateFlow<FirestoreState<User?>>(FirestoreState.loading())
    val userState = _userState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.currentUser().collect {
                _userState.value = it
            }
        }
    }

    var signOutResponse by mutableStateOf<SignOutResponse>(Resource.Success(false))
        private set

    fun signOut() = viewModelScope.launch {
        signOutResponse = Resource.Loading
        signOutResponse = useCases.signOut()
    }
}
