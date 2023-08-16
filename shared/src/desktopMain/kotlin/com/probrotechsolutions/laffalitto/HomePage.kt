package com.probrotechsolutions.laffalitto

import androidx.compose.runtime.Composable
import com.probrotechsolutions.laffalitto.actual.EnvironmentVariables
import java.awt.GraphicsEnvironment

@Composable
fun HomePage(environment: EnvironmentVariables) {
    CommonHomePage(environment)
}