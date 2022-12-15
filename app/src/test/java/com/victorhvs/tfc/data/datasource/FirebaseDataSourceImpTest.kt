package com.victorhvs.tfc.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.victorhvs.tfc.CoroutinesRule
import com.victorhvs.tfc.data.fake.FakeDataSource
import com.victorhvs.tfc.domain.models.User
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class FirebaseDataSourceImpTest {

    val client: FirebaseFirestore = mockk()

    @get:Rule
    val coroutineRule = CoroutinesRule()

    val firebaseDataSourceImp = FirebaseDataSourceImp(
        client = client,
        dispatcher = coroutineRule.dispatcherProvider
    )

    val userMock = FakeDataSource.user

    @Test
    fun `GIVEN a valid datasource RETURN users WHEN getRanking is called`() = runTest {
        // GIVEN
        prepareScenario()
        // WHEN
        val results = firebaseDataSourceImp.getRanking()
        // THEN
        assert(results.isNotEmpty())
    }

    private fun prepareScenario(
        result: User = userMock
    ) {
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")

        val querySnap = mockk<QuerySnapshot>()
        coEvery {
            val query = client.collection(any())
                .limit(any())
                .orderBy(any<String>(), any())
                .get()
            query.await()
        } returns querySnap

        every { querySnap.toObjects(User::class.java) } returns listOf(result)
    }
}