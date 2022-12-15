package com.victorhvs.tfc.presentation.screens.ranking

import com.victorhvs.tfc.CoroutinesRule
import com.victorhvs.tfc.core.State
import com.victorhvs.tfc.data.fake.FakeDataSource
import com.victorhvs.tfc.domain.models.User
import com.victorhvs.tfc.domain.repository.RankingRepository
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class RankingViewModelTest {

    @get:Rule
    val coroutineRule = CoroutinesRule()

    private val repository = mockk<RankingRepository>()

    lateinit var viewModel: RankingViewModel

    @Test
    fun `GIVEN an valid result RETURN State success WHEN viewModel is created`() =
        runTest {
            // GIVEN
            val success = State.success(listOf(FakeDataSource.user))

            coEvery {
                repository.getRanking()
            } returns flowOf(success)
            // WHEN
            // THEN
            viewModel = RankingViewModel(repository)
            viewModel.users.value shouldBe success
        }

    @Test
    fun `GIVEN an exception result RETURN State fail WHEN viewModel is created`() =
        runTest {
            // GIVEN
            val error = State.failed<List<User>>("ERROR")

            coEvery {
                repository.getRanking()
            } returns flowOf(error)
            // WHEN
            // THEN
            viewModel = RankingViewModel(repository)
            viewModel.users.value shouldBe error
        }
}