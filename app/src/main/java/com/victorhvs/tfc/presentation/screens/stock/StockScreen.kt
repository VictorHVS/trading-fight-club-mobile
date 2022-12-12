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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.victorhvs.tfc.R
import com.victorhvs.tfc.data.fake.FakeDataSource.weekPrice
import com.victorhvs.tfc.domain.enums.FirestoreState
import com.victorhvs.tfc.domain.enums.Interval
import com.victorhvs.tfc.domain.models.Stock
import com.victorhvs.tfc.domain.models.TimeSeries
import com.victorhvs.tfc.presentation.components.LogoRounded
import com.victorhvs.tfc.presentation.components.ProgressBar
import com.victorhvs.tfc.presentation.extensions.gainOrLossColor
import com.victorhvs.tfc.presentation.extensions.toFormatedCurrency
import com.victorhvs.tfc.presentation.theme.TfcTheme
import com.victorhvs.tfc.presentation.theme.spacing
import kotlin.math.floor


@Composable
fun StockScreen(
    stockId: String,
    stockName: String,
    viewModel: StockViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    showSheet: () -> Unit,
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
                    Header(stock = stock)
                    Chart(
                        timeseriesState = timeseriesState,
                        modifier = Modifier.weight(1f),
                        color = stock.priceAbsoluteFloating.gainOrLossColor(),
                        onClick = { interval ->
//                            LaunchedEffect(Unit) {
                            viewModel.fetchTimeSeries(stockId, interval)
//                            }
                        }
                    )

                    val userHasStock = true
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
    color: Color = Color.Black,
    onClick: (Interval) -> Unit
) {
    Column(modifier = modifier) {
        when (timeseriesState) {
            is FirestoreState.Failed -> {}
            is FirestoreState.Loading -> ProgressBar()
            is FirestoreState.Success -> {
                timeseriesState.data?.let {
                    LineChart(
                        modifier = modifier,
                        timeSeries = timeseriesState.data,
                        lineColor = color
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = { onClick(Interval.Today()) }
            ) {
                Text(text = "1D")
            }
            TextButton(onClick = { onClick(Interval.OneWeek()) }) {
                Text(text = "1W")
            }
            TextButton(onClick = { onClick(Interval.OneMonth()) }) {
                Text(text = "1M")
            }
            TextButton(onClick = { onClick(Interval.OneYear()) }) {
                Text(text = "1A")
            }
            TextButton(onClick = { onClick(Interval.Max()) }) {
                Text(text = "Máx.")
            }
            TextButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Outlined.CandlestickChart, contentDescription = null)
            }
        }
    }
}

