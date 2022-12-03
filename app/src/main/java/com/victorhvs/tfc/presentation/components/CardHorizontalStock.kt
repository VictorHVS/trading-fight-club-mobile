package com.victorhvs.tfc.presentation.components

import android.content.res.Configuration
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.victorhvs.tfc.R
import com.victorhvs.tfc.data.fake.FakeDataSource
import com.victorhvs.tfc.domain.models.Stock
import com.victorhvs.tfc.presentation.extensions.gainOrLossColor
import com.victorhvs.tfc.presentation.extensions.toFormatedCurrency

@Composable
fun CardHorizontalStock(
    stock: Stock,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ListItem(
            headlineText = { Text(stock.name) },
            overlineText = { Text(stock.symbol) },
            trailingContent = {
                PriceFloatingLabel(
                    floatAbsolute = stock.priceAbsoluteFloating,
                    floatPercentage = stock.priceFloating
                )
            },
            leadingContent = {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(stock.logoUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = stock.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
            }
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
                    color = it.gainOrLossColor()
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
            floatAbsolute?.let {
                Text(
                    text = it.toFormatedCurrency(showSign = true),
                    color = it.gainOrLossColor(),
                    style = MaterialTheme.typography.labelSmall
                )
            }

            floatPercentage?.let {
                val text = if (floatAbsolute != null) "(${it.toFormatedCurrency()})"
                else it.toFormatedCurrency()
                Text(
                    text = text,
                    color = it.gainOrLossColor(),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CardHorizontalStockPreview() {
    CardHorizontalStock(stock = FakeDataSource.flry3)
}