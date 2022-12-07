package com.victorhvs.tfc.data.datasource

import androidx.compose.ui.text.toUpperCase
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.victorhvs.tfc.core.DispatcherProvider
import com.victorhvs.tfc.domain.models.Stock
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseStocksPagingSource(
    val client: FirebaseFirestore,
    val query: String,
    val dispatcher: DispatcherProvider
) : PagingSource<QuerySnapshot, Stock>() {

    companion object {
        const val STOCK_REF = "stocks"
        const val STOCK_ORDER_FIELD = "uuid"
        const val STOCKS_PER_PAGE = 40L
    }

    override fun getRefreshKey(state: PagingState<QuerySnapshot, Stock>): QuerySnapshot? = null

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Stock> =
        withContext(dispatcher.io()) {
            try {
                val query = client.collection(STOCK_REF)
                    .whereEqualTo("enabled", true)
                    .whereGreaterThanOrEqualTo(STOCK_ORDER_FIELD, query.uppercase())
                    .orderBy(STOCK_ORDER_FIELD, Query.Direction.ASCENDING)
                    .limit(STOCKS_PER_PAGE)

                val currentPage = params.key ?: query.get().await()
                val lastVisibleProduct = currentPage.documents[currentPage.size() - 1]
                val nextPage = query.startAfter(lastVisibleProduct).get().await()
                LoadResult.Page(
                    data = currentPage.toObjects(Stock::class.java),
                    prevKey = null,
                    nextKey = nextPage
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }

}