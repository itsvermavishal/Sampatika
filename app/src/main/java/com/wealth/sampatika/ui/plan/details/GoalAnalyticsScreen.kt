package com.wealth.sampatika.ui.plan.details

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.wealth.sampatika.ui.plan.model.GoalPlan

@Composable
fun GoalAnalyticsScreen(
    goal: GoalPlan,
    onBack: () -> Unit
) {

    val data = goal.progressHistory.map { it.paidAmount }

    val animatedProgress by animateFloatAsState(
        targetValue = goal.progressPercent,
        label = ""
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Button(onClick = onBack) {
            Text("Back")
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = "${goal.title} Analytics",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(20.dp))

        // 🔵 Circular Progress Summary
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {

            CircularProgressIndicator(
                progress = animatedProgress,
                strokeWidth = 8.dp,
                modifier = Modifier.size(120.dp)
            )

            Text(
                text = "${(animatedProgress * 100).toInt()}%",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(Modifier.height(30.dp))

        Text(
            text = "Monthly Contribution Graph",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(16.dp))

        if (data.isEmpty() || data.all { it == 0.0 }) {

            Text("No data available yet.")

        } else {

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
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

                    // Draw line
                    previousPoint?.let {
                        drawLine(
                            color = Color.Blue,
                            start = it,
                            end = currentPoint,
                            strokeWidth = 5f,
                            cap = StrokeCap.Round
                        )
                    }

                    // Draw circle point
                    drawCircle(
                        color = Color.Red,
                        radius = 8f,
                        center = currentPoint
                    )

                    previousPoint = currentPoint
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // 🔵 Financial Summary
        Text("Total Target: ₹${goal.totalAmount}")
        Text("Completed: ₹${goal.completedAmount}")
        Text("Remaining: ₹${goal.remainingAmount}")
    }
}