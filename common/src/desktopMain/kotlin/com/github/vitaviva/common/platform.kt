package com.github.vitaviva.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.vitaviva.common.net.startServer
import io.rsocket.kotlin.payload.Payload
import io.rsocket.kotlin.payload.buildPayload
import io.rsocket.kotlin.payload.data
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

actual fun getPlatformName(): String {
    return "Desktop"
}


private val _whiteChessBitmap by lazy {
    useResource("chess_white.png", ::loadImageBitmap)
}
private val _blackChessBitmap by lazy {
    useResource("chess_black.png", ::loadImageBitmap)
}

actual val BLACK_CHESS_BMP: ImageBitmap
    get() = _blackChessBitmap
actual val WHITE_CHESS_BMP: ImageBitmap
    get() = _whiteChessBitmap


//Scroll
actual val MARGIN_SCROLLBAR: Dp = 8.dp

actual typealias ScrollbarAdapter = androidx.compose.foundation.ScrollbarAdapter

@OptIn(ExperimentalFoundationApi::class)
@Composable
actual fun rememberScrollbarAdapter(
    scrollState: LazyListState,
    itemCount: Int,
    averageItemSize: Dp
): ScrollbarAdapter =
    rememberScrollbarAdapter(scrollState = scrollState)

@Composable
actual fun VerticalScrollbar(
    modifier: Modifier,
    adapter: ScrollbarAdapter
) {
    androidx.compose.foundation.VerticalScrollbar(
        modifier = modifier,
        adapter = adapter
    )
}


private lateinit var _requestFlow: Flow<Payload>
private lateinit var _responseFlow: MutableSharedFlow<Payload>

actual suspend fun initWs() {
    startServer().let {
        _requestFlow = it.first
        _responseFlow = it.second
    }
}

actual fun remoteFlow(): Flow<Payload> {
    return _requestFlow
}

actual suspend fun sendToRemote(str: String) {
    _responseFlow.emit(buildPayload { this.data(str) })
}