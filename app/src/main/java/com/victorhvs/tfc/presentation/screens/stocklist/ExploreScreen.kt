package com.victorhvs.tfc.presentation.screens.stocklist

import android.widget.Space
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.victorhvs.tfc.R
import com.victorhvs.tfc.domain.models.Stock
import com.victorhvs.tfc.presentation.components.CardHorizontalStock
import com.victorhvs.tfc.presentation.components.ProgressBar
import com.victorhvs.tfc.presentation.components.SearchWidget

@Composable
fun ExploreScreen(
    navigateToStockScreen: (stock: Stock) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExploreViewModel = hiltViewModel(),
) {

    val pagingStocks = viewModel.searchedStocks.collectAsLazyPagingItems()
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
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            SearchWidget(
                modifier = modifier.padding(horizontal = 16.dp),
                text = viewModel.searchQuery.value,
                onTextChange = { viewModel.updateSearchQuery(it) },
                onSearchClicked = { viewModel.searchStocks(it) }
            )
            Spacer(modifier = modifier.height(4.dp))
            ExploreContent(
                modifier = modifier,
                navigateToStockScreen = navigateToStockScreen,
                pagingStocks = pagingStocks
            )
        }
    }
}

@Composable
fun ExploreContent(
    modifier: Modifier = Modifier,
    navigateToStockScreen: (stock: Stock) -> Unit,
    pagingStocks: LazyPagingItems<Stock>
) {
    when (pagingStocks.loadState.refresh) {
        LoadState.Loading -> {
            ProgressBar()
        }
        else -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
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
