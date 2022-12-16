package com.victorhvs.tfc.presentation.screens.buy_sell

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victorhvs.tfc.domain.enums.FirestoreState
import com.victorhvs.tfc.domain.enums.Interval
import com.victorhvs.tfc.domain.models.Stock
import com.victorhvs.tfc.domain.models.TimeSeries
import com.victorhvs.tfc.domain.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuySellViewModel @Inject constructor(
    private val repository: StockRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _stockState = MutableStateFlow<FirestoreState<Stock?>>(FirestoreState.loading())
    val stockState = _stockState

    private val _timeseriesState =
        MutableStateFlow<FirestoreState<List<TimeSeries?>>>(FirestoreState.loading())
    val timeseriesState = _timeseriesState

    init {
        val stockId = savedStateHandle.get<String>("stockId")
        stockId?.let {

            viewModelScope.launch(Dispatchers.IO) {
                repository.fetchStock(it).collect {
                    _stockState.value = it
                }
            }
            fetchTimeSeries(stockId, Interval.OneWeek())
        }
    }

    fun fetchTimeSeries(stockId: String, interval: Interval) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchTimeSeries(stockId, interval).collect {
                _timeseriesState.value = it
            }
        }
    }
}