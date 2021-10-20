package com.github.vitaviva.common

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.vitaviva.common.ui.GoBoard
import com.github.vitaviva.common.viewmodel.AppViewModel
import com.github.vitaviva.common.viewmodel.STONE_BLACK
import com.github.vitaviva.common.viewmodel.STONE_NONE
import com.github.vitaviva.common.viewmodel.STONE_WHITE

@Composable
fun App() {

    MaterialTheme {

        val scrollSatte = rememberScrollableState {
            it
        }
        val viewModel = remember { AppViewModel() }

        val stoneColor by viewModel.stoneColor.collectAsState(STONE_NONE)
        val remoteInfo by viewModel.pairedFlow.collectAsState("")

        Row(
            Modifier.scrollable(
                orientation = Orientation.Vertical,
                state = scrollSatte
            )
        ) {

            Box(Modifier.weight(1f)) {
                GoBoard(Modifier.fillMaxSize(), viewModel)
            }

            Column {
                Row(Modifier.padding(start = 10.dp, end = 10.dp)) {
                    Text("White")
                    RadioButton(
                        selected = stoneColor == STONE_WHITE,
                        onClick = { viewModel.setStoneColor(true) }
                    )
                }
                Row(Modifier.padding(start = 10.dp, end = 10.dp)) {
                    Text("Black")
                    RadioButton(
                        selected = stoneColor == STONE_BLACK,
                        onClick = { viewModel.setStoneColor(false) }
                    )
                }

                Button(onClick = {
                    viewModel.doPair()
                }) {
                    Text("Pairing")
                }

                Text(remoteInfo)
            }

        }

    }

}
