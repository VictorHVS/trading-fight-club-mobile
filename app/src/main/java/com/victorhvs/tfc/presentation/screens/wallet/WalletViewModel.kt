package com.victorhvs.tfc.presentation.screens.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victorhvs.tfc.core.State
import com.victorhvs.tfc.domain.enums.FirestoreState
import com.victorhvs.tfc.domain.models.Order
import com.victorhvs.tfc.domain.models.Portfolio
import com.victorhvs.tfc.domain.models.User
import com.victorhvs.tfc.domain.repository.ProfileRepository
import com.victorhvs.tfc.domain.repository.RankingRepository
import com.victorhvs.tfc.domain.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _userState = MutableStateFlow<FirestoreState<User?>>(FirestoreState.loading())
    val userState = _userState

    private val _orders: MutableStateFlow<FirestoreState<List<Order?>>> = MutableStateFlow(FirestoreState.loading())
    val orders: StateFlow<FirestoreState<List<Order?>>> = _orders

    private val _portfolios: MutableStateFlow<FirestoreState<List<Portfolio?>>> = MutableStateFlow(FirestoreState.loading())
    val portfolios: StateFlow<FirestoreState<List<Portfolio?>>> = _portfolios

    init {
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.currentUser().collect {
                _userState.value = it
            }
        }
        observePendingOrders()
        portfolio()
    }

    private fun observePendingOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.orderHistory(true).collect {
                _orders.value = it
            }
        }
    }

    private fun portfolio() {
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.portfolio().collect {
                _portfolios.value = it
            }
        }
    }
}
