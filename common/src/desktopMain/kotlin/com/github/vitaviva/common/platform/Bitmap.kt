package com.github.vitaviva.common.platform

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource


actual val BlackStoneBmp: ImageBitmap by lazy {
    useResource("stone_black.png", ::loadImageBitmap)
}
actual val WhiteStoneBmp: ImageBitmap by lazy {
    useResource("stone_white.png", ::loadImageBitmap)
}

