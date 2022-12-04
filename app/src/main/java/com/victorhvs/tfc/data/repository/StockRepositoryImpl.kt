package com.victorhvs.tfc.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.firestore.FirebaseFirestore
import com.victorhvs.tfc.core.DispatcherProvider
import com.victorhvs.tfc.data.datasource.FirebaseDataSource
import com.victorhvs.tfc.data.datasource.FirebaseStocksPagingSource
import com.victorhvs.tfc.domain.models.Stock
import com.victorhvs.tfc.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val client: FirebaseFirestore,
    private val dispatcher: DispatcherProvider
) : StockRepository {

    //    override suspend fun getStocks() = flow {
//        emit(State.loading())
//
//        val lotteries = firebaseDataSource.getStocks()
//        emit(State.success(lotteries))
//    }.catch {
//        emit(State.failed(it.message.toString()))
//    }.flowOn(dispatcher.io())

    companion object {
        const val STOCKS_PER_PAGE = 20
    }
    override fun searchStocks(query: String): Flow<PagingData<Stock>> {
        return Pager(
            config = PagingConfig(pageSize = STOCKS_PER_PAGE),
            pagingSourceFactory = {
                FirebaseStocksPagingSource(
                    client = client,
                    query = query,
                    dispatcher = dispatcher
                )
            }
        ).flow
    }

}
