package com.github.vitaviva.common.api

import androidx.compose.ui.unit.IntOffset


sealed class Message(val type: String) {

    companion object {
        val TypeChooseColor = "ChooseColor"
        val TypePlaceStone = "PlaceStone"
        val TypeGameQuit = "GameQuit"
        val TypeGameReset = "GameReset"
        val TypeGameLog = "GameLog"
    }

    data class ChooseColor(val isWhite: Boolean) :
        Message(TypeChooseColor) {
        override fun toString(): String {
            return isWhite.toString()
        }
    }

    data class PlaceStone(val offset: IntOffset) :
        Message(TypePlaceStone) {
        override fun toString(): String {
            return "${offset.x},${offset.y}"
        }
    }

    object GameQuit : Message(TypeGameQuit) {
        override fun toString(): String {
            return "game end"
        }
    }

    object GameReset : Message(TypeGameReset) {
        override fun toString(): String {
            return "game reset"
        }
    }

    data class GameLog(val info: String) : Message(TypeGameLog) {
        override fun toString(): String {
            return info
        }
    }

}
