package com.wealth.sampatika.ui.navigation

import com.wealth.sampatika.R

sealed class NavItem(
    val route: String,
    val title: String,
    val icon: Int
) {
    object Home : NavItem("home", "Home", R.drawable.house)
    object Plan : NavItem("plan", "Plan", R.drawable.plann)
    object Calculate : NavItem("calculate", "Calculate", R.drawable.calculate)
    object Track : NavItem("track", "Track", R.drawable.track)
    object Market : NavItem("market", "Market", R.drawable.market)
}