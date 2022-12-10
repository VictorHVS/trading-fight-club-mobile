package com.victorhvs.tfc.presentation.screens.stock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.victorhvs.tfc.R
import com.victorhvs.tfc.data.fake.FakeDataSource
import com.victorhvs.tfc.domain.models.FirestoreState
import com.victorhvs.tfc.domain.models.Stock
import com.victorhvs.tfc.presentation.components.LogoRounded
import com.victorhvs.tfc.presentation.components.ProgressBar
import com.victorhvs.tfc.presentation.extensions.gainOrLossColor
import com.victorhvs.tfc.presentation.extensions.toFormatedCurrency
import com.victorhvs.tfc.presentation.theme.TfcTheme
import com.victorhvs.tfc.presentation.theme.spacing


@Composable
fun StockScreen(
    stockId: String,
    stockName: String,
    viewModel: StockViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    showSheet: () -> Unit,
) {

    val stockState by viewModel.stockState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stockId.uppercase())
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
    ) { paddingValues ->

        when(stockState) {
            is FirestoreState.Failed -> navigateBack()
            is FirestoreState.Loading -> ProgressBar()
            is FirestoreState.Success -> {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    val stock = (stockState as FirestoreState.Success<Stock?>).data ?: return@Scaffold
                    Header(stock = stock)
                    Chart(modifier = Modifier.weight(1f))
                    val userHasStock = true
                    if (userHasStock) {
                        UserStock(
                            amount = 10,
                            priceAvg = 300.19,
                            portfolioSum = 3_001.90,
                            profitAbsolute = 187.0,
                            profitRate = 2.01
                        )
                    }
                    BuySellButtons(userHasStock, showSheet = showSheet)
                }
            }
        }
    }
}

@Composable
fun BuySellButtons(
    userHasStock: Boolean,
    showSheet: () -> Unit
) {
    Row(
        modifier = Modifier.padding(
            horizontal = MaterialTheme.spacing.medium
        ),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        Button(modifier = Modifier.weight(1f), onClick = { showSheet() }) {
            Text(text = stringResource(id = R.string.buy_action))
        }
        if (userHasStock) {
            Button(modifier = Modifier.weight(1f), onClick = { showSheet() }) {
                Text(text = stringResource(id = R.string.sell_action))
            }
        }
    }
}

@Composable
fun UserStock(
    amount: Int,
    priceAvg: Double,
    portfolioSum: Double,
    profitAbsolute: Double,
    profitRate: Double
) {
    LazyVerticalGrid(
        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        item {
            Column {
                Text(text = "Quantidade", style = MaterialTheme.typography.labelMedium)
                Text(
                    text = amount.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            Column {
                Text(text = "Custo Médio", style = MaterialTheme.typography.labelMedium)
                Text(
                    text = "R$$priceAvg",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            Column {
                Text(text = "Valor total", style = MaterialTheme.typography.labelMedium)
                Text(
                    text = "R$$portfolioSum",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            Column {
                Text(text = "Lucro", style = MaterialTheme.typography.labelMedium)
                Text(
                    text = "R$$profitAbsolute ($profitRate)",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = profitAbsolute.gainOrLossColor()
                )
            }
        }
    }
}

@Composable
fun Header(
    stock: Stock,
    modifier: Modifier = Modifier
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
                text = stock.name,
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = "R$" + stock.price.toFormatedCurrency(),
                style = MaterialTheme.typography.headlineLarge
            )
            val priceText = remember {
                val absoluteFloat = stock.priceAbsoluteFloating.toFormatedCurrency(true)
                val priceRate = stock.priceFloating.toFormatedCurrency()

                mutableStateOf("R$$absoluteFloat | $priceRate%")
            }

            Text(
                text = priceText.value,
                color = stock.priceAbsoluteFloating.gainOrLossColor(),
                style = MaterialTheme.typography.labelSmall
            )
        }
        LogoRounded(stock.logoUrl)
    }
}

@Composable
fun Chart(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Gray)
    )
}

@Preview
@Composable
fun StockScreenPrev() {
    TfcTheme {
        StockScreen(stockId = "flry3", stockName = "Fleury", navigateBack = { }) {

        }
    }
}