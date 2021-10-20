package com.github.vitaviva.common.platform

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource


actual val BlackStoneBmp: ImageBitmap by lazy {
    ImageBitmap.imageResource(_resources, _res.blackStone)
}
actual val WhiteStoneBmp: ImageBitmap by lazy {
    ImageBitmap.imageResource(_resources, _res.whiteStone)
}