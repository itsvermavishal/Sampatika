package com.wealth.sampatika.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch

@Composable
fun SampatikaBottomBar(navController: NavController) {

    val items = listOf(
        NavItem.Home,
        NavItem.Plan,
        NavItem.Calculate,
        NavItem.Track,
        NavItem.Market
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val selectedIndex = items.indexOfFirst {
        currentRoute?.startsWith(it.route) == true
    }.coerceAtLeast(0)

    // 🔥 Elastic indicator animation
    val indicatorOffset by animateFloatAsState(
        targetValue = selectedIndex.toFloat(),
        animationSpec = spring(
            dampingRatio = 0.7f,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        Color(0xFF6A11CB),
                        Color(0xFF2575FC)
                    )
                )
            )
    ) {

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            items.forEachIndexed { index, item ->

                val selected = index == selectedIndex

                // 🔥 Bounce animation
                val scale by animateFloatAsState(
                    targetValue = if (selected) 1.2f else 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    label = ""
                )

                val selectedColor = Color(0xFF00E5FF)
                val unselectedColor = Color.White.copy(alpha = 0.6f)

                val iconColor by animateColorAsState(
                    targetValue = if (selected) selectedColor else unselectedColor,
                    animationSpec = tween(250),
                    label = ""
                )

                val textColor by animateColorAsState(
                    targetValue = if (selected) selectedColor else unselectedColor,
                    animationSpec = tween(250),
                    label = ""
                )

                // 🔥 Glow pulse effect
                val infiniteTransition = rememberInfiniteTransition(label = "")
                val glowAlpha by infiniteTransition.animateFloat(
                    initialValue = 0.3f,
                    targetValue = 0.8f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = ""
                )

                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .clickable {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        // 🔥 Circular glass background
                        Box(
                            modifier = if (selected)
                                Modifier
                                    .size(40.dp)
                                    .background(
                                        Color.White.copy(alpha = 0.15f),
                                        shape = CircleShape
                                    )
                                    .shadow(
                                        elevation = 12.dp,
                                        shape = CircleShape,
                                        ambientColor = selectedColor.copy(alpha = glowAlpha),
                                        spotColor = selectedColor.copy(alpha = glowAlpha)
                                    )
                            else Modifier,
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = item.title,
                                tint = iconColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        AnimatedVisibility(
                            visible = selected,
                            enter = fadeIn() + expandHorizontally(),
                            exit = fadeOut() + shrinkHorizontally()
                        ) {
                            Text(
                                text = item.title,
                                color = textColor,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // 🔥 Elastic sliding indicator
        Box(
            modifier = Modifier
                .offset(
                    x = indicatorOffset *
                            (LocalConfiguration.current.screenWidthDp.dp / items.size)
                )
                .align(Alignment.BottomStart)
                .width(LocalConfiguration.current.screenWidthDp.dp / items.size)
                .height(5.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(Color.White, Color.Cyan)
                    ),
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                )
        )
    }
}