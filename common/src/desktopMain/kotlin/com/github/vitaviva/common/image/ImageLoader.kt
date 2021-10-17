package com.github.vitaviva.common.image

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Density
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.res.loadXmlImageVector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xml.sax.InputSource
import java.io.File
import java.io.IOException

//fun main() = singleWindowApplication {
//    val density = LocalDensity.current
//    Column {
//        AsyncImage(
//            load = { loadImageBitmap(File("sample.png")) },
//            painterFor = { remember { BitmapPainter(it) } },
//            contentDescription = "Sample",
//            modifier = Modifier.width(200.dp)
//        )
//        AsyncImage(
//            load = { loadSvgPainter(File("idea-logo.svg"), density) },
//            painterFor = { it },
//            contentDescription = "Idea logo",
//            contentScale = ContentScale.FillWidth,
//            modifier = Modifier.width(200.dp)
//        )
//        AsyncImage(
//            load = { loadXmlImageVector(File("compose-logo.xml"), density) },
//            painterFor = { rememberVectorPainter(it) },
//            contentDescription = "Compose logo",
//            contentScale = ContentScale.FillWidth,
//            modifier = Modifier.width(200.dp)
//        )
//    }
//}

@Composable
fun <T> AsyncImage(
    load: suspend () -> T,
    painterFor: @Composable (T) -> Painter,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val image: T? by produceState<T?>(null) {
        value = withContext(Dispatchers.IO) {
            try {
                load()
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    if (image != null) {
        Image(
            painter = painterFor(image!!),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    }
}

fun loadImageBitmap(file: File): ImageBitmap =
    file.inputStream().buffered().use(::loadImageBitmap)

fun loadSvgPainter(file: File, density: Density): Painter =
    file.inputStream().buffered().use { loadSvgPainter(it, density) }

fun loadXmlImageVector(file: File, density: Density): ImageVector =
    file.inputStream().buffered().use { loadXmlImageVector(InputSource(it), density) }

