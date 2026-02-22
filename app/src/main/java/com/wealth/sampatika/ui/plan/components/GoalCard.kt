package com.wealth.sampatika.ui.plan.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wealth.sampatika.ui.plan.model.GoalPlan
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GoalCard(
    goal: GoalPlan,
    onExploreClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onUpdateClick: () -> Unit,
    onExportClick: () -> Unit
) {

    val animatedProgress by animateFloatAsState(
        targetValue = goal.progressPercent,
        label = ""
    )

    Card(
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 5.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {

        Box(
            modifier = Modifier
                .background(goalGradient(goal.type))
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {

            Column {

                // 🔹 TITLE ROW
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = goal.title,
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1
                    )

                    IconButton(onClick = onDeleteClick, modifier = Modifier.size(22.dp)) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))

                // 🔹 PROGRESS  + Description + INFO ROW
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(contentAlignment = Alignment.Center) {

                        CircularProgressIndicator(
                            progress = { animatedProgress },
                            strokeWidth = 5.dp,
                            modifier = Modifier.size(85.dp),
                            color = Color.White,
                            trackColor = Color.White.copy(alpha = 0.25f)
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "${(animatedProgress * 100).toInt()}%",
                                color = Color.White,
                                style = MaterialTheme.typography.labelLarge
                            )

                            Text(
                                text = "Completed",
                                color = Color.White.copy(alpha = 0.85f),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }

                    Spacer(Modifier.width(15.dp))

                    Column {

                        if (goal.description.isNotBlank()) {

                            Spacer(Modifier.height(5.dp))

                            Text(
                                text = goal.description,
                                color = Color.White.copy(alpha = 0.85f),
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 2
                            )

                        }

                        Spacer(Modifier.height(6.dp))

                        // 🔹 FINANCIAL INFO ROW 1
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(
                                "Total ₹${"%.2f".format(goal.totalAmount)}",
                                color = Color.White,
                                style = MaterialTheme.typography.labelMedium
                            )

                            Text(
                                "Months ${goal.totalMonths}",
                                color = Color.White,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }

                        Spacer(Modifier.height(5.dp))

                        // 🔹 FINANCIAL INFO ROW 2
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(
                                "Saved ₹${"%.2f".format(goal.completedAmount)}",
                                color = Color.White,
                                style = MaterialTheme.typography.labelMedium
                            )

                            Text(
                                "Remaining ₹${"%.2f".format(goal.remainingAmount)}",
                                color = Color.White,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }

                Spacer(Modifier.height(15.dp))

                // 🔹 ACTION BUTTON ROW
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    ActionChip("Analytics", onHistoryClick)
                    ActionChip("Update", onUpdateClick)
                    ActionChip("Export", onExportClick)
                    ActionChip("Explore", onExploreClick)
                }
            }
        }
    }
}
@Composable
private fun RowScope.ActionChip(
    text: String,
    onClick: () -> Unit
) {

    var pressed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val backgroundColor by animateColorAsState(
        targetValue = if (pressed)
            Color.White
        else
            Color.White.copy(alpha = 0.15f),
        label = ""
    )

    val textColor by animateColorAsState(
        targetValue = if (pressed)
            Color(0xFF11998E)
        else
            Color.White,
        label = ""
    )

    Button(
        onClick = {
            pressed = true
            onClick()

            scope.launch {
                delay(300)
                pressed = false
            }
        },
        modifier = Modifier
            .weight(1f)
            .height(36.dp),
        shape = RoundedCornerShape(15),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1
        )
    }
}