package com.victorhvs.tfc.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.victorhvs.tfc.data.fake.FakeDataSource
import com.victorhvs.tfc.domain.models.Stock
import com.victorhvs.tfc.presentation.extensions.gainOrLossColor
import com.victorhvs.tfc.presentation.extensions.toFormatedCurrency
import com.victorhvs.tfc.presentation.theme.TfcTheme

@Composable
fun CardHorizontalStock(
    modifier: Modifier = Modifier,
    stock: Stock,
    onStockClicked: (stock: Stock) -> Unit
) {
    Column(modifier = modifier.clickable { onStockClicked(stock) }) {
        ListItem(
            headlineText = { Text(stock.name) },
            overlineText = { Text(stock.symbol) },
            trailingContent = {
                PriceFloatingLabel(
                    price = stock.price,
                    floatAbsolute = stock.priceAbsoluteFloating,
                    floatPercentage = stock.priceFloating,
                )
            },
            leadingContent = {
                Image(
                    painter = rememberAsyncImagePainter(stock.logoUrl),
                    contentDescription = stock.name,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape,
                        )
                        .clip(CircleShape),
                )
            },
        )
        Divider()
    }
}

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

@Preview
@Composable
private fun CardHorizontalStockPreview() {
    TfcTheme {
        CardHorizontalStock(stock = FakeDataSource.flry3, onStockClicked = {})
    }
}
