package com.github.vitaviva.common.platform

import androidx.compose.runtime.Composable


@Composable
expect fun AppDialog(content: String, onDismiss: () -> Unit)