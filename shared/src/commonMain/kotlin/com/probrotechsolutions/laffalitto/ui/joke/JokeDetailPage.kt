package com.probrotechsolutions.laffalitto.ui.joke

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.probrotechsolutions.laffalitto.actual.EnvironmentVariables
import com.probrotechsolutions.laffalitto.model.local.jokes.Joke
import com.probrotechsolutions.laffalitto.model.local.jokes.JokeType
import com.probrotechsolutions.laffalitto.model.mappers.JokeCategoryResponseMapper
import com.probrotechsolutions.laffalitto.model.mappers.JokeResponseMapper
import com.probrotechsolutions.laffalitto.model.network.services.JokeService
import com.probrotechsolutions.laffalitto.model.repositories.JokeRepository
import com.probrotechsolutions.laffalitto.viewmodel.JokeDetailViewModel
import moe.tlaster.precompose.viewmodel.viewModel

@Composable
fun JokeDetailPage(
    category: String,
    environmentVariables: EnvironmentVariables,
    onBack: () -> Unit
) {
    val viewModel = viewModel(JokeDetailViewModel::class, listOf(category)) {
        JokeDetailViewModel(
            category = category,
            jokeRepo = JokeRepository(
                jokeService = JokeService(environmentVariables),
                jokeResponseMapper = JokeCategoryResponseMapper(),
                jokeMapper = JokeResponseMapper()
            )
        )
    }
    val state by viewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(category) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator()
                }
                state.joke != null -> {
                    state.joke?.let { JokeContent(joke = it) }
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(onClick = { viewModel.fetchJoke() }) {
                        Text("Another one")
                    }
                }
                state.errorMsg.isNotEmpty() -> {
                    Text(
                        text = state.errorMsg,
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.body1
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.fetchJoke() }) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}

@Composable
private fun JokeContent(joke: Joke) {
    when (joke.type) {
        JokeType.SINGLE -> {
            Text(
                text = joke.joke ?: "",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxWidth()
            )
        }
        JokeType.TWO_PART -> {
            Text(
                text = joke.setup ?: "",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Divider()
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = joke.delivery ?: "",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