@Composable
fun BuySellButtons(
    userHasStock: Boolean,
    showSheet: () -> Unit
) {
    Row(
        modifier = Modifier.padding(
            horizontal = MaterialTheme.spacing.medium
        ),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        Button(modifier = Modifier.weight(1f), onClick = { showSheet() }) {
            Text(text = stringResource(id = R.string.buy_action))
        }
        if (userHasStock) {
            Button(modifier = Modifier.weight(1f), onClick = { showSheet() }) {
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
            Column {
                Text(text = "Quantidade", style = MaterialTheme.typography.labelMedium)
                Text(
                    text = amount.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            Column {
                Text(text = "Custo Médio", style = MaterialTheme.typography.labelMedium)
                Text(
                    text = "R$$priceAvg",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            Column {
                Text(text = "Valor total", style = MaterialTheme.typography.labelMedium)
                Text(
                    text = "R$$portfolioSum",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            Column {
                Text(text = "Lucro", style = MaterialTheme.typography.labelMedium)
                Text(
                    text = "R$$profitAbsolute ($profitRate)",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = profitAbsolute.gainOrLossColor()
                )
            }
        }
    }
}

@Composable
fun Header(
    stock: Stock,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = stock.name,
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = "R$" + stock.price.toFormatedCurrency(),
                style = MaterialTheme.typography.headlineLarge
            )
            val priceText = remember {
                val absoluteFloat = stock.priceAbsoluteFloating.toFormatedCurrency(true)
                val priceRate = stock.priceFloating.toFormatedCurrency()

                mutableStateOf("R$$absoluteFloat | $priceRate%")
            }

            Text(
                text = priceText.value,
                color = stock.priceAbsoluteFloating.gainOrLossColor(),
                style = MaterialTheme.typography.labelSmall
            )
        }
        LogoRounded(stock.logoUrl)
    }
}

@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    timeSeries: List<TimeSeries?>?,
    lineColor: Color = Color.Black
) {
    val textSize = 12f

    AndroidView(
        modifier = modifier
            .fillMaxWidth(),
        factory = { ctx ->
            LineChart(ctx)
        },
        update = { lineChart ->
            var lineData: LineData? = null

            val entries = timeSeries!!.filter { !it!!.close.isNaN() }.mapIndexed { index, item ->
                val value = floor(item!!.close * 100) / 100
                Entry(
                    index.toFloat(),
//                    item!!.close.toFloat()
                    value.toFloat()
                )
            }
            val dataset = LineDataSet(entries, "").apply {
                color = lineColor.toArgb()
//                valueFormatter = dataSetFormatter
                lineWidth = 2f
                setDrawCircles(false)
                setDrawValues(false)
            }
            lineData = LineData(dataset).apply {
                setValueTextSize(textSize)
//                setValueTextColor(onSurfaceTextColor)
            }

            lineChart.apply {
                legend.isEnabled = false
                setDrawGridBackground(false)
                setDrawBorders(false)
                setPinchZoom(false)
                setScaleEnabled(false)
//                setVisibleXRangeMaximum(2f)
                isDragEnabled = true
                description.isEnabled = false

                xAxis.apply {
//                    granularity = 1f
//                    isGranularityEnabled = true
                    setTextSize(textSize)
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    setDrawLabels(false)
                    setDrawBorders(false)
                    setDrawAxisLine(false)
//                    textColor = onSurfaceTextColor
//                    valueFormatter = xAxisFormatter
                }
                axisLeft.apply {
                    setTextSize(textSize)
//                    textColor = onSurfaceTextColor
//                    axisMinimum = 0f
//                    granularity = 1f
                    isGranularityEnabled = true
                    setDrawGridLines(false)
                    setDrawLabels(false)
                    setDrawBorders(false)
                    setDrawAxisLine(false)
//                    valueFormatter = leftAxisFormatter
                }
                axisRight.apply {
//                    axisMinimum = 0f
                    setDrawLabels(false)
                    setDrawGridLines(false)
                    setDrawLabels(false)
                    setDrawBorders(false)
                    setDrawAxisLine(false)
                }

                data = lineData

//                setVisibleXRangeMaximum(entries.size.div(3).toFloat())
//                moveViewToX(data.entryCount.toFloat())

                val ll1 = LimitLine(entries.maxBy { it.y }.y, "Upper Limit")
                ll1.lineWidth = 2f
                ll1.enableDashedLine(20f, 2f, 0f)
                ll1.labelPosition = LimitLabelPosition.RIGHT_TOP
                ll1.textSize = 10f
//                ll1.typeface = tfRegular

                val ll2 = LimitLine(entries.minBy { it.y }.y, "Lower Limit")
                ll2.lineWidth = 2f
                ll2.enableDashedLine(20f, 2f, 0f)
                ll2.labelPosition = LimitLabelPosition.RIGHT_BOTTOM
                ll2.textSize = 10f
                ll2.lineColor = Color.Gray.toArgb()
//                ll2.typeface = tfRegular


                // add limit lines
                axisLeft.addLimitLine(ll1)
                axisRight.addLimitLine(ll2)

                invalidate()
            }
        })
}

@Preview(showBackground = true)
@Composable
fun StockScreenPrev() {
    TfcTheme {
//        StockScreen(stockId = "flry3", stockName = "Fleury", navigateBack = { }) {
//
//        }
        LineChart(Modifier.fillMaxSize(), weekPrice)
    }
}