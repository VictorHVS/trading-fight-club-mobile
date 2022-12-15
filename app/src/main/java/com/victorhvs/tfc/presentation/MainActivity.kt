package com.victorhvs.tfc.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.rememberNavController
import com.victorhvs.tfc.domain.use_case.UseCases
import com.victorhvs.tfc.presentation.navigation.RootNavigationGraph
import com.victorhvs.tfc.presentation.screens.auth.AuthViewModel
import com.victorhvs.tfc.presentation.theme.TfcTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
//    private val viewModel by viewModels<AuthViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TfcTheme {
                RootNavigationGraph(
                    navController = rememberNavController()
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun DefaultPreview() {
//    TfcTheme {
//        RootNavigationGraph(navController = rememberNavController(), ViewModel())
//    }
//}
