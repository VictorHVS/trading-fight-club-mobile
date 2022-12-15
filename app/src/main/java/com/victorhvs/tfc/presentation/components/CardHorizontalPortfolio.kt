package com.victorhvs.tfc.presentation.components

import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.victorhvs.tfc.data.fake.FakeDataSource
import com.victorhvs.tfc.domain.models.Stock
import com.victorhvs.tfc.presentation.theme.TfcTheme

@Composable
fun CardHorizontalPortfolio(
    modifier: Modifier = Modifier,
    stock: Stock
) {
    Card(modifier = modifier) {
        ListItem(
            headlineText = { Text(stock.symbol) },
            supportingText = { Text("100 ações") },
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
    }
}

@Preview
@Composable
private fun CardHorizontalPortfolioPreview() {
    TfcTheme {
        CardHorizontalPortfolio(stock = FakeDataSource.flry3)
    }
}
