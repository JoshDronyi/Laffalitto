package com.probrotechsolutions.laffalitto

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.SaveableStateRegistry
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.probrotechsolutions.laffalitto.actual.EnvironmentVariables
import com.probrotechsolutions.laffalitto.ui.joke.JokeSection
import com.probrotechsolutions.laffalitto.viewmodel.TestViewModel
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.stateholder.LocalSavedStateHolder
import moe.tlaster.precompose.stateholder.LocalStateHolder
import moe.tlaster.precompose.stateholder.SavedStateHolder
import moe.tlaster.precompose.stateholder.StateHolder

const val TAG = "CommonHomePage"

@Composable
fun CommonHomePage(
    environmentVariables: EnvironmentVariables
) {
    LaffaLittoTheme {
        val navigator = rememberNavigator()
        Scaffold {
            JokeSection(
                environmentVariables
            )
        }
    }
}

@Composable
fun LaffaLittoTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme {
        content()
    }
}

@Composable
private fun CommonTestContent(
    testViewModel: TestViewModel, modifier: Modifier
) {
    val imageState by testViewModel.state.collectAsState()
    var shouldShow by remember { mutableStateOf(false) }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            shouldShow = !shouldShow
            testViewModel.getImage(TEST_IMAGE)
        }) {
            Text("Show the shit.")
        }
        AnimatedVisibility(shouldShow) {
            Column {
                Text("This a TEST jawn jawn.")
                if (imageState.isLoading) CircularProgressIndicator()
                else CoreImage(image = imageState.image)
            }
        }
    }
}

@Composable
fun CoreImage(
    modifier: Modifier = Modifier,
    image: ByteArray? = null,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.FillBounds
) {
    image?.toImageBitmap()?.let {
        Image(
            bitmap = it,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale
        )
    }
}

expect fun ByteArray.toImageBitmap(): ImageBitmap

const val TEST_IMAGE =
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR7KIz3YTs_ckhppLGEzzSJ5bb1mP34QMf41A&usqp=CAU"