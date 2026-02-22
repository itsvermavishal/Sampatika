package com.wealth.sampatika.ui.plan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wealth.sampatika.ui.plan.components.AddGoalBottomSheet
import com.wealth.sampatika.ui.plan.components.GoalCard
import com.wealth.sampatika.ui.plan.components.GoalCategoryGrid
import com.wealth.sampatika.ui.plan.components.UpdateGoalBottomSheet
import com.wealth.sampatika.ui.plan.details.GoalAnalyticsScreen
import com.wealth.sampatika.ui.plan.details.GoalDetailScreen
import com.wealth.sampatika.ui.plan.details.GoalPdfPreviewScreen
import com.wealth.sampatika.ui.plan.model.GoalPlan
import com.wealth.sampatika.ui.plan.model.GoalType
import com.wealth.sampatika.ui.plan.viewmodel.PlanViewModel

@Composable
fun PlanScreen(
    viewModel: PlanViewModel = viewModel()
) {

    var selectedGoalType by remember { mutableStateOf(GoalType.MARRIAGE) }
    var showSheet by remember { mutableStateOf(false) }
    var selectedGoal by remember { mutableStateOf<GoalPlan?>(null) }
    var screenState by remember { mutableStateOf("MAIN") }
    var goalToUpdate by remember { mutableStateOf<GoalPlan?>(null) }

    val backgroundGradient = Brush.verticalGradient(
        listOf(
            Color(0xFFE3F2FD),
            Color(0xFFF1F8E9)
        )
    )
    val context = LocalContext.current

    when (screenState) {
        "DETAIL" -> {
            GoalDetailScreen(
                goalId = selectedGoal!!.id,
                viewModel = viewModel,
                onBack = { screenState = "MAIN" },
                onExportClick = { goal ->
                    selectedGoal = goal
                    screenState = "PDF_PREVIEW"
                }
            )
            return
        }

        "ANALYTICS" -> {
            GoalAnalyticsScreen(
                goal = selectedGoal!!,
                onBack = { screenState = "MAIN" }
            )
            return
        }

        "PDF_PREVIEW" -> {
            GoalPdfPreviewScreen(
                goal = selectedGoal!!,
                onBack = { screenState = "MAIN" }
            )
            return
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    selectedGoalType = GoalType.CUSTOM
                    showSheet = true },
                icon = {
                    Icon(Icons.Default.Add, contentDescription = null)
                },
                text = {
                    Text("Add Your Goal")
                }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradient)
        ) {

            LazyColumn(
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 70.dp
                )
            ) {

                // 🔹 HEADER
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        IconButton(onClick = { /* handle back */ }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }

                        Text(
                            text = "Start Your Planning Today",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    Spacer(Modifier.height(20.dp))
                }

                // 🔹 GRID (Non-scrollable inside LazyColumn)
                item {
                    GoalCategoryGrid(
                        selectedType = selectedGoalType,
                        onSelect = { type ->
                            selectedGoalType = type
                            showSheet = true
                        }
                    )

                    Spacer(Modifier.height(20.dp))
                }

                // 🔹 GOALS
                val filteredGoals =
                    viewModel.goals
                        .filter { it.type == selectedGoalType }
                        .sortedByDescending { it.createdAt }

                if (filteredGoals.isEmpty()) {

                    item {
                        Text(
                            text = "No plans yet for ${selectedGoalType.displayName}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                } else {

                    items(filteredGoals) { goal ->

                        GoalCard(
                            goal = goal,
                            onExploreClick = {
                                selectedGoal = goal
                                screenState = "DETAIL"
                            },
                            onHistoryClick = {
                                selectedGoal = goal
                                screenState = "ANALYTICS"
                            },
                            onDeleteClick = {
                                viewModel.deleteGoal(goal.id)
                            },
                            onUpdateClick = {
                                goalToUpdate = goal
                            },
                            onExportClick = {
                                selectedGoal = goal
                                screenState = "PDF_PREVIEW"
                            }
                        )
                    }
                }
            }
        }
    }

    if (showSheet) {
        AddGoalBottomSheet(
            type = selectedGoalType,  // 🔥 IMPORTANT
            onAdd = { title, description, amount, months ->
                viewModel.addGoal(title, description, selectedGoalType, amount, months)
                showSheet = false
            },
            onDismiss = { showSheet = false }
        )
    }

    goalToUpdate?.let { goal ->

        UpdateGoalBottomSheet(
            goal = goal,
            onUpdate = { title, description, amount, months ->
                viewModel.updateGoal(goal.id, title, description, amount, months)
            },
            onDismiss = { goalToUpdate = null }
        )
    }
}