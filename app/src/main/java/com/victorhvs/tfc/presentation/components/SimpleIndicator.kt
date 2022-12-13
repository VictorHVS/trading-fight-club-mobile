package com.victorhvs.tfc.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.victorhvs.tfc.R
import com.victorhvs.tfc.presentation.theme.TfcTheme

@Composable
fun SimpleIndicator(
    @StringRes label: Int,
    textValue: String,
    textColor: Color
) {
    Column {
        Text(text = stringResource(id = label), style = MaterialTheme.typography.labelMedium)
        Text(
            text = textValue,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SimpleIndicatorPreview() {
    TfcTheme {
        SimpleIndicator(
            label = R.string.quantity,
            textValue = "1234",
            textColor = MaterialTheme.colorScheme.primary
        )
    }
}