package com.github.vitaviva.common.platform

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource


actual val BlackChessBmp: ImageBitmap by lazy {
    useResource("chess_black.png", ::loadImageBitmap)
}
actual val WhiteChessBmp: ImageBitmap by lazy {
    useResource("chess_white.png", ::loadImageBitmap)
}

