package com.probrotechsolutions.laffalitto.ui.joke

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.probrotechsolutions.laffalitto.actual.EnvironmentVariables
import com.probrotechsolutions.laffalitto.model.local.jokes.Alias
import com.probrotechsolutions.laffalitto.model.local.jokes.JokeCategory
import com.probrotechsolutions.laffalitto.model.mappers.JokeCategoryResponseMapper
import com.probrotechsolutions.laffalitto.model.network.services.JokeService
import com.probrotechsolutions.laffalitto.model.repositories.JokeRepository
import com.probrotechsolutions.laffalitto.viewmodel.JokeViewModel
import moe.tlaster.precompose.viewmodel.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JokePage(
    environmentVariables: EnvironmentVariables,
    onCategorySelected: (String) -> Unit
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

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Laffalitto") })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when {
                jokeState.isLoading -> {
                    CircularProgressIndicator()
                }
                jokeState.jokeCategories.isNotEmpty() -> {
                    JokeCategoryList(
                        jokeCategoryList = jokeState.jokeCategories,
                        onCategorySelected = onCategorySelected
                    )
                }
                jokeState.errorMsg.isNotEmpty() -> {
                    ErrorMessage(
                        error = jokeState.errorMsg,
                        onRetry = { jokeVm.getCategories() }
                    )
                }
                else -> {
                    Text(
                        text = "No categories available",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { jokeVm.getCategories() }) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}

@Composable
fun JokeCategoryList(
    jokeCategoryList: List<JokeCategory>,
    onCategorySelected: (String) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(jokeCategoryList) {
            JokeCategoryItem(it, onCategorySelected = onCategorySelected)
            Divider()
        }
    }
}

@Composable
fun JokeCategoryItem(
    category: JokeCategory,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp),
    onCategorySelected: (String) -> Unit = {}
) {
    Column(
        modifier = modifier.clickable { onCategorySelected(category.category) }
    ) {
        Text(
            text = category.category,
            style = MaterialTheme.typography.titleMedium
        )
        if (category.aliases.isNotEmpty()) {
            AliasRow(aliases = category.aliases)
        }
    }
}

@Composable
private fun AliasRow(aliases: List<Alias>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        aliases.forEach { alias ->
            Text(
                text = alias.name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun ErrorMessage(error: String, onRetry: () -> Unit) {
    Text(
        text = "Something went wrong: $error",
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodyLarge
    )
    Spacer(modifier = Modifier.height(16.dp))
    Button(onClick = onRetry) {
        Text("Retry")
    }
}
