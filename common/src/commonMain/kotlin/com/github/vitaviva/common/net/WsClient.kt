package com.github.vitaviva.common.net

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.websocket.*
import io.rsocket.kotlin.RSocket
import io.rsocket.kotlin.RSocketRequestHandler
import io.rsocket.kotlin.core.RSocketConnector
import io.rsocket.kotlin.keepalive.KeepAlive
import io.rsocket.kotlin.payload.Payload
import io.rsocket.kotlin.payload.PayloadMimeType
import io.rsocket.kotlin.payload.buildPayload
import io.rsocket.kotlin.payload.data
import io.rsocket.kotlin.transport.ktor.client.RSocketSupport
import io.rsocket.kotlin.transport.ktor.client.rSocket
import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.minutes
import kotlin.time.seconds

@OptIn(ExperimentalTime::class)
val client by lazy {
    HttpClient(CIO) {
        install(WebSockets)
        install(RSocketSupport) {
            connector = RSocketConnector {
                //configure rSocket connector (all values have defaults)
                connectionConfig {
                    keepAlive = KeepAlive(
                        interval = Duration.seconds(30),
                        maxLifetime = Duration.minutes(2)
                    )

                    //payload for setup frame
                    setupPayload { buildPayload { data("hello") } }

//                //mime types
//                payloadMimeType = PayloadMimeType(
//                    data = "application/json",
//                    metadata = "application/json"
//                )
                }

                //optional acceptor for server requests
                acceptor {
                    RSocketRequestHandler {
                        requestResponse { it } //echo request payload
                    }
                }
            }
        }
    }

}