package com.luiooiul.beat.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.luiooiul.beat.feature.home.HomeScreen
import com.luiooiul.beat.feature.setting.SettingScreen

@Composable
fun BeatNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = BeatDestinations.HOME_ROUTE,
    navActions: BeatNavActions = remember(navController) { BeatNavActions(navController) }
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(BeatDestinations.HOME_ROUTE) {
            HomeScreen(
                onSettingClick = navActions::navigateToSetting
            )
        }
        composable(BeatDestinations.SETTING_ROUTE) {
            SettingScreen(
                onBackClick = navActions::navigateBack
            )
        }
    }
}