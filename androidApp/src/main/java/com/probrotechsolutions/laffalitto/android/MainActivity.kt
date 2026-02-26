package com.probrotechsolutions.laffalitto.android

import android.os.Bundle
import androidx.activity.compose.setContent
import com.probrotechsolutions.laffalitto.CommonHomePage
import com.probrotechsolutions.laffalitto.actual.EnvironmentVariables
import moe.tlaster.precompose.lifecycle.PreComposeActivity

class MainActivity : PreComposeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                CommonHomePage(EnvironmentVariables())
            }
        }
    }
}
