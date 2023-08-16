package com.probrotechsolutions.laffalitto.ui.joke

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.probrotechsolutions.laffalitto.actual.EnvironmentVariables
import com.probrotechsolutions.laffalitto.model.local.jokes.Alias
import com.probrotechsolutions.laffalitto.model.local.jokes.JokeCategory
import com.probrotechsolutions.laffalitto.model.mappers.JokeCategoryResponseMapper
import com.probrotechsolutions.laffalitto.model.network.services.JokeService
import com.probrotechsolutions.laffalitto.model.repositories.JokeRepository
import com.probrotechsolutions.laffalitto.viewmodel.JokeViewModel
import kotlinx.coroutines.FlowPreview
import moe.tlaster.precompose.viewmodel.viewModel

@Composable
fun JokePage(
    environmentVariables: EnvironmentVariables
) {
    val jokeVm = viewModel(JokeViewModel::class, listOf(), creator = {
        JokeViewModel(
            JokeRepository(
                jokeService = JokeService(environmentVariables),
                jokeResponseMapper = JokeCategoryResponseMapper()
            )
        )
    })
    val jokeState by jokeVm.jokeState.collectAsState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        when {
            jokeState.isLoading -> {
                CircularProgressIndicator()
            }

            jokeState.jokeCategories.isNotEmpty() -> {
                JokeCategoryList(jokeState.jokeCategories)
            }

            jokeState.errorMsg.isNotEmpty() -> {
                ErrorMessage(jokeState.errorMsg)
            }
        }
    }

}

@Composable
fun JokeCategoryList(jokeCategoryList: List<JokeCategory>) {
    LazyColumn {
        items(jokeCategoryList) {
            JokeCategoryItem(it)
        }
    }
}

@Composable
fun JokeCategoryItem(
    category: JokeCategory,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    Text(
        text = category.category,
        style = MaterialTheme.typography.body2,
        modifier = modifier
    )
    Row {
        category.aliases.forEach {
            Text("${it.name} = ${it.resolved}")
        }
    }
}

@Composable
fun ErrorMessage(error:String) {
    Text("Oh Lawdy theres an error!! Error -> $error")
}

@FlowPreview
@Composable
fun JokePagePreview() {
    JokeCategoryItem(
        category = JokeCategory(
            "Cole",
            aliases = listOf(
                Alias(name = "greatest rapper", resolved = "Cole"),
                Alias(name = "king of the Mic", resolved = "Cole"),
                Alias(name = "King MC", resolved = "Cole")
            )
        )
    )
}
