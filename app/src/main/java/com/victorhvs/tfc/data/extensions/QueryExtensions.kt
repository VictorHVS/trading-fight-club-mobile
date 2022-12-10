package com.victorhvs.tfc.data.extensions

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.victorhvs.tfc.domain.models.FirestoreState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

fun Query.asFlow(): Flow<QuerySnapshot> {
    return callbackFlow {
        val callback = addSnapshotListener { querySnapshot, ex ->
            if (ex != null) {
                close(ex)
            } else {
                trySend(querySnapshot!!).isSuccess
            }
        }
        awaitClose {
            callback.remove()
        }
    }
}

inline fun <reified T> getDoc(docRef: DocumentReference) = flow {

    val task = docRef.get()
    val result = kotlin.runCatching { Tasks.await(task) }

    val data = result.getOrNull()?.toObject(T::class.java)
    emit(data)
}

inline fun <reified T> getCollection(colRef: Query) = flow {
    val task = colRef.get()
    val result = kotlin.runCatching { Tasks.await(task) }

    val data = result.getOrNull()?.documents?.map {
        it.toObject(T::class.java)
    }
    emit(data)
}

inline fun <reified T> observeDoc(docRef: DocumentReference):
        Flow<T?> = callbackFlow {
    val subscription = docRef.addSnapshotListener { snapshot, error ->
        if (error != null) {
            trySend(null)
            return@addSnapshotListener
        }
        snapshot?.exists()?.let {
            val obj = snapshot.toObject(T::class.java)
            trySend(obj)
        }
    }
    awaitClose { subscription.remove() }
}

inline fun <reified T> observeCollection(colRef: Query):
        Flow<List<T?>?> = callbackFlow {

    val subscription = colRef.addSnapshotListener { query, error ->
        if (error != null) {
            trySend(null)
            return@addSnapshotListener
        }

        val docs = query?.documents?.map {
            it.toObject(T::class.java)
        }

        trySend(docs)
    }

    awaitClose { subscription.remove() }
}

inline fun <reified T> observeStatefulDoc(docRef: DocumentReference):
        Flow<FirestoreState<T?>> = callbackFlow {

    trySend(FirestoreState.loading())

    val subscription = docRef.addSnapshotListener { snapshot, error ->
        if (error != null) {
            trySend(FirestoreState.failed(error.message))
            return@addSnapshotListener
        }
        snapshot?.exists()?.let {
            val obj = snapshot.toObject(T::class.java)
            trySend(FirestoreState.success(obj))
        }
    }

    awaitClose { subscription.remove() }
}

inline fun <reified T> getStatefulCollection(colRef: Query) = flow {
    emit(FirestoreState.loading())

    val task = colRef.get()
    val result = kotlin.runCatching { Tasks.await(task) }.getOrNull()

    val exception = task.exception

    if (exception != null) {
        emit(FirestoreState.failed(exception.message)) as T
    } else {
        val data = result?.documents?.map {
            it.toObject(T::class.java)
        }
        emit(FirestoreState.success(data))
    }
}