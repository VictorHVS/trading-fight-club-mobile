package com.victorhvs.tfc.presentation.screens.stock

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.victorhvs.tfc.domain.models.FirestoreState
import com.victorhvs.tfc.domain.models.Stock
import com.victorhvs.tfc.domain.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(
    private val repository: StockRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

//    val stockState: Flow<FirestoreState<Stock?>> =
//        repository.fetchStock(savedStateHandle.get<String>("uuid")!!)

    private val _stockState = MutableStateFlow<FirestoreState<Stock?>>(FirestoreState.loading())
    val stockState = _stockState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val breweryId = savedStateHandle.get<String>("stockId")
            breweryId?.let {
                repository.fetchStock(it).collect {
                    _stockState.value = it
                }
            }
        }
    }

//    fun updateSearchQuery(query: String) {
//        _searchQuery.value = query
//    }
//
//    fun searchStocks(query: String) {
//        viewModelScope.launch {
//            repository.searchStocks(query = query).cachedIn(viewModelScope).collect {
//                _searchedStock.value = it
//            }
//        }
//    }
}