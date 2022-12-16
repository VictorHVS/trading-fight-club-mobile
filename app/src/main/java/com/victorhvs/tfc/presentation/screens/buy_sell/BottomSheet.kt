package com.victorhvs.tfc.presentation.screens.stock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.victorhvs.tfc.R
import com.victorhvs.tfc.presentation.extensions.toFormatedCurrency
import com.victorhvs.tfc.presentation.screens.buy_sell.BuySellViewModel
import com.victorhvs.tfc.presentation.theme.TfcTheme
import com.victorhvs.tfc.presentation.theme.spacing

@Composable
fun TfcBottomSheet(
    modifier: Modifier = Modifier,
    stockId: String,
    isBuy: Boolean,
    currentPrice: Double,
    netValue: Double,
    viewModel: BuySellViewModel = hiltViewModel(),
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val quantityText = remember { mutableStateOf("0") }

    val quantity = remember { mutableStateOf(0) }
    val subtotal = quantity.value * currentPrice
    val finalNetValue = netValue - subtotal

    val type = if (isBuy) "Comprar" else "Vender"

    Column(
        modifier = modifier
            .background(
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                color = MaterialTheme.colorScheme.surface
            )
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .padding(MaterialTheme.spacing.medium)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
        Text(text = "$type $stockId", style = MaterialTheme.typography.headlineLarge)
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = quantityText.value,
            singleLine = true,
            onValueChange = {
                quantityText.value = it
                it.toIntOrNull()?.let {
                    quantity.value = it
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            label = { Text(stringResource(id = R.string.buy_sell_quantity)) }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = currentPrice.toFormatedCurrency(),
            enabled = false,
            onValueChange = { },
            leadingIcon = {
                Text(text = "R$")
            },
            label = { Text(stringResource(id = R.string.buy_sell_unit_price)) }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "$type a mercado")
            Switch(checked = true, onCheckedChange = {}, enabled = false)
        }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = subtotal.toFormatedCurrency(),
            enabled = false,
            leadingIcon = {
                Text(text = "R$")
            },
            onValueChange = { },
            label = { Text(stringResource(id = R.string.buy_sell_total_price)) }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = finalNetValue.toFormatedCurrency(),
            enabled = false,
            leadingIcon = {
                Text(text = "R$")
            },
            onValueChange = { },
            label = { Text(stringResource(id = R.string.buy_sell_net_value)) }
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = finalNetValue >= 0,
            onClick = { }) {
            Text(type)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TfcBottomSheetPreview() {
    TfcTheme {
        TfcBottomSheet(
            stockId = "FLRY3",
            isBuy = true,
            currentPrice = 1.82,
            netValue = 10_000.0
        )
    }
}

