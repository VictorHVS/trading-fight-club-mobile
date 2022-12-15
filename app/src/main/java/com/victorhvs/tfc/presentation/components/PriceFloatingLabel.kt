package com.victorhvs.tfc.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.victorhvs.tfc.presentation.extensions.gainOrLossColor
import com.victorhvs.tfc.presentation.extensions.toFormatedCurrency
import com.victorhvs.tfc.presentation.theme.TfcTheme

@Composable
fun PriceFloatingLabel(
    modifier: Modifier = Modifier,
    price: Double? = null,
    floatPercentage: Double? = null,
    floatAbsolute: Double? = null
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.End) {
        price?.let {
            Row {
                Text(
                    text = it.toFormatedCurrency(),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelSmall,
                    color = floatPercentage?.gainOrLossColor() ?: MaterialTheme.colorScheme.primary,
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
            floatAbsolute?.let {
                Text(
                    text = it.toFormatedCurrency(),
                    color = it.gainOrLossColor(),
                    style = MaterialTheme.typography.labelSmall,
                )
            }

            floatPercentage?.let {
                val text = if (floatAbsolute != null) {
                    "(${it.toFormatedCurrency()}%)"
                } else {
                    it.toFormatedCurrency(showSign = true)
                }
                Text(
                    text = text,
                    color = it.gainOrLossColor(),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PriceFloatingLabelPreview() {
    TfcTheme {
        PriceFloatingLabel(
            price = 123.0,
            floatPercentage = 333.2,
            floatAbsolute = 123.4
        )
    }
}