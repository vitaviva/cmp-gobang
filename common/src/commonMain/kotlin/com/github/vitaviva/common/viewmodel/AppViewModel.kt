package com.github.vitaviva.common.viewmodel

import androidx.compose.ui.unit.IntOffset
import com.github.vitaviva.common.api.Api
import com.github.vitaviva.common.api.Message
import com.github.vitaviva.common.platform.getPlatformName
import com.github.vitaviva.common.ui.BOARD_SIZE
import com.github.vitaviva.common.ui.LINE_COUNT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

const val CHESS_NONE = 0
const val CHESS_WHITE = 1
const val CHESS_BLACK = 2

class AppViewModel(
) : ViewController() {

    @Volatile
    private var _initConnected = false

    val pairedFlow = MutableSharedFlow<String>()

    private val _board = MutableStateFlow(Array(BOARD_SIZE) { IntArray(BOARD_SIZE) })
    val boardData: Flow<BoardData>
        get() = _board

    private var _chessColor = MutableStateFlow(CHESS_NONE)
    val chessColor: Flow<Int>
        get() = _chessColor


    /**
     * do pair
     */
    fun doPair() {
        coroutineScope.launch {
            Api.connect()
            _initConnected = true
            Api.receiveMessage().collect {
                when (it) {
                    is Message.PutChess -> putChessInternal(
                        it.offset, _chessColor.value != CHESS_WHITE
                    )
                    is Message.ChooseColor -> setChessColorInternal(!it.isWhite)
                    else -> {
                    }
                }
                pairedFlow.emit(it.toString())
            }
        }
    }


    /**
     * select chess type
     */
    fun setChessColor(isWhite: Boolean) {
        setChessColorInternal(isWhite)
        if (_initConnected) {
            coroutineScope.launch {
                Api.sendMessage(Message.ChooseColor(isWhite))
            }
        }
    }

    private fun setChessColorInternal(isWhite: Boolean) {
        _chessColor.value = if (isWhite) CHESS_WHITE else CHESS_BLACK
    }

    /**
     * add chess
     */
    fun putChess(offset: IntOffset) {
        if (_chessColor.value == CHESS_NONE) {
            coroutineScope.launch {
                pairedFlow.emit("please choose a color")
            }
            return
        }
        putChessInternal(offset, _chessColor.value == CHESS_WHITE)
        if (_initConnected) {
            coroutineScope.launch {
                Api.sendMessage(Message.PutChess(offset))
            }
        }
    }

    private fun putChessInternal(offset: IntOffset, isWhite: Boolean) {
        val cur = _board.value
        if (cur[offset.x][offset.y] != CHESS_NONE) {
            return
        }
        _board.value = cur.copyOf().also {
            it[offset.x][offset.y] = if (isWhite) CHESS_WHITE else CHESS_BLACK
        }

    }


    /**
     * clear chess
     */
    fun clearBoard() {
        _board.value = _board.value.copyOf().also {
            for (row in 0 until LINE_COUNT) {
                for (col in 0 until LINE_COUNT) {
                    it[col][row] = CHESS_NONE
                }
            }
        }
    }

}


typealias BoardData = Array<IntArray>