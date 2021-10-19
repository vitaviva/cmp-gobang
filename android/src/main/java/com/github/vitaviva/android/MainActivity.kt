package com.github.vitaviva.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.github.vitaviva.common.App
import com.github.vitaviva.common.Res
import com.github.vitaviva.common.initPlatformResource


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPlatformResource(
            Res(
                R.mipmap.chess_white,
                R.mipmap.chess_black,
                R.string.server_host
            )
        )
        setContent {
            App()
        }
    }
}

