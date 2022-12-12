package com.victorhvs.tfc.presentation.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.victorhvs.tfc.R
import com.victorhvs.tfc.presentation.theme.TfcTheme
import com.victorhvs.tfc.presentation.theme.spacing

@Composable
fun AuthScreen() {
    Column(
        modifier = Modifier
            .padding(MaterialTheme.spacing.medium)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        AuthHeader()
        Providers()
        Footer()
    }
}

@Composable
fun Footer() {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Precisa de Ajuda?  •  Quer Contribuir?\n" +
                "Made with ❤️ in Piauí, BR",
        textAlign = TextAlign.Center,
    )
}

@Composable
fun Providers() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Button(modifier = Modifier.fillMaxWidth(),
            onClick = { }) {
            Text(text = "Continuar com Google")
        }
    }
}

@Composable
fun AuthHeader() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null
        )
        Text(
            text = "Bem vindo ao TradingFight.club",
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun AuthPreview() {
    TfcTheme {
        AuthScreen()
    }
}