package com.github.vitaviva.common.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun BgBox(modifier: Modifier, content: @Composable () -> Unit)