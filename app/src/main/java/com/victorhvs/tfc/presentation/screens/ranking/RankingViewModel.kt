package com.victorhvs.tfc.presentation.screens.ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victorhvs.tfc.core.State
import com.victorhvs.tfc.domain.models.User
import com.victorhvs.tfc.domain.repository.RankingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(
    private val repository: RankingRepository
) : ViewModel() {

    private val _users: MutableStateFlow<State<List<User>>> =
        MutableStateFlow(State.loading())
    val users: StateFlow<State<List<User>>> = _users

    init {
        getTop50()
    }

    private fun getTop50() = viewModelScope.launch {
        try {
            repository.getRanking().collect {
                _users.value = it
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            _users.value = State.failed("${exception.message}")
        }
    }
}
