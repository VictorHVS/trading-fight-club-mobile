package com.victorhvs.tfc.presentation.screens.ranking

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.victorhvs.tfc.R
import com.victorhvs.tfc.core.State
import com.victorhvs.tfc.domain.models.User
import com.victorhvs.tfc.presentation.components.CardHorizontalRanking
import com.victorhvs.tfc.presentation.components.ProgressBar

@Composable
fun RankingListScreen(
    modifier: Modifier = Modifier,
    viewModel: RankingViewModel = hiltViewModel()
) {
    val state = viewModel.users.collectAsState(initial = State.loading()).value
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.screen_ranking))
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            RankingContent(modifier, state)
        }
    }
}

@Composable
fun RankingContent(
    modifier: Modifier = Modifier,
    state: State<List<User>>
) {
    when (state) {
        is State.Failed -> {
            Text(text = state.message)
        }

        is State.Loading -> ProgressBar()

        is State.Success -> {
            RankingList(modifier = modifier, users = state.data)
        }
    }
}

@Composable
fun RankingList(
    users: List<User>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(users) { index, user ->
            CardHorizontalRanking(user = user, position = index + 1)
        }
    }
}