package com.victorhvs.tfc.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.victorhvs.tfc.R
import com.victorhvs.tfc.presentation.theme.TfcTheme

@Composable
fun SectionTitle(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    actionButton: @Composable () -> Unit = { },
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = title),
            style = MaterialTheme.typography.titleMedium
        )
        actionButton()
    }
}

@Preview(showBackground = true)
@Composable
fun SectionTitlePreview() {
    TfcTheme {
        SectionTitle(
            title = R.string.history,
            actionButton = {
                IconButton(onClick = {

                }) {
                    Icon(imageVector = Icons.Outlined.ArrowForward, contentDescription = null)
                }
            }
        )
    }
}