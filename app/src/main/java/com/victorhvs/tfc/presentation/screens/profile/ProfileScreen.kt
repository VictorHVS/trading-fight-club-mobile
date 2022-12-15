package com.victorhvs.tfc.presentation.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.victorhvs.tfc.R
import com.victorhvs.tfc.domain.enums.FirestoreState
import com.victorhvs.tfc.domain.models.User
import com.victorhvs.tfc.presentation.components.ProgressBar
import com.victorhvs.tfc.presentation.components.SimpleIndicator
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
    val screenTitle = remember {
        mutableStateOf("")
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(screenTitle.value)
                },
                actions = {
                    TextButton(onClick = { navigateToAuthScreen() }) {
                        Icon(imageVector = Icons.Outlined.Logout, contentDescription = null)
                    }
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(MaterialTheme.spacing.medium)
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
