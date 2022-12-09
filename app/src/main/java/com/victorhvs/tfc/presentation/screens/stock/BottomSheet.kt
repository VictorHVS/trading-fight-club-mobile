package com.victorhvs.tfc.presentation.screens.stock

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun TfcBottomSheet(showFeed: () -> Unit, showAnotherSheet: () -> Unit, arg: String) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Sheet with arg: $arg")
        Button(onClick = showFeed) {
            Text("Click me to navigate!")
        }
        Button(onClick = showAnotherSheet) {
            Text("Click me to show another sheet!")
        }
    }
}