package com.wealth.sampatika.ui.plan.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wealth.sampatika.R
import com.wealth.sampatika.ui.plan.model.GoalType

@Composable
fun GoalCategoryGrid(
    selectedType: GoalType,
    onSelect: (GoalType) -> Unit
) {
    val categories = GoalType.values().toList()

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        categories.chunked(3).forEach { rowItems ->

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                rowItems.forEach { type ->

                    GoalCategoryCard(
                        type = type,
                        imageRes = getGoalImage(type),
                        description = getGoalDescription(type),
                        isSelected = type == selectedType,
                        onClick = { onSelect(type) },
                        modifier = Modifier.weight(1f)
                    )
                }

                // If odd number of items, fill empty space
                if (rowItems.size < 3) {
                    repeat(3 - rowItems.size) {
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

private fun getGoalImage(type: GoalType): Int {
    return when (type) {
        GoalType.MARRIAGE -> R.drawable.sampatikalogo
        GoalType.EDUCATION -> R.drawable.sampatikaapplogo
        GoalType.HOUSE -> R.drawable.sampatikalogo
        GoalType.VACATION -> R.drawable.sampatikalogo
        GoalType.PLOT -> R.drawable.sampatikaapplogo
        GoalType.CAR -> R.drawable.sampatikalogo
        GoalType.BIKE -> R.drawable.sampatikaapplogo
        GoalType.PHONE -> R.drawable.sampatikaapplogo
        GoalType.RETIREMENT -> R.drawable.sampatikalogo
        GoalType.CUSTOM -> R.drawable.sampatikalogo
    }
}

private fun getGoalDescription(type: GoalType): String {
    return when (type) {
        GoalType.MARRIAGE -> "Plan and fund your dream wedding smartly."
        GoalType.EDUCATION -> "Secure future education with disciplined savings."
        GoalType.HOUSE -> "Build your home fund step by step."
        GoalType.VACATION -> "Save monthly for your dream trip."
        GoalType.PLOT -> "Invest in land with structured planning."
        GoalType.CAR -> "Own your car without financial stress."
        GoalType.BIKE -> "Plan your bike purchase responsibly."
        GoalType.PHONE -> "Upgrade gadgets with smart savings."
        GoalType.RETIREMENT -> "Prepare early for peaceful retirement."
        GoalType.CUSTOM -> "Create your own personalized financial goal."
    }
}