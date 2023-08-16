package com.probrotechsolutions.laffalitto.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.tooling.preview.Preview
import com.probrotechsolutions.laffalitto.CommonHomePage
import com.probrotechsolutions.laffalitto.actual.EnvironmentVariables
import moe.tlaster.precompose.lifecycle.PreComposeActivity
import moe.tlaster.precompose.stateholder.SavedStateHolder

const val TAG = "MainActivity"
class MainActivity() : PreComposeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                CommonHomePage(EnvironmentVariables())
            }
        }
    }
}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!")
    }
}
