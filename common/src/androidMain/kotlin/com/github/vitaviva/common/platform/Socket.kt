package com.github.vitaviva.common.platform

import com.github.vitaviva.common.net.client
import io.rsocket.kotlin.RSocket
import io.rsocket.kotlin.payload.Payload
import io.rsocket.kotlin.payload.buildPayload
import io.rsocket.kotlin.payload.data
import io.rsocket.kotlin.payload.metadata
import io.rsocket.kotlin.transport.ktor.client.rSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


//connect to some url
private lateinit var rSocket: RSocket
private lateinit var _requestFlow: MutableSharedFlow<Payload>
private lateinit var _responseFlow: Flow<Payload>

actual suspend fun initWsConnect() {
//    rSocket = client.rSocket("wss://$serverHost:9000/rsocket") //无效？
    rSocket = client.rSocket(host = serverHost, port = 9000, path = "/rsocket")
    if (!::_requestFlow.isInitialized) {
        _requestFlow = MutableSharedFlow()
        _responseFlow = rSocket.requestChannel(buildPayload { data("Init") }, _requestFlow)
//        GlobalScope.launch(Dispatchers.IO) {
//            delay(5000)
//            repeat(10) { it ->
//                _requestFlow.emit(buildPayload {
//                    println(">>>>>>> $it")
//                    metadata("PutChess")
//                    data("0,0")
//                })
//            }
//
//        }
    } else {
        throw RuntimeException("duplicated init")
    }
}

actual fun receiveFromRemote(): Flow<Payload> = _responseFlow
actual suspend fun sendToRemote(payload: Payload) = _requestFlow.emit(payload)
