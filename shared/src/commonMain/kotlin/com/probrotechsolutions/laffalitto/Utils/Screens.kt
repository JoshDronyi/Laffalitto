package com.probrotechsolutions.laffalitto.Utils

sealed class Screens{
    enum class JokeScreens(val route:String){
        HOME("home"),
        JOKE("joke")
    }

}
