package com.github.vitaviva.common.platform

import com.github.vitaviva.common.api.Message
import com.github.vitaviva.common.net.startServer
import io.rsocket.kotlin.payload.Payload
import io.rsocket.kotlin.payload.buildPayload
import io.rsocket.kotlin.payload.data
import io.rsocket.kotlin.payload.metadata
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onStart


private lateinit var _requestFlow: Flow<Payload>
private lateinit var _responseFlow: MutableSharedFlow<Payload>

actual suspend fun initWsConnect() {
    startServer().let {
        _requestFlow = it.first
        _responseFlow = it.second
    }
}

actual fun receiveFromRemote(): Flow<Payload> = _requestFlow.onStart {
    emit(buildPayload {
        metadata(Message.TypeSysInfo)
        data("waiting pair ...")
    })
}

actual suspend fun sendToRemote(payload: Payload) = _responseFlow.emit(payload)