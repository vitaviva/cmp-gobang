package com.github.vitaviva.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.vitaviva.common.App
import com.github.vitaviva.common.platform.Res
import com.github.vitaviva.common.platform.initPlatformResource


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPlatformResource(
            Res(
                R.mipmap.stone_white,
                R.mipmap.stone_black,
                R.string.server_host
            )
        )
        setContent {
            App()
        }
    }
}

@Composable
@Preview
fun preview() {
    Box {
        App()
    }
}