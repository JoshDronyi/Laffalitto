@file:OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterialApi::class
)

package com.probrotechsolutions.laffalitto.ui.joke

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.probrotechsolutions.laffalitto.Utils.Screens.JokeScreens.HOME
import com.probrotechsolutions.laffalitto.Utils.Screens.JokeScreens.JOKE
import com.probrotechsolutions.laffalitto.actual.EnvironmentVariables
import com.probrotechsolutions.laffalitto.model.local.flags.Flag
import com.probrotechsolutions.laffalitto.model.local.jokeCategory.JokeCategory
import com.probrotechsolutions.laffalitto.model.mappers.JokeCategoryResponseMapper
import com.probrotechsolutions.laffalitto.model.mappers.JokeResponseMapper
import com.probrotechsolutions.laffalitto.model.network.services.JokeService
import com.probrotechsolutions.laffalitto.model.repositories.JokeRepository
import com.probrotechsolutions.laffalitto.viewmodel.JokeCategoryUiModel
import com.probrotechsolutions.laffalitto.viewmodel.JokeState
import com.probrotechsolutions.laffalitto.viewmodel.JokeViewModel
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.viewmodel.viewModel

@Composable
fun JokeSection(
    environmentVariables: EnvironmentVariables,
    navigator: Navigator = rememberNavigator(),
    jokeVm: JokeViewModel = viewModel(JokeViewModel::class, listOf(), creator = {
        JokeViewModel(
            JokeRepository(
                jokeService = JokeService(environmentVariables),
                jokeResponseMapper = JokeCategoryResponseMapper(),
                jokeMapper = JokeResponseMapper()
            )
        )
    }),
) {
    val jokeState by jokeVm.jokeState.collectAsState()
    NavHost(
        navigator = navigator,
        initialRoute = HOME.route
    ) {
        scene(HOME.route) {
            JokeHomePage(jokeState) {
                jokeVm.getJokes(it)
            }
        }
        scene(JOKE.route) {

        }
    }
}

@Composable
private fun JokeHomePage(jokeState: JokeState, onCategorySelect: (category: String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        when {
            jokeState.isLoading -> {
                CircularProgressIndicator()
            }

            jokeState.errorMsg.isNotEmpty() -> {
                ErrorMessage(jokeState.errorMsg)
            }

            else -> {
                JokeCategoryList(
                    jokeCategoryList = jokeState.jokeCategoryUiModel,
                    onItemSelect = onCategorySelect
                )
            }
        }
    }
}

@Composable
fun JokeCategoryList(
    jokeCategoryList: JokeCategoryUiModel,
    modifier: Modifier = Modifier,
    onItemSelect: (category: String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {

        SectionBackground(
            modifier
                .weight(1f)
        ) {
            TitleCard(
                "Flags",
                "Flags represent topics to look out for for one reason or another. If you would like to ignore content with a particular flag, simply select it here."
            )

            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(jokeCategoryList.flags) {
                    FlagListItem(it)
                }
            }
        }

        SectionBackground(
            modifier = modifier
                .weight(1f),
        ) {
            TitleCard(
                "Categories",
                "Choose from the available categories of jokes or choose \"Any\" if you have no preference or restriction."
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2)
            ) {

                items(jokeCategoryList.jokeCategories) {
                    JokeCategoryItem(it) {
                        onItemSelect(it.category)
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionBackground(
    modifier: Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        contentColor = MaterialTheme.colors.secondary,
    ) {
        Column {
            content()
        }
    }
}

@Composable
fun TitleCard(title: String, description: String, modifier: Modifier = Modifier.fillMaxWidth()) {
    Card(
        modifier = modifier,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h4
            )
            Divider(thickness = 1.dp, startIndent = 80.dp)
            Text(
                text = description,
                style = MaterialTheme.typography.subtitle1
            )
        }
    }
}

@Composable
fun FlagListItem(flag: Flag, modifier: Modifier = Modifier) {
    Text(
        text = flag.name,
        style = MaterialTheme.typography.subtitle2,
        modifier = modifier,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.onSurface,
    )
}

@Composable
fun JokeCategoryItem(
    category: JokeCategory,
    modifier: Modifier = Modifier.fillMaxWidth()
        .padding(8.dp),
    onItemClick: () -> Unit
) {
    Text(
        text = category.category,
        style = MaterialTheme.typography.subtitle2,
        modifier = modifier
            .clickable { onItemClick() }
            .border(BorderStroke(2.dp, MaterialTheme.colors.onSurface)),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.onSurface,
    )
}

@Composable
fun ErrorMessage(error: String) {
    Text("Oh Lawdy theres an error!! Error -> $error")
}
