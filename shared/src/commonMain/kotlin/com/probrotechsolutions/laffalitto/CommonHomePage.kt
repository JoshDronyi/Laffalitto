package com.probrotechsolutions.laffalitto

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.saveable.SaveableStateRegistry
import com.probrotechsolutions.laffalitto.actual.EnvironmentVariables
import com.probrotechsolutions.laffalitto.ui.joke.JokeDetailPage
import com.probrotechsolutions.laffalitto.ui.joke.JokePage
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.stateholder.LocalSavedStateHolder
import moe.tlaster.precompose.stateholder.LocalStateHolder
import moe.tlaster.precompose.stateholder.SavedStateHolder
import moe.tlaster.precompose.stateholder.StateHolder

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

@Composable
fun LaffaLittoTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalStateHolder provides StateHolder()) {
        val savedStateHolder = SavedStateHolder(
            "LaffaLittoRoot", SaveableStateRegistry(restoredValues = mapOf(), canBeSaved = { true })
        )
        CompositionLocalProvider(LocalSavedStateHolder provides savedStateHolder) {
            MaterialTheme {
                content()
            }
        }
    }
}
