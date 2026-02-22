package com.wealth.sampatika.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.wealth.sampatika.ui.calculate.CalculateScreen
import com.wealth.sampatika.ui.home.HomeScreen
import com.wealth.sampatika.ui.market.MarketScreen
import com.wealth.sampatika.ui.plan.PlanScreen
import com.wealth.sampatika.ui.track.TrackScreen
import com.wealth.sampatika.ui.details.DetailsScreen

@Composable
fun SampatikaApp() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            SampatikaBottomBar(navController)   // ✅ HERE
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {

            composable("home") { HomeScreen(navController) }
            composable("plan") { PlanScreen() }
            composable("calculate") { CalculateScreen() }
            composable("track") { TrackScreen() }
            composable("market") { MarketScreen(navController) }

            composable("details/{symbol}") { backStack ->
                val symbol = backStack.arguments?.getString("symbol") ?: ""
                DetailsScreen(symbol)
            }
        }
    }
}