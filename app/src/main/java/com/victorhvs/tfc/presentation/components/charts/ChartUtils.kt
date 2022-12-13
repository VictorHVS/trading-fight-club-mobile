package com.victorhvs.tfc.presentation.components.charts

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.LimitLine

fun limitLine(
    value: Float,
    label: String,
    lineColor: Color,
    labelPosition: LimitLine.LimitLabelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
): LimitLine {
    val limitLine = LimitLine(value, label)
    limitLine.lineWidth = 1f
    limitLine.enableDashedLine(10f, 10f, 0f)
    limitLine.labelPosition = labelPosition
    limitLine.textSize = 10f
    limitLine.textColor = lineColor.toArgb()
    limitLine.lineColor = lineColor.toArgb()
    return limitLine
}

fun AxisBase.defaults() {
    setDrawLabels(false)
    setDrawGridLines(false)
    setDrawAxisLine(false)
}