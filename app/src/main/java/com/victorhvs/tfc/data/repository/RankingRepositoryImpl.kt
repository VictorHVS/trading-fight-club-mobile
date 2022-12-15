package com.victorhvs.tfc.data.repository

import com.victorhvs.tfc.core.DispatcherProvider
import com.victorhvs.tfc.core.State
import com.victorhvs.tfc.data.datasource.FirebaseDataSource
import com.victorhvs.tfc.domain.models.User
import com.victorhvs.tfc.domain.repository.RankingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RankingRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource,
    private val dispatcher: DispatcherProvider
) : RankingRepository {

    override fun getRanking(): Flow<State<List<User>>> = flow {
        emit(State.loading())

        val lotteries = firebaseDataSource.getRanking()
        emit(State.success(lotteries))
    }.catch {
        it.printStackTrace()
        emit(State.failed(it.message.toString()))
    }.flowOn(dispatcher.io())
}