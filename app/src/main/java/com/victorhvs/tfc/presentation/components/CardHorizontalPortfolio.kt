package com.victorhvs.tfc.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.victorhvs.tfc.data.fake.FakeDataSource
import com.victorhvs.tfc.domain.models.Portfolio
import com.victorhvs.tfc.presentation.theme.TfcTheme

@Composable
fun CardHorizontalPortfolio(
    modifier: Modifier = Modifier,
    portfolio: Portfolio
) {
    Card(modifier = modifier) {
        ListItem(
            colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.background(Color.Red),
            headlineText = {
                Text(
                    text = portfolio.stockId,
                    style = MaterialTheme.typography.labelMedium
                )
            },
            supportingText = {
                Text(
                    text = "${portfolio.amount} ações",
                    style = MaterialTheme.typography.labelSmall
                )
            },
            trailingContent = {
                PriceFloatingLabel(
                    price = portfolio.totalSpent,
                    floatAbsolute = portfolio.mediumPrice
                )
            },
            leadingContent = {
                LogoRounded(
                    url = portfolio.stockUrl
                )
            },
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun CardHorizontalPortfolioPreview() {
    TfcTheme {
        CardHorizontalPortfolio(portfolio = FakeDataSource.portfolio)
    }
}
