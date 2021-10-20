package com.github.vitaviva.common.platform

import io.rsocket.kotlin.payload.Payload
import kotlinx.coroutines.flow.Flow

expect suspend fun initWsConnect()
expect fun receiveFromRemote(): Flow<Payload>
expect suspend fun sendToRemote(payload: Payload)