package com.github.vitaviva.common

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.vitaviva.common.platform.AppDialog
import com.github.vitaviva.common.platform.getPlatformName
import com.github.vitaviva.common.ui.GoBoard
import com.github.vitaviva.common.viewmodel.AppViewModel
import com.github.vitaviva.common.viewmodel.GAME_LOST
import com.github.vitaviva.common.viewmodel.GAME_PLAYING
import com.github.vitaviva.common.viewmodel.STONE_BLACK
import com.github.vitaviva.common.viewmodel.STONE_NONE
import com.github.vitaviva.common.viewmodel.STONE_WHITE
import org.intellij.lang.annotations.JdkConstants

@Composable
fun App() {

    MaterialTheme {

        val viewModel = remember { AppViewModel() }
        val gameStauts by viewModel.gameStatus.collectAsState(GAME_PLAYING)

        Row {
            GoBoard(Modifier.weight(0.7f), viewModel)
            GameInfo(Modifier.weight(0.3f), viewModel)
        }

        if (gameStauts != GAME_PLAYING) {
            AppDialog(if (gameStauts == GAME_LOST) "You Lost 😭" else "You Win 🎉") {
                viewModel.clearStones()
            }
        }
    }

}

@Composable
fun GameInfo(modifier: Modifier, viewModel: AppViewModel) {

    val stoneColor by viewModel.stoneColor.collectAsState(STONE_NONE)
    val gameLog by viewModel.gameLog.collectAsState("")
    val isPaired by viewModel.isPaired.collectAsState(false)

    Column(modifier.padding(5.dp)) {
        Row {
            Text("White", modifier = Modifier.align(Alignment.CenterVertically))
            Spacer(Modifier.height(5.dp))
            RadioButton(
                selected = stoneColor == STONE_WHITE,
                onClick = { viewModel.setStoneColor(true) },
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
        Spacer(Modifier.height(2.dp))
        Row {
            Text("Black", modifier = Modifier.align(Alignment.CenterVertically))
            Spacer(Modifier.height(5.dp))
            RadioButton(
                selected = stoneColor == STONE_BLACK,
                onClick = { viewModel.setStoneColor(false) },
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        Spacer(Modifier.height(5.dp))

        Button(onClick = { viewModel.clearStones() }) {
            Text("Reset")
        }

        if (!isPaired) {
            Spacer(Modifier.height(5.dp))
            Button(onClick = { viewModel.doPair() }) {
                Text("Pair")
            }
        }

        Spacer(Modifier.height(5.dp))

        Column(
            modifier = Modifier.height(150.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(gameLog)
        }

    }

}