package com.victorhvs.tfc.domain.repository

import com.victorhvs.tfc.core.State
import com.victorhvs.tfc.domain.models.User
import kotlinx.coroutines.flow.Flow

interface RankingRepository {

    fun getRanking(): Flow<State<List<User>>>
}