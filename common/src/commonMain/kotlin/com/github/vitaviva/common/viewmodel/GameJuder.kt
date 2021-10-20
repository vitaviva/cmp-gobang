package com.github.vitaviva.common.viewmodel


fun isGameEnd(board: Array<IntArray>, x: Int, y: Int): Boolean {
    return getLinkCount(board, x, y, DIRECT.VERTICAL) == 5 || getLinkCount(
        board,
        x,
        y,
        DIRECT.HORIZONTAL
    ) == 5 || getLinkCount(board, x, y, DIRECT.LEFT_TILT) == 5 || getLinkCount(
        board,
        x,
        y,
        DIRECT.RIGHT_TILT
    ) == 5
}

private fun getLinkCount(board: Array<IntArray>, x: Int, y: Int, direct: DIRECT): Int {
    var linkCount = 0
    var tmpX = x
    var tmpY = y
    when (direct) {
        DIRECT.VERTICAL -> {
            //上
            while (tmpY >= 0 && board[tmpX][tmpY] == board[x][y]) {
                linkCount++
                tmpY--
                if (linkCount == 5) {
                    return linkCount
                }
            }
            tmpY = y
            //下
            while (tmpY < board.size - 1 && board[tmpX][tmpY + 1] == board[x][y]) {
                linkCount++
                tmpY++
                if (linkCount == 5) {
                    return linkCount
                }
            }
        }
        DIRECT.HORIZONTAL -> {
            //左
            while (tmpX >= 0 && board[tmpX][tmpY] == board[x][y]) {
                linkCount++
                tmpX--
                if (linkCount == 5) {
                    return linkCount
                }
            }
            tmpX = x
            //右
            while (tmpX < board.size - 1 && board[tmpX + 1][tmpY] == board[x][y]) {
                linkCount++
                tmpX++
                if (linkCount == 5) {
                    return linkCount
                }
            }
        }
        DIRECT.LEFT_TILT -> {
            //左上
            while (tmpX >= 0 && tmpY >= 0 && board[tmpX][tmpY] == board[x][y]) {
                linkCount++
                tmpX--
                tmpY--
                if (linkCount == 5) {
                    return linkCount
                }
            }
            tmpX = x
            tmpY = y
            //右下
            while (tmpX < board.size - 1 && tmpY < board.size - 1 && board[tmpX + 1][tmpY + 1] == board[x][y]) {
                linkCount++
                tmpX++
                tmpY++
                if (linkCount == 5) {
                    return linkCount
                }
            }
        }
        DIRECT.RIGHT_TILT -> {
            //右上
            while (tmpX < board.size && tmpY >= 0 && board[tmpX][tmpY] == board[x][y]) {
                linkCount++
                tmpX++
                tmpY--
                if (linkCount == 5) {
                    return linkCount
                }
            }
            tmpX = x
            tmpY = y
            //左下
            while (tmpX > 0 && tmpY < board.size - 1 && board[tmpX - 1][tmpY + 1] == board[x][y]) {
                linkCount++
                tmpX--
                tmpY++
                if (linkCount == 5) {
                    return linkCount
                }
            }
        }
    }
    return linkCount
}

private enum class DIRECT {
    VERTICAL, HORIZONTAL, LEFT_TILT, RIGHT_TILT
}
