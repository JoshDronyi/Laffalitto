import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.probrotechsolutions.laffalitto.HomePage
import com.probrotechsolutions.laffalitto.actual.EnvironmentVariables
import moe.tlaster.precompose.PreComposeWindow

fun main() {
    application {
        val windowState = rememberWindowState()
        PreComposeWindow(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "My Project"
        ) {
            HomePage(EnvironmentVariables())
        }
    }
}