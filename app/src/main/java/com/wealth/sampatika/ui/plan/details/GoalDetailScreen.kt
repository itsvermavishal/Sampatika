package com.wealth.sampatika.ui.plan.details

import android.R.attr.progress
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wealth.sampatika.ui.plan.model.GoalPlan
import com.wealth.sampatika.ui.plan.model.MonthlyProgress
import com.wealth.sampatika.ui.plan.viewmodel.PlanViewModel
import com.wealth.sampatika.utils.exportGoalToPdf
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalDetailScreen(
    goalId: String,
    viewModel: PlanViewModel,
    onBack: () -> Unit,
    onExportClick: (GoalPlan) -> Unit
) {
    val goal = viewModel.goals.first { it.id == goalId }

    val context = LocalContext.current

    val formatter = remember {
        SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    }

    val totalMonths = goal.progressHistory.size
    val savedMonths = goal.progressHistory.count { it.isPaid }

    val progressPercent =
        if (totalMonths == 0) 0f
        else savedMonths.toFloat() / totalMonths.toFloat()

    val animatedProgress by animateFloatAsState(
        targetValue = progressPercent,
        label = ""
    )

    val percentageText = (progressPercent * 100).toInt()

    val targetColor = when {
        percentageText == 0 -> Color(0xFFE53935)
        percentageText == 100 -> Color(0xFF4CAF50)
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
    }

    val progressColor by animateColorAsState(
        targetValue = targetColor,
        label = ""
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            contentAlignment = Alignment.Center
        ) {

            // Center Title
            Text(
                text = goal.title,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 16.sp
            )

            // 🔹 Left Back Button
            FilledTonalButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .height(36.dp),
                colors = ButtonDefaults.filledTonalButtonColors()
            ) {
                Text("Back")
            }

// 🔹 Right Export Button
            FilledTonalButton(
                onClick = { onExportClick( goal) },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .height(36.dp),
                colors = ButtonDefaults.filledTonalButtonColors()
            ) {
                Text("Export")
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Progress",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                    color = progressColor
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "$percentageText% Completed",
                        style = MaterialTheme.typography.titleMedium,
                        color = progressColor
                    )

                    if (percentageText == 100) {
                        Spacer(Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = progressColor
                        )
                    }
                }
            }

            Spacer(Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFE0E0E0))
            ) {
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp),
                    color = progressColor,
                    trackColor = Color.Transparent
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        val firstUnpaidMonthIndex =
            goal.progressHistory
                .sortedBy { it.monthIndex }
                .firstOrNull { !it.isPaid }
                ?.monthIndex

        LazyColumn {

            val sortedList = goal.progressHistory
                .sortedWith(
                    compareBy<MonthlyProgress> { it.isPaid }
                        .thenBy { it.monthIndex }
                )

            items(sortedList, key = { it.monthIndex }) { month ->

                val isUpcoming = month.monthIndex == firstUnpaidMonthIndex
                val isFuture = !month.isPaid && !isUpcoming

                val previousMonth = sortedList
                    .firstOrNull { it.monthIndex == month.monthIndex - 1 }

                val isEnabled = previousMonth == null || previousMonth.isPaid

                TimelineRow(
                    month = month,
                    enabled = isEnabled,
                    isUpcoming = isUpcoming,
                    isFuture = isFuture,
                    formatter = formatter,
                    onToggle = { status ->
                        viewModel.updateMonthStatus(
                            goal.id,
                            month.monthIndex,
                            status
                        )
                    }
                )
            }
        }
    }
}