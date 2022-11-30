package com.victorhvs.tfc.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.victorhvs.tfc.core.DispatcherProvider
import javax.inject.Inject

interface FirebaseDataSource {
//    suspend fun getMegaResults(): List<ContestResult>
}

class FirebaseDataSourceImp @Inject constructor(
    val client: FirebaseFirestore,
    val dispatcher: DispatcherProvider
) : FirebaseDataSource {

//    override suspend fun getMegaResults(): List<ContestResult> {
//        return withContext(dispatcher.io()) {
//            val mLotteryCollection = client.collection(Constants.FIRESTORE_COLLECTION_MEGA)
//
//            val snapshot = mLotteryCollection.limit(15)
//                .orderBy(Constants.FIRESTORE_CONTEST_DATEFIELD, Query.Direction.DESCENDING)
//                .get()
//                .await()
//
//            snapshot.toObjects(ContestResult::class.java)
//        }
//    }
}
