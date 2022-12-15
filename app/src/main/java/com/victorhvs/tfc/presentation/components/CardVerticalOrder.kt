package com.victorhvs.tfc.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.victorhvs.tfc.presentation.theme.TfcTheme
import com.victorhvs.tfc.presentation.theme.spacing

@Composable
fun CardVerticalOrder(
    modifier: Modifier = Modifier,
    logoUrl: String,
    isBuy: Boolean = true,
    amount: Int,
    symbol: String,
    total_price: Double
) {
    Card(
        modifier = modifier
            .requiredWidth(100.dp)
            .wrapContentHeight(),
    ) {
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .padding(
                    horizontal = MaterialTheme.spacing.medium,
                    vertical = MaterialTheme.spacing.small
                ),
            horizontalAlignment = Alignment.Start
        ) {
            val type = if (isBuy) "Compra" else "Venda"
            LogoRounded(
                url = logoUrl
            )
            Text(
                text = symbol,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = "R$$total_price",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "$type de $amount ações",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CardVerticalOrderPreview() {
    TfcTheme {
        CardVerticalOrder(
            logoUrl = "",
            isBuy = false,
            amount = 10,
            symbol = "FLRY3.SA",
            total_price = 123.4
        )
    }
}
