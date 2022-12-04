package com.victorhvs.tfc.presentation.screens.stocklist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.victorhvs.tfc.domain.models.Stock
import com.victorhvs.tfc.domain.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val repository: StockRepository
) : ViewModel() {

//    private val _stocks: MutableStateFlow<State<List<Stock>>> =
//        MutableStateFlow(State.loading())
//    val stocks: StateFlow<State<List<Stock>>> = _stocks

//    init {
//        getStocks()
//    }

//    private fun getStocks() = viewModelScope.launch {
//        try {
//            repository.getStocks().collect {
//                _stocks.value = it
//            }
//        } catch (exception: Exception) {
//            _stocks.value = State.failed("${exception.message}")
//        }
//    }

    private val _searchQuery = mutableStateOf("")
    val searchQuery = _searchQuery

    private val _searchedStock = MutableStateFlow<PagingData<Stock>>(PagingData.empty())
    val searchedStocks = _searchedStock

    init {
        _searchQuery.value = ""
        searchStocks(_searchQuery.value)
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun searchStocks(query: String) {
        viewModelScope.launch {
            repository.searchStocks(query = query).cachedIn(viewModelScope).collect {
                _searchedStock.value = it
            }
        }
    }

//    val stocks = repository.searchStocks("").cachedIn(viewModelScope)
}
