package com.github.vitaviva.common.viewmodel

import androidx.compose.ui.unit.IntOffset
import com.github.vitaviva.common.api.Api
import com.github.vitaviva.common.api.Message
import com.github.vitaviva.common.ui.BOARD_SIZE
import com.github.vitaviva.common.ui.LINE_COUNT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

const val STONE_NONE = 0
const val STONE_WHITE = 1
const val STONE_BLACK = 2
const val GAME_PLAYING = 0
const val GAME_WIN = 1
const val GAME_LOST = 2

class AppViewModel(
) : ViewController() {

    private val _gameLog = MutableStateFlow("")
    val gameLog: Flow<String>
        get() = _gameLog

    private val _board = MutableStateFlow(Array(BOARD_SIZE) { IntArray(BOARD_SIZE) })
    val boardData: Flow<BoardData>
        get() = _board

    private var _stoneColor = MutableStateFlow(STONE_NONE)
    val stoneColor: Flow<Int>
        get() = _stoneColor


    private var _isPaired = MutableStateFlow(false)
    val isPaired: Flow<Boolean>
        get() = _isPaired

    private var _gameStatus = MutableStateFlow(GAME_PLAYING)
    val gameStatus: Flow<Int>
        get() = _gameStatus

    /**
     * do pair
     */
    fun doPair() {
        coroutineScope.launch {
            Api.connect()
            _isPaired.emit(true)
            Api.receiveMessage().collect { msg ->
                when (msg) {
                    is Message.PlaceStone ->
                        _placeStone(msg.offset, _stoneColor.value != STONE_WHITE)
                            .also { _appendLog("${if (_stoneColor.value == STONE_WHITE) "b" else "w"}: ${msg.offset}") }
                    is Message.ChooseColor -> _setStoneColor(!msg.isWhite)
                    is Message.GameReset -> _clearStones()
                    is Message.GameQuit -> TODO()
                    is Message.GameLog -> _printLog(msg.toString())
                }

            }
        }
    }


    /**
     * select stone color
     */
    fun setStoneColor(isWhite: Boolean) {
        _setStoneColor(isWhite)
        if (_isPaired.value) {
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
            _printLog("please choose a color")
            return
        }

        _placeStone(offset, _stoneColor.value == STONE_WHITE).apply {
            _appendLog("${if (_stoneColor.value == STONE_WHITE) "w" else "b"}: ${offset}")
        }
        if (_isPaired.value) {
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

        if (isGameEnd(_board.value, offset.x, offset.y)) {
            _gameEnd(_stoneColor.value == if (isWhite) STONE_WHITE else STONE_BLACK)
        }
    }


    /**
     * clear stones
     */
    fun clearStones() {
        _clearStones()
        _gameStatus.value = GAME_PLAYING
        if (_isPaired.value) {
            coroutineScope.launch {
                Api.sendMessage(Message.GameReset)
            }
        }
    }

    private fun _clearStones() {
        _clearInfo()
        _board.value = _board.value.copyOf().also {
            for (row in 0 until LINE_COUNT) {
                for (col in 0 until LINE_COUNT) {
                    it[col][row] = STONE_NONE
                }
            }
        }
    }

    private fun _printLog(info: String) {
        _gameLog.value = info
    }

    private var _appendLog: String = ""
    private fun _appendLog(info: String) {
        _appendLog = "$info\n$_appendLog"
        _gameLog.value = _appendLog
    }

    private fun _clearInfo() {
        _appendLog = ""
        _gameLog.value = ""
    }

    private fun _gameEnd(isWin: Boolean) {
        _gameStatus.value = if (isWin) GAME_WIN else GAME_LOST
    }
}


typealias BoardData = Array<IntArray>