package com.victorhvs.tfc.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.victorhvs.tfc.core.DispatcherProvider
import com.victorhvs.tfc.domain.models.Stock
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface FirebaseDataSource {
    suspend fun getStocks(): List<Stock>
}

class FirebaseDataSourceImp @Inject constructor(
    val client: FirebaseFirestore,
    val dispatcher: DispatcherProvider
) : FirebaseDataSource {

    companion object {
        const val STOCK_REF = "stocks"
        const val STOCK_ORDER_FIELD = "uuid"
    }
    override suspend fun getStocks(): List<Stock> {
        return withContext(dispatcher.io()) {
            val mLotteryCollection = client.collection(STOCK_REF)

            val snapshot = mLotteryCollection
                .whereEqualTo("enabled", true)
                .limit(50)
                .orderBy(STOCK_ORDER_FIELD, Query.Direction.ASCENDING)
                .get()
                .await()

            snapshot.toObjects(Stock::class.java)
        }
    }
}
