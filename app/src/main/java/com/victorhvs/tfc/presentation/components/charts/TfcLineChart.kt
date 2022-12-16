package com.victorhvs.tfc.presentation.components.charts

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.victorhvs.tfc.data.fake.FakeDataSource.weekPrice
import com.victorhvs.tfc.domain.models.TimeSeries
import com.victorhvs.tfc.presentation.extensions.toFormatedCurrency
import com.victorhvs.tfc.presentation.theme.TfcTheme
import kotlin.math.floor

@Composable
fun TfcLineChart(
    modifier: Modifier = Modifier,
    timeSeries: List<TimeSeries?>,
    lineColor: Color = Color.Black
) {
    val onPrimaryContainer = MaterialTheme.colorScheme.onPrimaryContainer

    val entriesNotNaN = remember { timeSeries.filterNotNull().filter { !it.close.isNaN() } }
    val mediumValue = remember {
        floor(entriesNotNaN.map { it.close }.average().toFloat() * 100) / 100
    }
    val maxValue = remember { entriesNotNaN.maxBy { it.close }.close.toFloat() }
    val minValue = remember { entriesNotNaN.minBy { it.close }.close.toFloat() }

    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { ctx ->
            LineChart(ctx)
        },
        update = { lineChart ->
            val entries = timeSeries
                .filter { !it!!.close.isNaN() }
                .mapIndexed { index, item ->
                    Entry(
                        index.toFloat(),
                        item!!.close.toFloat()
                    )
                }

            val dataset = LineDataSet(entries, "").apply {
                color = lineColor.toArgb()
                lineWidth = 1f
                setDrawCircles(false)
                setDrawValues(false)
            }

            lineChart.apply {
                legend.isEnabled = false
                setDrawGridBackground(false)
                setDrawBorders(false)
                setPinchZoom(false)
                setScaleEnabled(false)
                isDragEnabled = true
                description.isEnabled = false
                minOffset = 0f

                xAxis.defaults()
                axisLeft.defaults()
                axisRight.defaults()

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

                data = LineData(dataset)
                invalidate()
            }
        })
}

@Preview(showBackground = true)
@Composable
private fun LineChartPreview() {
    TfcTheme {
        TfcLineChart(Modifier.fillMaxSize(), weekPrice)
    }
}