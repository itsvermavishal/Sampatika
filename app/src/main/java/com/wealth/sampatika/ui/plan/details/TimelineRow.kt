package com.wealth.sampatika.ui.plan.details

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp
import com.wealth.sampatika.ui.plan.model.MonthlyProgress
import com.wealth.sampatika.ui.plan.model.SaveStatus
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun TimelineRow(
    month: MonthlyProgress,
    enabled: Boolean,
    isUpcoming: Boolean,
    isFuture: Boolean,
    formatter: SimpleDateFormat,
    onToggle: (SaveStatus) -> Unit
) {

    val infiniteTransition = rememberInfiniteTransition(label = "")

    val animatedPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ),
        label = ""
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {

        Canvas(
            modifier = Modifier
                .width(25.dp)
                .height(80.dp)
        ) {

            val lineColor = when {
                month.isPaid -> Color(0xFF4CAF50)        // GREEN
                isUpcoming -> Color(0xFFE53935)         // RED (only first unpaid)
                isFuture -> Color(0xFFBDBDBD)           // GRAY
                else -> Color.LightGray
            }

            // dotted line
            drawLine(
                color = lineColor,
                start = Offset(size.width / 2, 0f),
                end = Offset(size.width / 2, size.height),
                strokeWidth = 10f,
                pathEffect = PathEffect.dashPathEffect(
                    floatArrayOf(10f, 10f),
                    animatedPhase
                )
            )
            // solid circle
            drawCircle(
                color = lineColor,
                radius = 30f,
                center = Offset(size.width / 2, size.height / 2)
            )

        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {

            Text(
                text = "Month ${month.monthIndex}",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                "Expected: ₹${"%.2f".format(month.expectedAmount)}"
            )

            month.actionTime?.let {
                Text(
                    text = formatter.format(Date(it)),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

        }
        // RIGHT TOGGLE
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (month.isPaid) {
                Text(
                    text = "Saved",
                    color = Color(0xFF2E7D32),
                    style = MaterialTheme.typography.bodySmall
                )
            } else if (isUpcoming) {
                Text(
                    text = "Not Saved",
                    color = Color(0xFFC62828),
                    style = MaterialTheme.typography.bodySmall
                )
            }
// 🚫 Future months → show nothing

            Spacer(Modifier.width(6.dp))

            Switch(
                checked = month.isPaid,
                enabled = enabled,
                onCheckedChange = { isChecked ->
                    onToggle(
                        if (isChecked)
                            SaveStatus.SAVED
                        else
                            SaveStatus.NOT_SAVED
                    )
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF4CAF50),

                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor =
                        if (isUpcoming) Color(0xFFE53935)
                        else Color(0xFFBDBDBD),

                    disabledUncheckedTrackColor = Color.LightGray
                )
            )
        }
    }
}