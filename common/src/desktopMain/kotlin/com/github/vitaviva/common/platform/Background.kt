package com.github.vitaviva.common.platform

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
actual fun BgBox(modifier: Modifier, content: @Composable () -> Unit) {
    Box(modifier) {
        Image(
            painter = painterResource("board_bg.jpeg"),
            "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        content()
    }
}