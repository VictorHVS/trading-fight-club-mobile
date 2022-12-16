package com.victorhvs.tfc.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.victorhvs.tfc.core.Resource
import com.victorhvs.tfc.data.datasource.FirebaseDataSource
import com.victorhvs.tfc.data.datasource.FirebaseDataSourceImp.Companion.USER_REF
import com.victorhvs.tfc.data.extensions.observeStatefulCollection
import com.victorhvs.tfc.domain.enums.FirestoreState
import com.victorhvs.tfc.domain.models.Order
import com.victorhvs.tfc.domain.models.Portfolio
import com.victorhvs.tfc.domain.models.User
import com.victorhvs.tfc.domain.repository.ProfileRepository
import com.victorhvs.tfc.domain.repository.SignOutResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestoreDataSource: FirebaseDataSource,
    private val client: FirebaseFirestore,
) : ProfileRepository {
    override suspend fun signOut(): SignOutResponse {
        return try {
            auth.currentUser?.delete()?.await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    override suspend fun currentUser(): Flow<FirestoreState<User?>> {
        return firestoreDataSource.getUser(auth.currentUser!!.uid)
    }

    override fun orderHistory(onlyPending: Boolean): Flow<FirestoreState<List<Order?>>> {
        val path = "${USER_REF}/${auth.currentUser!!.uid}/orders"

        var query = client.collection(path).orderBy("created_at", Query.Direction.DESCENDING)

        if (onlyPending)
            query = client.collection(path).whereEqualTo("executed", false)

        return observeStatefulCollection(query)
    }

    override fun portfolio(): Flow<FirestoreState<List<Portfolio?>>> {
        val path = "${USER_REF}/${auth.currentUser!!.uid}/portfolio"

        val query = client.collection(path).orderBy("total_spent", Query.Direction.DESCENDING)

        return observeStatefulCollection(query)
    }
}