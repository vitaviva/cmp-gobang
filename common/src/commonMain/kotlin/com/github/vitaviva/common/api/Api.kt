package com.github.vitaviva.common.api

import androidx.compose.ui.unit.IntOffset
import com.github.vitaviva.common.api.Message.Companion.TypeChooseColor
import com.github.vitaviva.common.api.Message.Companion.TypeEndGame
import com.github.vitaviva.common.api.Message.Companion.TypePlaceStone
import com.github.vitaviva.common.api.Message.Companion.TypeSysInfo
import com.github.vitaviva.common.platform.initWsConnect
import com.github.vitaviva.common.platform.receiveFromRemote
import com.github.vitaviva.common.platform.sendToRemote
import io.rsocket.kotlin.payload.buildPayload
import io.rsocket.kotlin.payload.data
import io.rsocket.kotlin.payload.metadata
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


object Api {
    suspend fun connect() = initWsConnect()

    fun receiveMessage(): Flow<Message> = receiveFromRemote().map {
        when (it.metadata!!.readText()) {
            TypePlaceStone -> {
                val (x, y) = it.data.readText().split(",")
                Message.PlaceStone(IntOffset(x.toInt(), y.toInt()))
            }
            TypeChooseColor -> Message.ChooseColor(it.data.readText().toBoolean())
            TypeEndGame -> Message.EndGame
            TypeSysInfo -> Message.SysInfo(it.data.readText())
            else -> error("Unknown message !")
        }
    }

    suspend fun sendMessage(message: Message) =
        sendToRemote(buildPayload {
            metadata(message.type)
            data("$message")
        })
}