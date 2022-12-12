package com.victorhvs.tfc.presentation.screens.explore

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.victorhvs.tfc.R
import com.victorhvs.tfc.domain.models.Stock
import com.victorhvs.tfc.presentation.components.CardHorizontalStock
import com.victorhvs.tfc.presentation.components.ProgressBar
import com.victorhvs.tfc.presentation.components.SearchWidget
import kotlinx.coroutines.launch

@Composable
fun ExploreScreen(
    navigateToStockScreen: (stock: Stock) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExploreViewModel = hiltViewModel(),
) {

    val pagingStocks = viewModel.searchedStocks.collectAsLazyPagingItems()
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

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
                .fillMaxSize(),
        ) {
            val listState = rememberLazyListState()
            val coroutineScope = rememberCoroutineScope()

            SearchWidget(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = viewModel.searchQuery.value,
                onTextChange = { viewModel.updateSearchQuery(it) },
                onSearchClicked = {
                    viewModel.searchStocks(it)
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
            )
            Spacer(modifier = Modifier.height(4.dp))
            ExploreContent(
                navigateToStockScreen = navigateToStockScreen,
                stocks = pagingStocks,
                listState = listState
            )
        }
    }
}

@Composable
fun ExploreContent(
    stocks: LazyPagingItems<Stock>,
    listState: LazyListState,
    modifier: Modifier = Modifier,
    navigateToStockScreen: (stock: Stock) -> Unit
) {
    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize(),
    ) {
        items(stocks) { stock ->
            stock?.let {
                CardHorizontalStock(
                    stock = stock,
                    onStockClicked = navigateToStockScreen,
                )
            }
        }
    }

    stocks.apply {
        when {
            loadState.refresh is LoadState.Loading -> ProgressBar()
            loadState.refresh is LoadState.Error -> println(loadState)
            loadState.append is LoadState.Loading -> ProgressBar()
            loadState.append is LoadState.Error -> Toast.makeText(
                LocalContext.current,
                loadState.append.toString(),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
