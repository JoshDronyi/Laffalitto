package com.probrotechsolutions.laffalitto

import com.probrotechsolutions.laffalitto.actual.EnvironmentVariables
import moe.tlaster.precompose.PreComposeApplication

const val TITLE = "ios_app"
fun MainViewController() =
    PreComposeApplication(TITLE) {
        CommonHomePage(EnvironmentVariables())
    }
