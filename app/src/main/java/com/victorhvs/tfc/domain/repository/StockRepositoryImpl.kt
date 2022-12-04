package com.victorhvs.tfc.domain.repository

import com.victorhvs.tfc.core.DispatcherProvider
import com.victorhvs.tfc.core.State
import com.victorhvs.tfc.data.datasource.FirebaseDataSource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource,
    private val dispatcher: DispatcherProvider
) : StockRepository {

    override suspend fun getStocks() = flow {
        emit(State.loading())

        val lotteries = firebaseDataSource.getStocks()
        emit(State.success(lotteries))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(dispatcher.io())
}
