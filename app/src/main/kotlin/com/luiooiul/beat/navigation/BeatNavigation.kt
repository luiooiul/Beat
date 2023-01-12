package com.luiooiul.beat.navigation

import androidx.navigation.NavHostController

object BeatDestinations {
    const val HOME_ROUTE = "home_route"
    const val SETTING_ROUTE = "setting_route"
}

class BeatNavActions(private val navController: NavHostController) {

    fun navigateToSetting() {
        navController.navigate(BeatDestinations.SETTING_ROUTE)
    }

    fun navigateBack() {
        navController.popBackStack()
    }
}