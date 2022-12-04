package com.victorhvs.tfc.presentation.extensions

import androidx.compose.ui.graphics.Color
import com.victorhvs.tfc.presentation.theme.gain
import com.victorhvs.tfc.presentation.theme.loss

fun Double.gainOrLossColor(): Color = if (this >= 0) gain else loss
