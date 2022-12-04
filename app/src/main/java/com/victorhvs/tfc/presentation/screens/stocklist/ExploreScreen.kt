package com.victorhvs.tfc.presentation.screens.stocklist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.victorhvs.tfc.R
import com.victorhvs.tfc.core.State
import com.victorhvs.tfc.data.fake.FakeDataSource
import com.victorhvs.tfc.domain.models.Stock
import com.victorhvs.tfc.presentation.components.CardHorizontalStock
import com.victorhvs.tfc.presentation.theme.TfcTheme

@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier,
    viewModel: ExploreViewModel = hiltViewModel()
) {
    val state = viewModel.stocks.collectAsState(initial = State.loading()).value
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.screen_explore))
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .wrapContentSize(Alignment.Center),
        ) {
            when (state) {
                is State.Failed -> {
                    Text(text = state.message)
                }

                is State.Loading -> {
                    Text(text = stringResource(id = R.string.loading))
                }

                is State.Success -> {
                    StockList(
                        stocks = state.data,
                    )
                }
            }
        }
    }
}

@Composable
fun StockList(
    stocks: List<Stock>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = stocks, key = { it.uuid }) { stock ->
            CardHorizontalStock(
                stock = stock,
            )
        }
    }
}

@Preview
@Composable
private fun StockListPreview() {

    val stocks = arrayListOf(
        FakeDataSource.flry3.copy(uuid = "1"),
        FakeDataSource.flry3.copy(uuid = "2"),
        FakeDataSource.flry3.copy(uuid = "3"),
        FakeDataSource.flry3.copy(uuid = "4"),
        FakeDataSource.flry3.copy(uuid = "5"),
        FakeDataSource.flry3.copy(uuid = "6"),
    )

    TfcTheme {
        StockList(stocks = stocks)
    }
}
