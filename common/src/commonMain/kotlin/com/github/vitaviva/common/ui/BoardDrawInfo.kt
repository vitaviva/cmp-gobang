package com.github.vitaviva.common.ui


const val LINE_COUNT = 15
internal const val BOARD_MARGIN = 40f
internal const val BOARD_LINE_WIDTH_DP = 0.7f //棋盘线宽度
internal const val BOARD_FRAME_WIDTH_DP = 1f //棋盘框的线宽度
internal const val BOARD_POINT_RADIUS_DP = 4f //棋盘五个圆点的半径宽度


object BoardDrawInfo {

    var GridWidth = 0f
    var GridHeight = 0f

    lateinit var BoardFramePoints //棋盘边框
            : FloatArray
    lateinit var VerticalLinePoints //棋盘竖线
            : FloatArray
    lateinit var HorizontalLinePoints //棋盘横线
            : FloatArray
    lateinit var BlackPoints //棋盘黑点
            : FloatArray

    val isInitialized
        get() = ::BoardFramePoints.isInitialized

    fun calcLinePoints(width: Float, height: Float) {
        HorizontalLinePoints = FloatArray(LINE_COUNT * 4)
        VerticalLinePoints = FloatArray(LINE_COUNT * 4)
        val boardWidth: Float = width - BOARD_MARGIN * 2
        val boardHeight: Float = height - BOARD_MARGIN * 2
        GridWidth = boardWidth / (LINE_COUNT - 1)
        run {
            var i = 0
            while (i < LINE_COUNT * 4) {
                VerticalLinePoints[i] = i * GridWidth / 4 + BOARD_MARGIN
                VerticalLinePoints[i + 1] = BOARD_MARGIN
                VerticalLinePoints[i + 2] = i * GridWidth / 4 + BOARD_MARGIN
                VerticalLinePoints[i + 3] = boardHeight + BOARD_MARGIN
                i += 4
            }
        }
        GridHeight = boardHeight / (LINE_COUNT - 1)
        var i = 0
        while (i < LINE_COUNT * 4) {
            HorizontalLinePoints[i] = BOARD_MARGIN
            HorizontalLinePoints[i + 1] = i * GridHeight / 4 + BOARD_MARGIN
            HorizontalLinePoints[i + 2] = boardWidth + BOARD_MARGIN
            HorizontalLinePoints[i + 3] = i * GridHeight / 4 + BOARD_MARGIN
            i += 4
        }
        val frameMargin = BOARD_MARGIN * 0.8f
        BoardFramePoints = floatArrayOf(
            frameMargin,
            frameMargin,
            width - frameMargin,
            frameMargin,  //上横
            frameMargin,
            height - frameMargin,
            width - frameMargin,
            height - frameMargin,  //下横
            frameMargin,
            frameMargin,
            frameMargin,
            height - frameMargin,  //左竖
            width - frameMargin,
            frameMargin,
            width - frameMargin,
            height - frameMargin
        ) //右竖
        BlackPoints = floatArrayOf(
            3 * GridWidth + BOARD_MARGIN, 3 * GridHeight + BOARD_MARGIN,
            11 * GridWidth + BOARD_MARGIN, 3 * GridHeight + BOARD_MARGIN,
            7 * GridWidth + BOARD_MARGIN, 7 * GridHeight + BOARD_MARGIN,
            3 * GridWidth + BOARD_MARGIN, 11 * GridHeight + BOARD_MARGIN,
            11 * GridWidth + BOARD_MARGIN, 11 * GridHeight + BOARD_MARGIN
        )
    }
}

