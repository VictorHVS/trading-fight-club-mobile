package com.victorhvs.tfc.presentation.screens.auth

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.victorhvs.tfc.R
import com.victorhvs.tfc.core.Resource
import com.victorhvs.tfc.domain.enums.AuthProvider
import com.victorhvs.tfc.presentation.components.ProgressBar
import com.victorhvs.tfc.presentation.theme.TfcTheme
import com.victorhvs.tfc.presentation.theme.spacing

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navigateToHome: () -> Unit
) {
    val context = LocalContext.current

    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(MaterialTheme.spacing.medium)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            AuthHeader()
            Providers(Modifier.fillMaxWidth(), signIn = {
                viewModel.signIn(it)
            })
            Footer(onClick = {
                openUrl(
                    url = "https://github.com/VictorHVS/trading-fight-club-mobile/issues",
                    context = context
                )
            })
        }
    }

    when (val signInResponse = viewModel.signInResponse) {
        is Resource.Loading -> ProgressBar()
        is Resource.Success -> signInResponse.data.let { signedIn ->
            LaunchedEffect(signedIn) {
                if (signedIn) {
                    navigateToHome()
                }
            }
        }

        is Resource.Failure -> LaunchedEffect(Unit) {
            print(signInResponse.throwable)
        }
    }

}

@Composable
fun Footer(onClick: () -> Unit) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        text = "Precisa de Ajuda?  •  Quer Contribuir?\n" +
                "Made with ❤️ in Piauí, BR",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.secondary),
    )
}

@Composable
fun Providers(
    modifier: Modifier,
    signIn: (AuthProvider) -> Unit
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth(),
            value = "",
            enabled = false,
            singleLine = true,
            onValueChange = { },
            label = { Text(stringResource(id = R.string.auth_email_label)) }
        )
        Button(
            modifier = modifier,
            enabled = false,
            onClick = { }
        ) {
            Text(text = stringResource(R.string.auth_provider_email))
        }
        Divider(modifier = modifier.padding(vertical = MaterialTheme.spacing.medium))
        Button(
            modifier = modifier,
            enabled = false,
            onClick = { }
        ) {
            Text(text = stringResource(R.string.auth_provider_google))
        }
        Button(
            modifier = modifier,
            enabled = false,
            onClick = { }) {
            Text(text = stringResource(R.string.auth_provider_github))
        }
        OutlinedButton(
            modifier = modifier.padding(top = MaterialTheme.spacing.medium),
            onClick = { signIn(AuthProvider.ANON) }
        ) {
            Text(text = stringResource(R.string.auth_provider_anon))
        }

        val annotatedString = buildAnnotatedString {
            append("Ao continuar, você reconhece que leu, entendeu e concorda com os ")
            withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                append("Termos & Condições")
            }
            append(" e ")
            withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                append("Política de Privacidade.")
            }
        }

        Text(
            modifier = modifier
                .padding(top = MaterialTheme.spacing.medium)
                .clickable { },
            text = annotatedString,
            textAlign = TextAlign.Center,
            style = MaterialTheme
                .typography
                .bodySmall
                .copy(color = MaterialTheme.colorScheme.secondary),
        )
    }
}

@Composable
fun AuthHeader() {
    Row(
        modifier = Modifier.padding(top = MaterialTheme.spacing.medium),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val res = if (isSystemInDarkTheme()) R.drawable.logo_white else R.drawable.logo_black
        Image(
            painter = painterResource(id = res),
            contentDescription = stringResource(id = R.string.app_name)
        )
        Text(
            text = stringResource(R.string.auth_welcome),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

fun openUrl(url: String, context: Context) {
    context.startActivity(
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AuthPreview() {
    TfcTheme {
        AuthScreen {

        }
    }
}