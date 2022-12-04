package com.victorhvs.tfc.presentation.screens.stocklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victorhvs.tfc.core.State
import com.victorhvs.tfc.domain.models.Stock
import com.victorhvs.tfc.domain.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val repository: StockRepository
) : ViewModel() {

    private val _stocks: MutableStateFlow<State<List<Stock>>> =
        MutableStateFlow(State.loading())
    val stocks: StateFlow<State<List<Stock>>> = _stocks

    init {
        getStocks()
    }

    private fun getStocks() = viewModelScope.launch {
        try {
            repository.getStocks().collect {
                _stocks.value = it
            }
        } catch (exception: Exception) {
            _stocks.value = State.failed("${exception.message}")
        }
    }
}
