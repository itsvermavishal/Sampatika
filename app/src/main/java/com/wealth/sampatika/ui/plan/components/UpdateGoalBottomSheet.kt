package com.wealth.sampatika.ui.plan.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.wealth.sampatika.ui.plan.model.GoalPlan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateGoalBottomSheet(
    goal: GoalPlan,
    onUpdate: (String, String, Double, Int) -> Unit,
    onDismiss: () -> Unit
) {

    var title by remember { mutableStateOf(goal.title) }
    var description by remember { mutableStateOf(goal.description) }
    var amount by remember { mutableStateOf(goal.totalAmount.toString()) }
    var months by remember { mutableStateOf(goal.totalMonths.toString()) }

    val savedAmount = goal.progressHistory
        .filter { it.isPaid }
        .sumOf { it.paidAmount }

    val savedMonths = goal.progressHistory.count { it.isPaid }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            Text(
                text = "Update Goal",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { if (it.length <= 25) title = it },
                label = { Text("Goal Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = {
                    if (it.length <= 75) description = it
                },
                label = { Text("Description (Why Changing Goal?)") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = {
                    if (it.matches(Regex("^\\d*\\.?\\d*$")))
                        amount = it
                },
                label = {
                    Text("Total Amount (Min ₹${"%.2f".format(savedAmount)})")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = months,
                onValueChange = {
                    if (it.matches(Regex("^\\d*$")))
                        months = it
                },
                label = {
                    Text("Total Months (Min ${savedMonths + 1})")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    val newAmount = amount.toDoubleOrNull()
                    val newMonths = months.toIntOrNull()
                    val newDescription: String = description.takeIf { it.isNotBlank() }.toString()

                    if (
                        newAmount != null &&
                        newMonths != null &&
                        newAmount >= savedAmount &&
                        newMonths >= savedMonths + 1
                    ) {
                        onUpdate(title, newDescription, newAmount, newMonths)
                        onDismiss()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update Plan")
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}