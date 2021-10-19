package com.github.vitaviva.common.viewmodel

import androidx.compose.ui.unit.IntOffset
import com.github.vitaviva.common.gobang.BOARD_SIZE
import com.github.vitaviva.common.gobang.LINE_COUNT
import com.github.vitaviva.common.initWs
import com.github.vitaviva.common.remoteFlow
import com.github.vitaviva.common.sendToRemote
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
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

    fun doPair() {
        GlobalScope.launch {
            initWs()
            delay(1000)
            remoteFlow().collect {
                pairedFlow.emit(it.data.readText())
            }
        }
    }

    val pairedFlow = MutableSharedFlow<String>()

    private var _currentCheesType = MutableStateFlow(CHESS_WHITE)
    val curChessType: Flow<Int>
        get() = _currentCheesType

    fun setChessType(isWhite: Boolean) {
        _currentCheesType .value = if (isWhite) CHESS_WHITE
        else CHESS_BLACK
    }

    private val _board = MutableStateFlow(Array(BOARD_SIZE) { IntArray(BOARD_SIZE) })

    val boardData: Flow<BoardData>
        get() = _board

    fun putChess(offset: IntOffset) {
        val cur = _board.value
        if (cur[offset.x][offset.y] != CHESS_NONE) {
            return
        }
        val new = cur.copyOf()
        new[offset.x][offset.y] = _currentCheesType.value

        _board.value = new
        GlobalScope.launch {
            sendToRemote(offset.toString())
        }
    }


    fun clearBoard() {
        val new = _board.value
        for (row in 0 until LINE_COUNT) {
            for (col in 0 until LINE_COUNT) {
                new[col][row] = CHESS_NONE
            }
        }
        _board.value = new
    }

}


typealias BoardData = Array<IntArray>