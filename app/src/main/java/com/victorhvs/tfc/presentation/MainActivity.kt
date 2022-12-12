package com.victorhvs.tfc.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.victorhvs.tfc.presentation.navigation.RootNavigationGraph
import com.victorhvs.tfc.presentation.theme.TfcTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            WindowCompat.setDecorFitsSystemWindows(window, false)
            TfcTheme {
                RootNavigationGraph(navController = rememberNavController())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    TfcTheme {
        RootNavigationGraph(navController = rememberNavController())
    }
}
