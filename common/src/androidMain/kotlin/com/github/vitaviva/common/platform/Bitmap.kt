package com.github.vitaviva.common.platform

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource


actual val BlackChessBmp: ImageBitmap by lazy {
    ImageBitmap.imageResource(_resources, _res.blackChess)
}
actual val WhiteChessBmp: ImageBitmap by lazy {
    ImageBitmap.imageResource(_resources, _res.whiteChess)
}