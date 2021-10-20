package com.github.vitaviva.common.platform

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterialApi::class)
@Composable
actual fun AppDialog(content: String, onDismiss: () -> Unit) {

    AlertDialog(
        modifier = Modifier.width(250.dp),
        onDismissRequest = onDismiss,
        title = {
            Text(text = "GoBang Desktop")
        },
        text = {
            Text(content)
        },
        buttons = {
            Box(modifier = Modifier.padding(all = 8.dp)) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onDismiss
                ) {
                    Text("OK")
                }
            }

        }
    )
}