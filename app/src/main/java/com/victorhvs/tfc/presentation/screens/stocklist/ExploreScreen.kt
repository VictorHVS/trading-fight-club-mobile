package com.victorhvs.tfc.presentation.screens.stocklist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.victorhvs.tfc.R
import com.victorhvs.tfc.domain.models.Stock
import com.victorhvs.tfc.presentation.components.CardHorizontalStock
import com.victorhvs.tfc.presentation.components.ProgressBar

@Composable
fun ExploreScreen(
    navigateToStockScreen: (stock: Stock) -> Unit,
    modifier: Modifier = Modifier
) {
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
    ) { paddingValues ->
        ExploreContent(
            paddingValues = paddingValues,
            navigateToStockScreen = navigateToStockScreen
        )
    }
}

@Composable
fun ExploreContent(
    viewModel: ExploreViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    navigateToStockScreen: (stock: Stock) -> Unit
) {
    val pagingStocks = viewModel.searchedStocks.collectAsLazyPagingItems()

    when (pagingStocks.loadState.refresh) {
        LoadState.Loading -> {
            ProgressBar()
        }
        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                itemsIndexed(pagingStocks) { _, stock ->
                    stock?.let {
                        CardHorizontalStock(
                            stock = stock,
                            onStockClicked = navigateToStockScreen
                        )
                    }
                }
            }
        }
    }
}
