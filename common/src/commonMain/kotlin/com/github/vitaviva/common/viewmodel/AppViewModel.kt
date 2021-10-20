package com.github.vitaviva.common.viewmodel

import androidx.compose.ui.unit.IntOffset
import com.github.vitaviva.common.api.Api
import com.github.vitaviva.common.api.Message
import com.github.vitaviva.common.ui.BOARD_SIZE
import com.github.vitaviva.common.ui.LINE_COUNT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

const val STONE_NONE = 0
const val STONE_WHITE = 1
const val STONE_BLACK = 2

class AppViewModel(
) : ViewController() {

    @Volatile
    private var _initConnected = false

    val pairedFlow = MutableSharedFlow<String>()

    private val _board = MutableStateFlow(Array(BOARD_SIZE) { IntArray(BOARD_SIZE) })
    val boardData: Flow<BoardData>
        get() = _board

    private var _stoneColor = MutableStateFlow(STONE_NONE)
    val stoneColor: Flow<Int>
        get() = _stoneColor


    /**
     * do pair
     */
    fun doPair() {
        coroutineScope.launch {
            Api.connect()
            _initConnected = true
            Api.receiveMessage().collect {
                when (it) {
                    is Message.PlaceStone -> _placeStone(
                        it.offset, _stoneColor.value != STONE_WHITE
                    )
                    is Message.ChooseColor -> _setStoneColor(!it.isWhite)
                    else -> {
                    }
                }
                pairedFlow.emit(it.toString())
            }
        }
    }


    /**
     * select stone color
     */
    fun setStoneColor(isWhite: Boolean) {
        _setStoneColor(isWhite)
        if (_initConnected) {
            coroutineScope.launch {
                Api.sendMessage(Message.ChooseColor(isWhite))
            }
        }
    }

    private fun _setStoneColor(isWhite: Boolean) {
        _stoneColor.value = if (isWhite) STONE_WHITE else STONE_BLACK
    }

    /**
     * place stone
     */
    fun placeStone(offset: IntOffset) {
        if (_stoneColor.value == STONE_NONE) {
            coroutineScope.launch {
                pairedFlow.emit("please choose a color")
            }
            return
        }
        _placeStone(offset, _stoneColor.value == STONE_WHITE)
        if (_initConnected) {
            coroutineScope.launch {
                Api.sendMessage(Message.PlaceStone(offset))
            }
        }
    }

    private fun _placeStone(offset: IntOffset, isWhite: Boolean) {
        val cur = _board.value
        if (cur[offset.x][offset.y] != STONE_NONE) {
            return
        }
        _board.value = cur.copyOf().also {
            it[offset.x][offset.y] = if (isWhite) STONE_WHITE else STONE_BLACK
        }

    }


    /**
     * clear stones
     */
    fun clearBoard() {
        _board.value = _board.value.copyOf().also {
            for (row in 0 until LINE_COUNT) {
                for (col in 0 until LINE_COUNT) {
                    it[col][row] = STONE_NONE
                }
            }
        }
    }

}


typealias BoardData = Array<IntArray>