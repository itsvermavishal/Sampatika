package com.wealth.sampatika.ui.plan.components

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.wealth.sampatika.ui.plan.model.GoalType

fun goalGradient(type: GoalType): Brush {
    return when (type) {
        GoalType.MARRIAGE -> Brush.verticalGradient(
            listOf(Color(0xFFFF9A9E), Color(0xFFFAD0C4))
        )
        GoalType.EDUCATION -> Brush.verticalGradient(
            listOf(Color(0xFF89F7FE), Color(0xFF66A6FF))
        )
        GoalType.HOUSE -> Brush.verticalGradient(
            listOf(Color(0xFFA18CD1), Color(0xFFFBC2EB))
        )
        else -> Brush.verticalGradient(
            listOf(Color(0xFF11998E), Color(0xFF38EF7D))
        )
    }
}