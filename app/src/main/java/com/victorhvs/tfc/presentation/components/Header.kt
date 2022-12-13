package com.victorhvs.tfc.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.victorhvs.tfc.presentation.extensions.gainOrLossColor
import com.victorhvs.tfc.presentation.extensions.toFormatedCurrency
import com.victorhvs.tfc.presentation.theme.TfcTheme
import com.victorhvs.tfc.presentation.theme.spacing

@Composable
fun Header(
    name: String,
    price: Double,
    priceAbsoluteFloating: Double,
    priceFloating: Double,
    modifier: Modifier = Modifier,
    imageUrl: String? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = name,
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = "R$" + price.toFormatedCurrency(),
                style = MaterialTheme.typography.headlineLarge
            )
            val priceText = remember {
                val absoluteFloat = priceAbsoluteFloating.toFormatedCurrency(true)
                val priceRate = priceFloating.toFormatedCurrency()
                mutableStateOf("R$$absoluteFloat | $priceRate%")
            }

            Text(
                text = priceText.value,
                color = priceAbsoluteFloating.gainOrLossColor(),
                style = MaterialTheme.typography.labelSmall
            )
        }
        imageUrl?.let { LogoRounded(it) }
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    TfcTheme {
        Header(
            name = "Minha Carteira",
            price = 10_000.0,
            priceAbsoluteFloating = 2.04,
            priceFloating = 0.64
        )
    }
}