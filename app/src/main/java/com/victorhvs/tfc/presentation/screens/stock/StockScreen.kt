package com.victorhvs.tfc.presentation.screens.stock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CandlestickChart
import androidx.compose.material.icons.rounded.ShowChart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.victorhvs.tfc.R
import com.victorhvs.tfc.domain.enums.FirestoreState
import com.victorhvs.tfc.domain.enums.Interval
import com.victorhvs.tfc.domain.models.Stock
import com.victorhvs.tfc.domain.models.TimeSeries
import com.victorhvs.tfc.presentation.components.Header
import com.victorhvs.tfc.presentation.components.ProgressBar
import com.victorhvs.tfc.presentation.components.SimpleIndicator
import com.victorhvs.tfc.presentation.components.charts.TfcCandleChart
import com.victorhvs.tfc.presentation.components.charts.TfcLineChart
import com.victorhvs.tfc.presentation.extensions.gainOrLossColor
import com.victorhvs.tfc.presentation.theme.TfcTheme
import com.victorhvs.tfc.presentation.theme.spacing


@Composable
fun StockScreen(
    stockId: String,
    viewModel: StockViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    showSheet: (isBuy: Boolean) -> Unit,
) {

    val stockState by viewModel.stockState.collectAsState()
    val timeseriesState by viewModel.timeseriesState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stockId.uppercase())
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
    ) { paddingValues ->

        when (stockState) {
            is FirestoreState.Failed -> navigateBack()
            is FirestoreState.Loading -> ProgressBar()
            is FirestoreState.Success -> {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    val stock =
                        (stockState as FirestoreState.Success<Stock?>).data ?: return@Scaffold
                    Header(
                        name = stock.name,
                        price = stock.price,
                        priceAbsoluteFloating = stock.priceAbsoluteFloating,
                        priceFloating = stock.priceFloating,
                        imageUrl = stock.logoUrl
                    )

                    Chart(
                        timeseriesState = timeseriesState,
                        modifier = Modifier.weight(1f),
                        lineColor = stock.priceAbsoluteFloating.gainOrLossColor()
                    ) { interval ->
                        viewModel.fetchTimeSeries(stockId, interval)
                    }

                    val userHasStock = false
                    if (userHasStock) {
                        UserStock(
                            amount = 10,
                            priceAvg = 300.19,
                            portfolioSum = 3_001.90,
                            profitAbsolute = 187.0,
                            profitRate = 2.01
                        )
                    }
                    BuySellButtons(userHasStock, showSheet = showSheet)
                }
            }
        }
    }
}

@Composable
fun Chart(
    timeseriesState: FirestoreState<List<TimeSeries?>>,
    modifier: Modifier = Modifier,
    lineColor: Color = Color.Black,
    onClick: (Interval) -> Unit
) {

    var isLineChart by remember { mutableStateOf(true) }
    var selectedPeriod: Interval by remember { mutableStateOf(Interval.OneWeek()) }

    Column(modifier = modifier) {
        when (timeseriesState) {
            is FirestoreState.Failed -> {}
            is FirestoreState.Loading -> ProgressBar()
            is FirestoreState.Success -> {
                if (isLineChart) {
                    TfcLineChart(
                        modifier = modifier,
                        timeSeries = timeseriesState.data,
                        lineColor = lineColor
                    )
                } else {
                    TfcCandleChart(
                        modifier = modifier,
                        timeSeries = timeseriesState.data
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Interval.allIntervals().forEach { interval ->
                TextButton(
                    onClick = {
                        onClick(interval)
                        selectedPeriod = interval
                    }
                ) {
                    val color =
                        if (selectedPeriod == interval) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    Text(
                        text = interval.period,
                        color = color
                    )
                }
            }

            TextButton(onClick = { isLineChart = !isLineChart }) {
                if (isLineChart)
                    Icon(imageVector = Icons.Outlined.CandlestickChart, contentDescription = null)
                else
                    Icon(imageVector = Icons.Rounded.ShowChart, contentDescription = null)
            }
        }
    }
}

@Composable
fun BuySellButtons(
    userHasStock: Boolean,
    showSheet: (isBuy: Boolean) -> Unit
) {
    Row(
        modifier = Modifier.padding(
            horizontal = MaterialTheme.spacing.medium
        ),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        Button(modifier = Modifier.weight(1f), onClick = { showSheet(true) }) {
            Text(text = stringResource(id = R.string.buy_action))
        }
        if (userHasStock) {
            Button(modifier = Modifier.weight(1f), onClick = { showSheet(false) }) {
                Text(text = stringResource(id = R.string.sell_action))
            }
        }
    }
}

@Composable
fun UserStock(
    amount: Int,
    priceAvg: Double,
    portfolioSum: Double,
    profitAbsolute: Double,
    profitRate: Double
) {
    LazyVerticalGrid(
        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {

        item {
            SimpleIndicator(
                label = R.string.quantity,
                textValue = amount.toString(),
                textColor = MaterialTheme.colorScheme.primary
            )
        }

        item {
            SimpleIndicator(
                label = R.string.avg_price,
                textValue = "R$$priceAvg",
                textColor = MaterialTheme.colorScheme.primary
            )
        }

        item {
            SimpleIndicator(
                label = R.string.total_value,
                textValue = "R$$portfolioSum",
                textColor = MaterialTheme.colorScheme.primary
            )
        }

        item {
            SimpleIndicator(
                label = R.string.profit,
                textValue = "R$$profitAbsolute ($profitRate)",
                textColor = profitAbsolute.gainOrLossColor()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StockScreenPrev() {
    TfcTheme {
        StockScreen(
            stockId = "flry3",
            navigateBack = { },
            showSheet = { }
        )
    }
}