package com.victorhvs.tfc.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.victorhvs.tfc.data.fake.FakeDataSource
import com.victorhvs.tfc.domain.models.Stock
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
                LogoRounded(
                    url = stock.logoUrl
                )
            },
        )
        Divider()
    }
}

@Preview
@Composable
private fun CardHorizontalStockPreview() {
    TfcTheme {
        CardHorizontalStock(stock = FakeDataSource.flry3, onStockClicked = {})
    }
}
