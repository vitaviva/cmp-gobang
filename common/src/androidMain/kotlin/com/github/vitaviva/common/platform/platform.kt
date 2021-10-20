package com.github.vitaviva.common.platform

import android.content.Context
import android.content.res.Resources
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes


actual fun getPlatformName(): String {
    return "Android"
}

internal val serverHost by lazy {
    _resources.getString(_res.serverHost)
}

internal lateinit var _resources: Resources
internal lateinit var _res: Res

data class Res(
    @DrawableRes val whiteChess: Int,
    @DrawableRes val blackChess: Int,
    @StringRes val serverHost: Int
)

fun Context.initPlatformResource(res: Res) {
    _resources = resources
    _res = res
}
