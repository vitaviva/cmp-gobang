//package com.github.vitaviva.common.wscom
//
//
//import io.ktor.application.*
//import io.ktor.network.selector.*
//import io.ktor.network.sockets.*
//import io.ktor.routing.*
//import io.ktor.util.*
//import io.ktor.websocket.*
//import io.rsocket.kotlin.ConnectionAcceptor
//import io.rsocket.kotlin.RSocketRequestHandler
//import io.rsocket.kotlin.core.RSocketServer
//import io.rsocket.kotlin.payload.buildPayload
//import io.rsocket.kotlin.payload.data
//import io.rsocket.kotlin.transport.ktor.server.RSocketSupport
//import io.rsocket.kotlin.transport.ktor.server.rSocket
//import kotlin.coroutines.CoroutineContext
//
//@OptIn(KtorExperimentalAPI::class, InternalAPI::class)
//suspend fun runTcpServer(dispatcher: CoroutineContext, port: Int) {
//    println("Start TCP server on 0.0.0.0:${port}")
//    val transport = aSocket(SelectorManager(dispatcher)).tcp().bind {
//
//    }
//    RSocketServer().bind(transport, pingPongAcceptor).join()
//}
//
//val pingPongAcceptor: ConnectionAcceptor = ConnectionAcceptor {
//    RSocketRequestHandler {
//        requestResponse {
//            buildPayload { data("Pong") }
//        }
//    }
//}
//
//fun Application.rsocket() {
//    install(WebSockets)
//    install(RSocketSupport)
//    routing {
//        rSocket("/rsocket", acceptor = pingPongAcceptor)
//    }
//    println("RSocket enabled!")
//}