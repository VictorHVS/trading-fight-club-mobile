package com.victorhvs.tfc.presentation.screens.wallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.victorhvs.tfc.R
import com.victorhvs.tfc.domain.enums.FirestoreState
import com.victorhvs.tfc.domain.models.Order
import com.victorhvs.tfc.domain.models.Portfolio
import com.victorhvs.tfc.domain.models.User
import com.victorhvs.tfc.presentation.components.CardHorizontalPortfolio
import com.victorhvs.tfc.presentation.components.CardVerticalOrder
import com.victorhvs.tfc.presentation.components.Header
import com.victorhvs.tfc.presentation.components.SectionTitle
import com.victorhvs.tfc.presentation.theme.spacing

@Composable
fun WalletScreen(
    modifier: Modifier = Modifier,
    viewModel: WalletViewModel = hiltViewModel()
) {
    val userState by viewModel.userState.collectAsState()
    val orderState by viewModel.orders.collectAsState()
    val portfolioState by viewModel.portfolios.collectAsState()

    Scaffold(
        modifier = modifier,
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            WalletHeader(modifier, userState)
            PendingOrders(
                modifier = modifier.padding(horizontal = MaterialTheme.spacing.medium),
                orderState = orderState
            )
            Portfolios(
                modifier = modifier.padding(horizontal = MaterialTheme.spacing.medium),
                portfolioState = portfolioState
            )
        }
    }
}

@Composable
fun PendingOrders(modifier: Modifier, orderState: FirestoreState<List<Order?>>) {
    SectionTitle(
        title = R.string.pending_orders,
        modifier = modifier
    )

    when (orderState) {
        is FirestoreState.Failed -> {
            Text(
                modifier = modifier
                    .padding(vertical = MaterialTheme.spacing.medium)
                    .fillMaxWidth(),
                text = "Sem ordens pendentes",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall
            )
        }

        is FirestoreState.Loading -> {}
        is FirestoreState.Success -> {
            val orders = orderState.data ?: return

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                modifier = Modifier.padding(vertical = MaterialTheme.spacing.medium)
            ) {
                item {
                    Spacer(
                        modifier = Modifier.padding(MaterialTheme.spacing.small)
                    )
                }
                itemsIndexed(orders) { index, order ->
                    CardVerticalOrder(
                        logoUrl = order!!.stockUrl ?: "",
                        amount = order.amount,
                        symbol = order.stockId,
                        totalPrice = order.totalPrice
                    )
                }
            }
        }
    }
}

@Composable
fun Portfolios(modifier: Modifier, portfolioState: FirestoreState<List<Portfolio?>>) {
    SectionTitle(
        title = R.string.my_stocks,
        modifier = modifier
    )

    when (portfolioState) {
        is FirestoreState.Failed -> {
            Text(
                modifier = modifier
                    .padding(vertical = MaterialTheme.spacing.medium)
                    .fillMaxWidth(),
                text = "Compre uma ação no através do menu explorar",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall
            )
        }

        is FirestoreState.Loading -> {}
        is FirestoreState.Success -> {
            val portfolios = portfolioState.data ?: return

            LazyColumn(
                modifier = Modifier.padding(MaterialTheme.spacing.medium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                itemsIndexed(portfolios) { index, portfolio ->
                    CardHorizontalPortfolio(portfolio = portfolio!!)
                }
            }
        }
    }
}

@Composable
fun WalletHeader(modifier: Modifier, userState: FirestoreState<User?>) {
    when (userState) {
        is FirestoreState.Failed -> {}
        is FirestoreState.Loading -> {}
        is FirestoreState.Success -> {
            val user = userState.data ?: return
            Header(
                modifier = modifier.padding(vertical = 16.dp),
                name = "Minha carteira",
                price = user.portfolio.first().sum,
                priceAbsoluteFloating = user.portfolio.first().priceFlutuationAbsolute,
                priceFloating = user.portfolio.first().priceFlutuation
            )
        }
    }
}