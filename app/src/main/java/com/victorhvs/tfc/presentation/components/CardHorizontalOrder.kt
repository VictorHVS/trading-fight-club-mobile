package com.victorhvs.tfc.presentation.components

import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.victorhvs.tfc.presentation.extensions.format
import com.victorhvs.tfc.presentation.theme.TfcTheme
import java.util.Date

@Composable
fun CardHorizontalOrder(
    modifier: Modifier = Modifier,
    logoUrl: String,
    isBuy: Boolean = true,
    amount: Int,
    symbol: String,
    currency: String,
    unitPrice: Double,
    total_price: Double,
    trailingText: String
) {
    Card(
        modifier = modifier.wrapContentHeight()
    ) {

        val type = if (isBuy) "Compra" else "Venda"

        ListItem(
            headlineText = { Text("$type $amount ($symbol)") },
            supportingText = { Text("$total_price$currency à $unitPrice$currency") },
            trailingContent = {
                Text(
                    text = trailingText
                )
            },
            leadingContent = {
                LogoRounded(
                    url = logoUrl
                )
            },
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CardHorizontalOrderPreview() {
    TfcTheme {
        CardHorizontalOrder(
            logoUrl = "",
            isBuy = false,
            amount = 10,
            symbol = "FLRY3.SA",
            currency = "BRL",
            unitPrice = 12.34,
            total_price = 123.4,
            trailingText = Date().format()
        )
    }
}
