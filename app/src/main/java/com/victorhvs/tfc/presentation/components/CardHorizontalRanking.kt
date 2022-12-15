package com.victorhvs.tfc.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.victorhvs.tfc.R
import com.victorhvs.tfc.data.fake.FakeDataSource
import com.victorhvs.tfc.domain.models.User
import com.victorhvs.tfc.presentation.theme.TfcTheme

@Composable
fun CardHorizontalRanking(
    modifier: Modifier = Modifier,
    user: User,
    position: Int
) {
    Column(modifier = modifier) {
        ListItem(
            headlineText = { Text(user.name ?: stringResource(id = R.string.anon_name)) },
            overlineText = { Text(user.username ?: "@${user.uuid.substring(0..10)}") },
            trailingContent = {
                user.portfolio.firstOrNull()?.let {
                    PriceFloatingLabel(
                        floatAbsolute = it.priceFlutuationAbsolute,
                        floatPercentage = it.priceFlutuation,
                    )
                }
            },
            leadingContent = {
                CharRounded(text = position.toString())
            },
        )
        Divider()
    }
}

@Composable
fun CharRounded(
    text: String,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .size(40.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview
@Composable
private fun CardHorizontalStockPreview() {
    TfcTheme {
        CardHorizontalRanking(user = FakeDataSource.user, position = 1)
    }
}
