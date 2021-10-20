package com.github.vitaviva.common.net

import com.github.vitaviva.common.api.Message.Companion.TypeSysInfo
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.util.*
import io.ktor.websocket.*
import io.rsocket.kotlin.ConnectionAcceptor
import io.rsocket.kotlin.ExperimentalStreamsApi
import io.rsocket.kotlin.PrefetchStrategy
import io.rsocket.kotlin.RSocketRequestHandler
import io.rsocket.kotlin.core.RSocketServer
import io.rsocket.kotlin.payload.Payload
import io.rsocket.kotlin.payload.buildPayload
import io.rsocket.kotlin.payload.data
import io.rsocket.kotlin.payload.metadata
import io.rsocket.kotlin.transport.ktor.server.RSocketSupport
import io.rsocket.kotlin.transport.ktor.server.rSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalStreamsApi
@OptIn(InternalAPI::class)
fun startServer(): Pair<MutableSharedFlow<Payload>, MutableSharedFlow<Payload>> {

    val _request = MutableSharedFlow<Payload>()
    val _response = MutableSharedFlow<Payload>()

    val acceptor = ConnectionAcceptor {
        RSocketRequestHandler {
            //handler for request/channel
            requestChannel { init, request ->
                println("Init with: ${init.data.readText()}")
                _request.emit(buildPayload {
                    metadata(TypeSysInfo)
                    data("pairing ...")
                })
                delay(1000)
                request.onStart {
                    emit(buildPayload {
                        metadata(TypeSysInfo)
                        data("paired successfully")
                    })
                }.onEach {
                    _request.emit(it)
                }.flatMapLatest {
                    _response
                }
            }
        }
    }
//    val rSocketServer = RSocketServer()
//    rSocketServer.bind(
//        TcpServerTransport(
//            ActorSelectorManager(Dispatchers.IO), port = 9000, hostname = "192.168.3.135"/*"10.78.57.44"*/
//        ), acceptor
//    )

    //create ktor server
    embeddedServer(CIO, port = 9000) {
        install(WebSockets)
        install(RSocketSupport) {
            server = RSocketServer()
        }
        //configure routing
        routing {
            //configure route `url:port/rsocket`
            rSocket("rsocket", acceptor = acceptor)
        }
    }.start(false)
    return _request to _response
}