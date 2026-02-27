package com.probrotechsolutions.laffalitto

import androidx.compose.runtime.Composable
import com.probrotechsolutions.laffalitto.actual.EnvironmentVariables
import com.probrotechsolutions.laffalitto.ui.joke.JokeDetailPage
import com.probrotechsolutions.laffalitto.ui.joke.JokePage
import com.probrotechsolutions.laffalitto.ui.theme.LaffaLittoTheme
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator

private const val ROUTE_CATEGORIES = "/categories"
private const val ROUTE_JOKE = "/joke/{category}"

@Composable
fun CommonHomePage(
    environmentVariables: EnvironmentVariables
) {
    LaffaLittoTheme {
        val navigator = rememberNavigator()
        NavHost(
            navigator = navigator,
            initialRoute = ROUTE_CATEGORIES
        ) {
            scene(ROUTE_CATEGORIES) {
                JokePage(
                    environmentVariables = environmentVariables,
                    onCategorySelected = { category ->
                        navigator.navigate("/joke/$category")
                    }
                )
            }
            scene(ROUTE_JOKE) { backStackEntry ->
                val category = backStackEntry.pathMap["category"] ?: ""
                JokeDetailPage(
                    category = category,
                    environmentVariables = environmentVariables,
                    onBack = { navigator.goBack() }
                )
            }
        }
    }
}

