package com.github.vitaviva.common.api

import androidx.compose.ui.unit.IntOffset


sealed class Message(val type: String) {

    companion object {
        val TypeChooseColor = "ChooseColor"
        val TypePutChess = "PutChess"
        val TypeEndGame = "EndGame"
        val TypeSysInfo = "SysInfo"
    }

    data class ChooseColor(val isWhite: Boolean) :
        Message(TypeChooseColor) {
        override fun toString(): String {
            return isWhite.toString()
        }
    }

    data class PutChess(val offset: IntOffset) :
        Message(TypePutChess) {
        override fun toString(): String {
            return "${offset.x},${offset.y}"
        }
    }

    object EndGame : Message(TypeEndGame) {
        override fun toString(): String {
            return "game end"
        }
    }

    data class SysInfo(val info: String) : Message(TypeSysInfo) {
        override fun toString(): String {
            return info
        }
    }

}
