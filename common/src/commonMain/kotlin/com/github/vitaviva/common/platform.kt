package com.github.vitaviva.common

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.Dp

expect fun getPlatformName(): String

expect val BLACK_CHESS_BMP : ImageBitmap

expect val WHITE_CHESS_BMP : ImageBitmap



//Scroll
expect val MARGIN_SCROLLBAR: Dp

expect interface ScrollbarAdapter

@Composable
expect fun rememberScrollbarAdapter(
    scrollState: LazyListState,
    itemCount: Int,
    averageItemSize: Dp
): ScrollbarAdapter

@Composable
expect fun VerticalScrollbar(
    modifier: Modifier,
    adapter: ScrollbarAdapter
)
