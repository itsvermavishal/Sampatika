package com.wealth.sampatika.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.wealth.sampatika.ui.calculate.CalculateScreen
import com.wealth.sampatika.ui.home.HomeScreen
import com.wealth.sampatika.ui.market.MarketScreen
import com.wealth.sampatika.ui.plan.PlanScreen
import com.wealth.sampatika.ui.details.*
import com.wealth.sampatika.ui.track.TrackScreen

@Composable
fun SampatikaApp() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {

                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("home") },
                    icon = { Icon(Icons.Default.Home, null) },
                    label = { Text("Home") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("plan") },
                    icon = { Icon(Icons.Default.ShoppingCart, null) },
                    label = { Text("Plan") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("calculate") },
                    icon = { Icon(Icons.Default.Clear, null) },
                    label = { Text("Calculate") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("track") },
                    icon = { Icon(Icons.Default.DateRange, null) },
                    label = { Text("Track") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("market") },
                    icon = { Icon(Icons.Default.Settings, null) },
                    label = { Text("Market") }
                )
            }
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