import androidx.compose.desktop.Window
import com.github.vitaviva.common.App
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

fun main() =
    Window("gobang")
    {
        App()
    }


@Preview
@Composable
fun preview() {
    App()
}
