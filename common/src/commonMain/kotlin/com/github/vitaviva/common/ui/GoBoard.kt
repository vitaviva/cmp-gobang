package com.github.vitaviva.common.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
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
import com.github.vitaviva.common.platform.BlackStoneBmp
import com.github.vitaviva.common.platform.WhiteStoneBmp
import com.github.vitaviva.common.ui.BoardDrawInfo.GridHeight
import com.github.vitaviva.common.ui.BoardDrawInfo.GridWidth
import com.github.vitaviva.common.ui.BoardDrawInfo.calcLinePoints
import com.github.vitaviva.common.viewmodel.AppViewModel
import com.github.vitaviva.common.viewmodel.STONE_BLACK
import com.github.vitaviva.common.viewmodel.STONE_WHITE
import kotlinx.coroutines.launch
import java.lang.Float.min
import kotlin.math.round
import kotlin.math.roundToInt


const val BOARD_SIZE = LINE_COUNT

@Composable
fun GoBoard(
    modifier: Modifier,
    viewModel: AppViewModel
) {
    val boardData by viewModel.boardData.collectAsState(Array(BOARD_SIZE) { IntArray(BOARD_SIZE) })
    val scope = rememberCoroutineScope()


    Box(modifier) {
        with(LocalDensity.current) {
            val (linePaint, framePaint) = remember {
                Paint().apply {
                    color = Color.Black
                    isAntiAlias = true
                    strokeWidth = BOARD_LINE_WIDTH_DP.dp.toPx()
                } to Paint().apply {
                    color = Color.Black
                    isAntiAlias = true
                    strokeWidth = BOARD_FRAME_WIDTH_DP.dp.toPx()
                }
            }

            DisposableEffect(Unit) { //init
                viewModel.clearStones()
                onDispose { }
            }

            Canvas(modifier = Modifier.fillMaxSize().pointerInput(Unit) {
                scope.launch {
                    detectTapGestures {
                        viewModel.placeStone(convertPoint(it.x, it.y))
                    }
                }
            }) {
                if (!BoardDrawInfo.isInitialized) {
                    val min = min(size.width, size.height)
                    calcLinePoints(min, min)
                }

                drawLines(linePaint, framePaint)
                drawBlackPoints(BOARD_POINT_RADIUS_DP.dp.toPx())
                drawStones(boardData)
            }
        }
    }

}


private fun convertPoint(x: Float, y: Float): IntOffset {
    val i = round(((x - BOARD_MARGIN) / GridWidth).toDouble()).toInt()
    val j = round(((y - BOARD_MARGIN) / GridWidth).toDouble()).toInt()
    return IntOffset(i, j)
}

private fun DrawScope.drawLines(linePaint: Paint, framePaint: Paint) {

    drawIntoCanvas { canvas ->
        fun drawLines(linePoints: FloatArray, paint: Paint) {
            for (i in linePoints.indices step 4) {
                canvas.drawLine(
                    Offset(
                        linePoints[i],
                        linePoints[i + 1]
                    ),
                    Offset(
                        linePoints[i + 2],
                        linePoints[i + 3]
                    ),
                    paint
                )
            }
        }
        canvas.withSave {
            with(BoardDrawInfo) {
                drawLines(HorizontalLinePoints, linePaint)
                drawLines(VerticalLinePoints, linePaint)
                drawLines(BoardFramePoints, framePaint)
            }
        }
    }

}


private fun DrawScope.drawBlackPoints(radius: Float) {
    with(BoardDrawInfo) {
        for (i in BlackPoints.indices step 2) {
            drawCircle(
                Color.Black,
                radius = radius,
                center = Offset(BlackPoints[i], BlackPoints[i + 1])
            )
        }
    }
}

private fun DrawScope.drawStones(mBoard: Array<IntArray>) {

    val halfStoneSize = (GridWidth / 2f).roundToInt()
    drawIntoCanvas { canvas ->
        canvas.withSave {
            for (row in 0 until LINE_COUNT) {
                for (col in 0 until LINE_COUNT) {
                    val x = BOARD_MARGIN + col * GridWidth
                    val y = BOARD_MARGIN + row * GridHeight
                    val offset = IntOffset(
                        (x - halfStoneSize).roundToInt(),
                        (y - halfStoneSize).roundToInt()
                    )
                    val bmp = when (mBoard[col][row]) {
                        STONE_WHITE -> WhiteStoneBmp
                        STONE_BLACK -> BlackStoneBmp
                        else -> null
                    }
                    bmp?.let {
                        canvas.drawImageRect(
                            it,
                            dstOffset = offset,
                            dstSize = IntSize(
                                halfStoneSize * 2,
                                halfStoneSize * 2
                            ),
                            paint = EmptyPaint
                        )
                    }
                }
            }
        }
    }

}

private val EmptyPaint by lazy {
    Paint()
}