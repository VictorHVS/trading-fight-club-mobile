package com.victorhvs.tfc.presentation.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.victorhvs.tfc.R
import com.victorhvs.tfc.core.Resource
import com.victorhvs.tfc.domain.enums.FirestoreState
import com.victorhvs.tfc.domain.models.Order
import com.victorhvs.tfc.domain.models.User
import com.victorhvs.tfc.presentation.components.CardHorizontalOrder
import com.victorhvs.tfc.presentation.components.ProgressBar
import com.victorhvs.tfc.presentation.components.SectionTitle
import com.victorhvs.tfc.presentation.components.SimpleIndicator
import com.victorhvs.tfc.presentation.extensions.format
import com.victorhvs.tfc.presentation.extensions.gainOrLossColor
import com.victorhvs.tfc.presentation.extensions.toFormatedCurrency
import com.victorhvs.tfc.presentation.theme.spacing

@Composable
fun ProfileScreen(
    navigateToAuthScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {

    val userState by viewModel.userState.collectAsState()
    val orderState by viewModel.orders.collectAsState()

    val screenTitle = remember {
        mutableStateOf("")
    }

    when (val signOutResponse = viewModel.signOutResponse) {
        is Resource.Loading -> ProgressBar()
        is Resource.Success -> {
            LaunchedEffect(signOutResponse) {
                if (signOutResponse.data) {
                    navigateToAuthScreen()
                }
            }
        }

        is Resource.Failure -> LaunchedEffect(Unit) {
            print(signOutResponse.throwable)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(screenTitle.value)
                },
                actions = {
                    TextButton(onClick = {
                        viewModel.signOut()
                    }) {
                        Icon(imageVector = Icons.Outlined.Logout, contentDescription = null)
                    }
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            when (userState) {
                is FirestoreState.Failed -> {}
                is FirestoreState.Loading -> ProgressBar()
                is FirestoreState.Success -> {
                    val user =
                        (userState as FirestoreState.Success<User?>).data ?: return@Scaffold

                    screenTitle.value = user.username ?: user.uuid.substring(0..10)

                    UserIndicators(user, modifier)
                    OrdersHistory(
                        modifier = modifier.padding(horizontal = MaterialTheme.spacing.medium),
                        orderState = orderState
                    )
                }
            }
        }
    }
}

@Composable
fun UserIndicators(
    user: User,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {

        item {
            SimpleIndicator(
                label = R.string.portfolio,
                textValue = "R$${user.portfolio.first().sum.toFormatedCurrency()}",
                textColor = MaterialTheme.colorScheme.primary
            )
        }

        item {
            SimpleIndicator(
                label = R.string.performance,
                textValue = "${user.performance}%",
                textColor = user.performance.gainOrLossColor()
            )
        }

        item {
            SimpleIndicator(
                label = R.string.net_value,
                textValue = "R$${user.portfolio.first().netValue.toFormatedCurrency()}",
                textColor = MaterialTheme.colorScheme.primary
            )
        }

        item {
            SimpleIndicator(
                label = R.string.trades,
                textValue = user.trades.toString(),
                textColor = MaterialTheme.colorScheme.primary
            )
        }

        item {
            SimpleIndicator(
                label = R.string.rank_global,
                textValue = "# ${user.rankPosition}",
                textColor = MaterialTheme.colorScheme.primary
            )
        }

        item {
            SimpleIndicator(
                label = R.string.profitable,
                textValue = "${user.proftable}%",
                textColor = user.proftable.gainOrLossColor()
            )
        }
    }
}

@Composable
fun OrdersHistory(modifier: Modifier, orderState: FirestoreState<List<Order?>>) {
    SectionTitle(
        title = R.string.pending_orders,
        modifier = modifier.padding(top = MaterialTheme.spacing.medium)
    )

    when (orderState) {
        is FirestoreState.Failed -> {
            Text(
                modifier = modifier
                    .padding(vertical = MaterialTheme.spacing.medium)
                    .fillMaxWidth(),
                text = "Sem ordens",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall
            )
        }

        is FirestoreState.Loading -> {}
        is FirestoreState.Success -> {
            val orders = orderState.data ?: return

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.medium)
            ) {
                itemsIndexed(orders) { index, order ->
                    if (order == null)
                        return@itemsIndexed

                    val trailingText = when (order.executed) {
                        true -> order.createdAt.format()
                        false -> "Pendente"
                        null -> "Cancelada"
                    }
                    CardHorizontalOrder(
                        logoUrl = order.stockUrl ?: "",
                        amount = order.amount,
                        symbol = order.stockId,
                        isBuy = order.isBuy,
                        currency = order.currency,
                        unitPrice = order.unitPrice,
                        total_price = order.totalPrice,
                        trailingText = trailingText
                    )
                }
            }
        }
    }
}
