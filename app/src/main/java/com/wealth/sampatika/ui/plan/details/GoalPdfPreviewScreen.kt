package com.wealth.sampatika.ui.plan.details

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.wealth.sampatika.ui.plan.model.GoalPlan
import com.wealth.sampatika.utils.generateAndSavePdf
import kotlin.math.roundToInt

@Composable
fun GoalPdfPreviewScreen(
    goal: GoalPlan,
    onBack: () -> Unit
) {

    val context = LocalContext.current

    val progressPercent =
        if (goal.totalAmount == 0.0) 0f
        else (goal.completedAmount / goal.totalAmount).toFloat()

    val animatedProgress by animateFloatAsState(
        targetValue = progressPercent,
        label = ""
    )

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                FilledTonalButton(onClick = onBack) {
                    Text("Back")
                }

                FilledTonalButton(
                    onClick = {
                        generateAndSavePdf(context, goal)
                    }
                ) {
                    Text("Download PDF")
                }
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // 🔹 Header
            item {
                Text(
                    text = "Goal Report Preview",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            // 🔹 Goal Summary
            item {
                Card {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("Title: ${goal.title}")
                        Text("Total Amount: ₹${"%.2f".format(goal.totalAmount)}")
                        Text("Completed: ₹${"%.2f".format(goal.completedAmount)}")
                        Text("Remaining: ₹${"%.2f".format(goal.remainingAmount)}")
                        Text("Progress: ${(progressPercent * 100).roundToInt()}%")
                    }
                }
            }

            // 🔹 Circular Progress Preview
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator(
                        progress = animatedProgress,
                        strokeWidth = 8.dp,
                        modifier = Modifier.size(120.dp)
                    )

                    Text(
                        text = "${(progressPercent * 100).roundToInt()}%",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            // 🔹 Graph Preview
            item {
                Text(
                    text = "Monthly Contribution Graph",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            item {

                val data = goal.progressHistory.map { it.paidAmount }

                if (data.isEmpty() || data.all { it == 0.0 }) {

                    Text("No contribution data yet.")

                } else {

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    ) {

                        val maxValue = data.maxOrNull() ?: 1.0
                        val widthStep = size.width / (data.size + 1)

                        var previousPoint: Offset? = null

                        data.forEachIndexed { index, value ->

                            val x = widthStep * (index + 1)

                            val normalized =
                                (value / maxValue).coerceIn(0.0, 1.0)

                            val y =
                                size.height - (normalized * size.height).toFloat()

                            val currentPoint = Offset(x, y)

                            previousPoint?.let {
                                drawLine(
                                    color = Color.Blue,
                                    start = it,
                                    end = currentPoint,
                                    strokeWidth = 5f,
                                    cap = StrokeCap.Round
                                )
                            }

                            drawCircle(
                                color = Color.Red,
                                radius = 8f,
                                center = currentPoint
                            )

                            previousPoint = currentPoint
                        }
                    }
                }
            }

            // 🔹 History Section
            item {
                Text(
                    text = "Monthly History",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            items(goal.progressHistory) { month ->

                Card {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Column {
                            Text("Month ${month.monthIndex}")
                            Text("Expected: ₹${"%.2f".format(month.expectedAmount)}")
                        }

                        Text(
                            text = if (month.isPaid)
                                "Paid ₹${"%.2f".format(month.paidAmount)}"
                            else
                                "Not Paid",
                            color = if (month.isPaid)
                                Color(0xFF2E7D32)
                            else
                                Color.Red
                        )
                    }
                }
            }

            item {
                Spacer(Modifier.height(40.dp))
            }
        }
    }
}