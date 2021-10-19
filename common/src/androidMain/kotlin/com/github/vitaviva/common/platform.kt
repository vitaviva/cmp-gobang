package com.github.vitaviva.common

import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.vitaviva.common.net.client
import io.rsocket.kotlin.RSocket
import io.rsocket.kotlin.payload.Payload
import io.rsocket.kotlin.payload.buildPayload
import io.rsocket.kotlin.payload.data
import io.rsocket.kotlin.transport.ktor.client.rSocket
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow


actual fun getPlatformName(): String {
    return "Android"
}


private val _whiteChessBitmap by lazy {
    ImageBitmap.imageResource(_resources, _res.whiteChess)
}

private val _blackChessBitmap by lazy {
    ImageBitmap.imageResource(_resources, _res.blackChess)
}

val serverHost by lazy {
    _resources.getString(_res.serverHost)
}

actual val BLACK_CHESS_BMP: ImageBitmap
    get() = _whiteChessBitmap
actual val WHITE_CHESS_BMP: ImageBitmap
    get() = _blackChessBitmap

private lateinit var _resources: Resources
private lateinit var _res: Res

data class Res(
    @DrawableRes val whiteChess: Int,
    @DrawableRes val blackChess: Int,
    @StringRes val serverHost: Int
)

fun Context.initPlatformResource(res: Res) {
    _resources = resources
    _res = res
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


//connect to some url
private lateinit var rSocket: RSocket


actual suspend fun initWs() {
//    rSocket = client.rSocket("wss://$serverHost:9000/rsocket") //
    rSocket = client.rSocket(host = serverHost, port = 9000, path = "/rsocket")

    delay(1000)
    sendToRemote("say Hello")
}


actual fun remoteFlow(): Flow<Payload> {
    return _responseFlow
}

private lateinit var _requestFlow: MutableSharedFlow<Payload>
private lateinit var _responseFlow: Flow<Payload>
actual suspend fun sendToRemote(str: String) {

//    GlobalScope.launch {
//        rSocket.requestResponse(buildPayload { data("Init")})
//    }
    if (!::_requestFlow.isInitialized) {
        _requestFlow = MutableSharedFlow()
        _responseFlow = rSocket.requestChannel(buildPayload { data("Init") }, _requestFlow)
    }
    _requestFlow.emit(buildPayload { data(str) })
}
