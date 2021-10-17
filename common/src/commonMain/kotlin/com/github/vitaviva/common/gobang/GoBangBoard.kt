package com.github.vitaviva.common.gobang

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.withSave
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.github.vitaviva.common.BLACK_CHESS_BMP
import com.github.vitaviva.common.viewmodel.AppViewModel
import com.github.vitaviva.common.viewmodel.CHESS_BLACK
import com.github.vitaviva.common.viewmodel.CHESS_WHITE
import com.github.vitaviva.common.WHITE_CHESS_BMP
import kotlinx.coroutines.launch
import java.lang.Float.min
import kotlin.math.roundToInt


const val LINE_COUNT = 15
private const val BOARD_MARGIN = 40f
private const val HALF_CHESS_SIZE = 35

const val BOARD_SIZE = LINE_COUNT
private const val BOARD_LINE_WIDTH_DP = 0.7f //棋盘线宽度

private const val BOARD_FRAME_WIDTH_DP = 1f //棋盘框的线宽度

private const val BOARD_POINT_RADIUS_DP = 4f //棋盘五个圆点的半径宽度


@Composable
fun GoBangBoard(
    modifier: Modifier,
    viewModel: AppViewModel
) {
    val boardData by viewModel.boardData.collectAsState(Array(BOARD_SIZE) { IntArray(BOARD_SIZE) })
    val scope = rememberCoroutineScope()
    with(LocalDensity.current) {
        val linePaint = remember {
            Paint().apply {
                color = Color.Black
                isAntiAlias = true
                strokeWidth = 1.dp.toPx()
            }

        }


        DisposableEffect(Unit) { //init
            viewModel.clearBoard()
            onDispose { }
        }

        Canvas(modifier = modifier.pointerInput(Unit) {
            scope.launch {//demonstrate detectDragGestures
                detectTapGestures {
                    viewModel.putChess( convertPoint(it.x, it.y))
                }
            }

        }) {
            if (!::mHorizontalLinePoints.isInitialized) {
                val min = min(size.width, size.height)
                calcLinePoints(min, min)
            }
            drawLines(linePaint)
            drawBlackPoints(BOARD_POINT_RADIUS_DP.dp.toPx())
            drawChess(boardData)
        }
    }


}

fun convertPoint(x: Float, y: Float): IntOffset {
    val i = Math.rint(((x - BOARD_MARGIN) / mGridWidth).toDouble()).toInt()
    val j = Math.rint(((y - BOARD_MARGIN) / mGridHeight).toDouble()).toInt()
    return IntOffset(i, j)
}

private const val mLineCount = LINE_COUNT
private var mGridWidth = 0f
private var mGridHeight = 0f
private lateinit var mBoardFramePoints //棋盘边框
        : FloatArray
private lateinit var mVerticalLinePoints //棋盘竖线
        : FloatArray
private lateinit var mHorizontalLinePoints //棋盘横线
        : FloatArray
private lateinit var mBlackPoints //棋盘黑点
        : FloatArray

private fun calcLinePoints(width: Float, height: Float) {
    mHorizontalLinePoints = FloatArray(mLineCount * 4)
    mVerticalLinePoints = FloatArray(mLineCount * 4)
    val boardWidth: Float = width - BOARD_MARGIN * 2
    val boardHeight: Float = height - BOARD_MARGIN * 2
    mGridWidth = boardWidth / (mLineCount - 1)
    run {
        var i = 0
        while (i < mLineCount * 4) {
            mVerticalLinePoints[i] = i * mGridWidth / 4 + BOARD_MARGIN
            mVerticalLinePoints[i + 1] = BOARD_MARGIN
            mVerticalLinePoints[i + 2] = i * mGridWidth / 4 + BOARD_MARGIN
            mVerticalLinePoints[i + 3] = boardHeight + BOARD_MARGIN
            i += 4
        }
    }
    mGridHeight = boardHeight / (mLineCount - 1)
    var i = 0
    while (i < mLineCount * 4) {
        mHorizontalLinePoints[i] = BOARD_MARGIN
        mHorizontalLinePoints[i + 1] = i * mGridHeight / 4 + BOARD_MARGIN
        mHorizontalLinePoints[i + 2] = boardWidth + BOARD_MARGIN
        mHorizontalLinePoints[i + 3] = i * mGridHeight / 4 + BOARD_MARGIN
        i += 4
    }
    val frameMargin = BOARD_MARGIN * 0.8f
    mBoardFramePoints = floatArrayOf(
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
    mBlackPoints = floatArrayOf(
        3 * mGridWidth + BOARD_MARGIN, 3 * mGridHeight + BOARD_MARGIN,
        11 * mGridWidth + BOARD_MARGIN, 3 * mGridHeight + BOARD_MARGIN,
        7 * mGridWidth + BOARD_MARGIN, 7 * mGridHeight + BOARD_MARGIN,
        3 * mGridWidth + BOARD_MARGIN, 11 * mGridHeight + BOARD_MARGIN,
        11 * mGridWidth + BOARD_MARGIN, 11 * mGridHeight + BOARD_MARGIN
    )
}

fun DrawScope.drawLines(paint: Paint) {

    drawIntoCanvas { canvas ->
        canvas.withSave {
            for (i in mHorizontalLinePoints.indices step 4) {
                canvas.drawLine(
                    Offset(
                        mHorizontalLinePoints[i],
                        mHorizontalLinePoints[i + 1]
                    ),
                    Offset(
                        mHorizontalLinePoints[i + 2],
                        mHorizontalLinePoints[i + 3]
                    ),
                    paint
                )
            }

            for (i in mVerticalLinePoints.indices step 4) {
                canvas.drawLine(
                    Offset(
                        mVerticalLinePoints[i],
                        mVerticalLinePoints[i + 1]
                    ),
                    Offset(
                        mVerticalLinePoints[i + 2],
                        mVerticalLinePoints[i + 3]
                    ),
                    paint
                )
            }
        }
    }

}


fun DrawScope.drawBlackPoints(radius: Float) {

    for (i in mBlackPoints.indices step 2) {
        drawCircle(
            Color.Black,
            radius = radius,
            center = Offset(mBlackPoints[i], mBlackPoints[i + 1])
        )
    }
}

fun DrawScope.drawChess(mBoard: Array<IntArray>) {

    drawIntoCanvas { canvas ->
        canvas.withSave {
            for (row in 0 until LINE_COUNT) {
                for (col in 0 until LINE_COUNT) {
                    val x = BOARD_MARGIN + col * mGridWidth
                    val y = BOARD_MARGIN + row * mGridHeight
                    val offset = IntOffset(
                        (x - HALF_CHESS_SIZE).roundToInt(),
                        (y - HALF_CHESS_SIZE).roundToInt()
                    )
                    if (mBoard[col][row] == CHESS_WHITE) {
                        canvas.drawImageRect(
                            WHITE_CHESS_BMP,
                            dstOffset = offset,
                            dstSize = IntSize(HALF_CHESS_SIZE * 2, HALF_CHESS_SIZE * 2),
                            paint = Paint()
                        )
                    } else if (mBoard[col][row] == CHESS_BLACK) {
                        canvas.drawImageRect(
                            BLACK_CHESS_BMP,
                            dstOffset = offset,
                            dstSize = IntSize(HALF_CHESS_SIZE * 2, HALF_CHESS_SIZE * 2),
                            paint = Paint()
                        )
                    }
                }
            }
        }
    }

}
