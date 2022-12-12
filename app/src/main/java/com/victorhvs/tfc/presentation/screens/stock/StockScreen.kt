package com.victorhvs.tfc.presentation.screens.stock

import android.graphics.Paint
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
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
import com.victorhvs.tfc.presentation.theme.gain
import com.victorhvs.tfc.presentation.theme.loss
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
                        lineColor = stock.priceAbsoluteFloating.gainOrLossColor()
                    ) { interval ->
//                            LaunchedEffect(Unit) {
                        viewModel.fetchTimeSeries(stockId, interval)
//                            }
                    }

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
                    LineChart(
                        modifier = modifier,
                        timeSeries = timeseriesState.data,
                        lineColor = lineColor
                    )
                } else {
                    CandleChart(
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
    timeSeries: List<TimeSeries?>,
    lineColor: Color = Color.Black
) {
    val textSize = 12f
    val onPrimaryContainer = MaterialTheme.colorScheme.onPrimaryContainer.toArgb()

    AndroidView(
        modifier = modifier
            .fillMaxWidth(),
        factory = { ctx ->
            LineChart(ctx)
        },
        update = { lineChart ->
            var lineData: LineData? = null

            val entries = timeSeries.filter { !it!!.close.isNaN() }.mapIndexed { index, item ->
//                val value = floor(item!!.close * 100) / 100
                Entry(
                    index.toFloat(),
//                    item!!.close.toFloat()
                    item!!.close.toFloat()
                )
            }

            val leftAxisFormatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    return "$value VICTOR\nAEHO"
                }
            }

            val dataset = LineDataSet(entries, "").apply {
                color = lineColor.toArgb()
                valueFormatter = leftAxisFormatter
                lineWidth = 1f
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
                    valueFormatter = leftAxisFormatter
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
                    valueFormatter = leftAxisFormatter
                }
                axisRight.apply {
//                    axisMinimum = 0f
                    setDrawLabels(false)
                    setDrawGridLines(false)
                    setDrawBorders(false)
                    setDrawAxisLine(false)
                    valueFormatter = leftAxisFormatter
                }

                data = lineData

//                setVisibleXRangeMaximum(entries.size.div(3).toFloat())
//                moveViewToX(data.entryCount.toFloat())

                if (entries.isNotEmpty()) {
                    val highest = entries.maxBy { it.y }.y
                    val ll1 = LimitLine(highest, "R$${highest.toFormatedCurrency()}")
                    ll1.lineWidth = 1f
                    ll1.lineColor = onPrimaryContainer
                    ll1.enableDashedLine(10f, 10f, 0f)
                    ll1.labelPosition = LimitLabelPosition.RIGHT_TOP
                    ll1.textSize = 10f
                    ll1.textColor = onPrimaryContainer
//                ll1.typeface = tfRegular

                    val avg = floor(timeSeries.filter { !it!!.close.isNaN() }.map { it!!.close }
                        .average() * 100) / 100
                    val ll3 = LimitLine(avg.toFloat(), "R$${avg.toFormatedCurrency()}")
                    ll3.lineWidth = 1f
                    ll3.enableDashedLine(10f, 10f, 0f)
                    ll3.labelPosition = LimitLabelPosition.RIGHT_TOP
                    ll3.textSize = 10f
                    ll3.lineColor = onPrimaryContainer
                    ll3.textColor = onPrimaryContainer

                    val lowest = entries.minBy { it.y }.y
                    val ll2 = LimitLine(lowest, "R$${lowest.toFormatedCurrency()}")
                    ll2.lineWidth = 1f
                    ll2.enableDashedLine(10f, 10f, 0f)
                    ll2.labelPosition = LimitLabelPosition.RIGHT_BOTTOM
                    ll2.textSize = 10f
                    ll2.textColor = onPrimaryContainer
                    ll2.lineColor = onPrimaryContainer
//                ll2.typeface = tfRegular


                    // add limit lines
                    axisLeft.addLimitLine(ll1)
                    axisRight.addLimitLine(ll3)
                    axisRight.addLimitLine(ll2)
                }

                invalidate()
            }
        })
}

@Composable
fun CandleChart(
    modifier: Modifier = Modifier,
    timeSeries: List<TimeSeries?>
) {
    val textSize = 12f
    val onPrimaryContainer = MaterialTheme.colorScheme.onPrimaryContainer.toArgb()
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
    val gain = gain.toArgb()
    val loss = loss.toArgb()

    AndroidView(
        modifier = modifier
            .fillMaxWidth(),
        factory = { ctx ->
            CandleStickChart(ctx)
        },
        update = { candleChart ->
            var candleData: CandleData? = null

            val entries = timeSeries.filter {
                !it!!.close.isNaN() &&
                !it.high.isNaN() &&
                !it.low.isNaN() &&
                !it.open.isNaN()
            }.mapIndexed { index, item ->
                item?.let {
                    CandleEntry(
                        index.toFloat(),
                        item.high.toFloat(),
                        item.low.toFloat(),
                        item.open.toFloat(),
                        item.close.toFloat()
                    )
                }
            }

            val dataset = CandleDataSet(entries, "").apply {
                setDrawIcons(false)
                setDrawValues(false)
                axisDependency = AxisDependency.RIGHT
//        set1.setColor(Color.rgb(80, 80, 80));
                //        set1.setColor(Color.rgb(80, 80, 80));
                shadowColor = primaryColor
                shadowWidth = 0.7f
                decreasingColor = loss
                decreasingPaintStyle = Paint.Style.FILL_AND_STROKE
                increasingColor = gain
                increasingPaintStyle = Paint.Style.FILL_AND_STROKE
                neutralColor = primaryColor
            }
            candleData = CandleData(dataset)

            candleChart.apply {
//                resetTracking()

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
                }
                axisRight.apply {
//                    axisMinimum = 0f
                    setDrawLabels(false)
                    setDrawGridLines(false)
                    setDrawBorders(false)
                    setDrawAxisLine(false)
                }

                data = candleData
                invalidate()
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun StockScreenPrev() {
    TfcTheme {
//        StockScreen(stockId = "flry3", stockName = "Fleury", navigateBack = { }) {
//
//        }
//        LineChart(Modifier.fillMaxSize(), weekPrice)
        CandleChart(
            timeSeries = weekPrice
        )
    }
}