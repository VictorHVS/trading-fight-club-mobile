package com.victorhvs.tfc.data.repository

import com.victorhvs.tfc.CoroutinesRule
import com.victorhvs.tfc.core.State
import com.victorhvs.tfc.data.datasource.FirebaseDataSource
import com.victorhvs.tfc.data.fake.FakeDataSource
import com.victorhvs.tfc.domain.models.User
import io.kotest.matchers.collections.shouldContainAll
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class RankingRepositoryImplTest {
    private val datasource: FirebaseDataSource = mockk()

    @get:Rule
    val coroutineRule = CoroutinesRule()
    private val repository = RankingRepositoryImpl(datasource, coroutineRule.dispatcherProvider)

    @Test
    fun `GIVEN an exception RETURN loading and fail WHEN getRanking is called`() = runTest {
        // GIVEN
        val states = mutableListOf<State<List<User>>>()

        coEvery {
            datasource.getRanking()
        } throws Exception("")
        // WHEN
        val job = launch(coroutineRule.scope.coroutineContext) {
            repository.getRanking().toList(states)
        }

        // THEN
        states shouldContainAll listOf(State.loading(), State.failed(""))

        job.cancel()
    }

    @Test
    fun `GIVEN an valid result RETURN loading and success WHEN getRanking is called`() =
        runTest {
            // GIVEN
            val states = mutableListOf<State<List<User>>>()

            coEvery {
                datasource.getRanking()
            } returns listOf(FakeDataSource.user)
            // WHEN
            val job = launch(coroutineRule.scope.coroutineContext) {
                repository.getRanking().toList(states)
            }

            // THEN
            states shouldContainAll listOf(
                State.loading(),
                State.success(listOf(FakeDataSource.user))
            )

            job.cancel()
        }
}