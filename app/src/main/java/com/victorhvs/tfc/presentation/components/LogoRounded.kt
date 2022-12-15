package com.victorhvs.tfc.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.victorhvs.tfc.R
import com.victorhvs.tfc.presentation.theme.TfcTheme

@Composable
fun LogoRounded(
    url: String,
    modifier: Modifier = Modifier
) {
    Image(
        painter = rememberAsyncImagePainter(url),
        contentDescription = stringResource(id = R.string.logo_rounded),
        modifier = modifier
            .size(40.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape,
            )
            .clip(CircleShape),
    )
}

@Preview(showBackground = true)
@Composable
fun LogoRoundedPreview() {
    TfcTheme {
        LogoRounded(url = "")
    }
}