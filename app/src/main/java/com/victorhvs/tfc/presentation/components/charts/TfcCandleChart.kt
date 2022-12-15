package com.victorhvs.tfc.presentation.components.charts

import android.graphics.Paint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.victorhvs.tfc.data.fake.FakeDataSource
import com.victorhvs.tfc.domain.models.TimeSeries
import com.victorhvs.tfc.presentation.extensions.toFormatedCurrency
import com.victorhvs.tfc.presentation.theme.TfcTheme
import com.victorhvs.tfc.presentation.theme.gain
import com.victorhvs.tfc.presentation.theme.loss
import kotlin.math.floor

@Composable
fun TfcCandleChart(
    modifier: Modifier = Modifier,
    timeSeries: List<TimeSeries?>
) {
    val onPrimaryContainer = MaterialTheme.colorScheme.onPrimaryContainer
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
    val gain = gain.toArgb()
    val loss = loss.toArgb()

    val entriesNotNaN = remember { timeSeries.filterNotNull().filter { !it.close.isNaN() } }
    val mediumValue = remember {
        val medium = (floor(entriesNotNaN.map { it.close }.average().toFloat() * 100) / 100)
        if (medium.isNaN())
            0f
        else
            medium
    }
    val maxValue = remember { entriesNotNaN.maxByOrNull { it.high }?.high?.toFloat() ?: 0f }
    val minValue = remember { entriesNotNaN.maxByOrNull { it.low }?.low?.toFloat() ?: 0f }

    AndroidView(
        modifier = modifier
            .fillMaxWidth(),
        factory = { ctx ->
            CandleStickChart(ctx)
        },
        update = { candleChart ->
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
                axisDependency = YAxis.AxisDependency.RIGHT
                shadowColor = primaryColor
                shadowWidth = 0.7f
                decreasingColor = loss
                decreasingPaintStyle = Paint.Style.FILL_AND_STROKE
                increasingColor = gain
                increasingPaintStyle = Paint.Style.FILL_AND_STROKE
                neutralColor = primaryColor
            }

            candleChart.apply {
                legend.isEnabled = false
                setDrawGridBackground(false)
                setDrawBorders(false)
                setPinchZoom(false)
                setScaleEnabled(false)
                isDragEnabled = true
                description.isEnabled = false

                xAxis.defaults()
                axisRight.defaults()
                axisLeft.defaults()

                val mediumLabel = "R$${mediumValue.toFormatedCurrency()}"
                val mediumLimit = limitLine(mediumValue, mediumLabel, onPrimaryContainer)

                val maxLabel = "R$${maxValue.toFormatedCurrency()}"
                val highestLimit = limitLine(maxValue, maxLabel, onPrimaryContainer)

                val minLabel = "R$${minValue.toFormatedCurrency()}"
                val lowestLimit = limitLine(
                    minValue,
                    minLabel,
                    onPrimaryContainer,
                    LimitLine.LimitLabelPosition.RIGHT_BOTTOM
                )

                axisRight.addLimitLine(highestLimit)
                axisRight.addLimitLine(mediumLimit)
                axisRight.addLimitLine(lowestLimit)

                minOffset = 0f
                data = CandleData(dataset)
                invalidate()
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun CandleChartPreview() {
    TfcTheme {
        TfcCandleChart(
            modifier = Modifier.fillMaxSize(),
            timeSeries = FakeDataSource.weekPrice
        )

    }
}