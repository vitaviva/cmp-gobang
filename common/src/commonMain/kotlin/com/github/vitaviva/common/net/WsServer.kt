package com.github.vitaviva.common.net

import io.ktor.application.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.util.*
import io.ktor.websocket.*
import io.rsocket.kotlin.ConnectionAcceptor
import io.rsocket.kotlin.PrefetchStrategy
import io.rsocket.kotlin.RSocketRequestHandler
import io.rsocket.kotlin.core.RSocketServer
import io.rsocket.kotlin.payload.Payload
import io.rsocket.kotlin.payload.buildPayload
import io.rsocket.kotlin.payload.data
import io.rsocket.kotlin.payload.metadata
import io.rsocket.kotlin.transport.ktor.server.RSocketSupport
import io.rsocket.kotlin.transport.ktor.server.rSocket
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@OptIn(InternalAPI::class)
fun startServer(): Pair<Flow<Payload>, MutableSharedFlow<Payload>> {

    val _request = MutableSharedFlow<Payload>()
    val _response = MutableSharedFlow<Payload>()
//
//    val server = RSocketServer().bind(TcpServerTransport(port = 8000)) {
//        RSocketRequestHandler {
//            requestChannel { init, request ->
//                println("Init with: ${init.data.readText()}")
//                request.flowOn(PrefetchStrategy(3, 0)).take(3).flatMapConcat { payload ->
//                    val data = payload.data.readText()
//                    flow {
//                        repeat(3) {
//                            emit(Payload("$data(copy $it)"))
//                        }
//                    }
//                }
//            }
//        }
//    }

    val acceptor = ConnectionAcceptor {
        RSocketRequestHandler {
            //handler for request/response
            requestResponse { request: Payload ->
                //... some work here
                throw RuntimeException("request received ！！！！:${request.data}")
                delay(500) // work emulation
                buildPayload {
                    data("data")
                    metadata("metadata")
                }
            }
            //handler for request/channel


            requestChannel { init, request ->
                println("Init with: ${init.data.readText()}")
                GlobalScope.launch {
                    request.flowOn(PrefetchStrategy(3, 0)).collect {
                        _request.emit(it)
                    }
                }
                _response
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