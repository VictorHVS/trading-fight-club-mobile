package com.victorhvs.tfc.presentation.screens.buy_sell

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victorhvs.tfc.domain.enums.FirestoreState
import com.victorhvs.tfc.domain.models.Order
import com.victorhvs.tfc.domain.models.Stock
import com.victorhvs.tfc.domain.models.User
import com.victorhvs.tfc.domain.repository.ProfileRepository
import com.victorhvs.tfc.domain.repository.StockRepository
import com.victorhvs.tfc.presentation.extensions.toFormatedCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuySellViewModel @Inject constructor(
    private val repository: StockRepository,
    private val profileRepository: ProfileRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _stockState = MutableStateFlow<FirestoreState<Stock?>>(FirestoreState.loading())
    val stockState = _stockState

    private val _userState = MutableStateFlow<FirestoreState<User?>>(FirestoreState.loading())

    val _quantity = mutableStateOf(0)
    val subtotal = mutableStateOf("0")
    val finalNetValue = mutableStateOf("0")

    private val _postOrderState = MutableStateFlow<FirestoreState<Unit>>(FirestoreState.loading())
    val postOrderState = _postOrderState

    fun init(stockId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchStock(stockId).collect {
                _stockState.value = it
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.currentUser().collect {
                _userState.value = it
                calculeNetValue()
            }
        }
    }

    fun updateQuantity(quantity: String) {
        quantity.toIntOrNull()?.let {
            _quantity.value = it

            calculeSubtotal()
            calculeNetValue()
        }
    }

    fun postOrder(isBuy: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val stock : Stock = (_stockState.value as FirestoreState.Success<Stock?>).data!!
                val order = Order(
                    amount = _quantity.value,
                    stockId = stock.uuid,
                    isBuy = isBuy,
                    currency = stock.currency,
                    currencySymbol = "R$",
                    exchangeId = stock.exchangeId,
                    stockUrl = stock.logoUrl
                )
                repository.postOrder(order)
            }.onSuccess {
                _postOrderState.value = FirestoreState.success(Unit)
            }.onFailure {
                it.printStackTrace()
                _postOrderState.value = FirestoreState.failed(it.message)
            }
        }
    }

    private fun calculeSubtotal() {
        try {
            val stock : Stock = (_stockState.value as FirestoreState.Success<Stock?>).data!!
            subtotal.value = (_quantity.value * stock.price).toFormatedCurrency()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun calculeNetValue() {
        try {
            val user : User = (_userState.value as FirestoreState.Success<User?>).data!!
            finalNetValue.value = (user.portfolio.first().netValue - subtotal.value.toDouble()).toFormatedCurrency()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}