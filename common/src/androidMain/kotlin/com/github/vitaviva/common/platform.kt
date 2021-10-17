package com.github.vitaviva.common

import android.content.res.Resources
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


actual fun getPlatformName(): String {
    return "Android"
}


private var _whiteChessRes: Int = 0
private var _blackChessRes: Int = 0
private lateinit var _resources : Resources

private val _whiteChessBitmap by lazy {
    ImageBitmap.imageResource(_resources, _whiteChessRes)
}

private val _blackChessBitmap by lazy {
    ImageBitmap.imageResource(_resources, _blackChessRes)
}

actual val BLACK_CHESS_BMP: ImageBitmap
    get() = _whiteChessBitmap
actual val WHITE_CHESS_BMP: ImageBitmap
    get() = _blackChessBitmap


fun initPlatformResource(resource: Resources, whiteChess: Int, blackChess: Int) {
    _resources = resource
    _whiteChessRes = whiteChess
    _blackChessRes = blackChess
}



//Scroll
actual val MARGIN_SCROLLBAR: Dp = 0.dp

actual interface ScrollbarAdapter

@Composable
actual fun rememberScrollbarAdapter(
    scrollState: LazyListState,
    itemCount: Int,
    averageItemSize: Dp
): ScrollbarAdapter =
    object : ScrollbarAdapter {}

@Composable
actual fun VerticalScrollbar(
    modifier: Modifier,
    adapter: ScrollbarAdapter
) {
}
