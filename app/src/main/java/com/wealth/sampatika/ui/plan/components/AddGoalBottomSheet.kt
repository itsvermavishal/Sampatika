package com.wealth.sampatika.ui.plan.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.wealth.sampatika.ui.plan.model.GoalType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGoalBottomSheet(
    type: GoalType,
    onAdd: (String, String, Double, Int) -> Unit,
    onDismiss: () -> Unit
) {

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var months by remember { mutableStateOf("") }

    ModalBottomSheet(onDismissRequest = onDismiss) {

        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {

            Text(
                text = if (type == GoalType.CUSTOM)
                    "Create Your Goal"
                else
                    "Plan your ${type.displayName} Goal",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = title,
                onValueChange = {
                    if (it.length <= 25) title = it   // 1 line limit
                },
                label = { Text("Goal Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = {
                    if (it.length <= 75) description = it  // 2 line safe limit
                },
                label = { Text("Description (Optional)") },
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
                label = { Text("Total Amount") },
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
                label = { Text("Total Months") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    val amountValue = amount.toDoubleOrNull()
                    val monthsValue = months.toIntOrNull()
                    val description = description.takeIf { it.isNotBlank() }.toString()

                    if (title.isNotBlank() &&
                        description.length <= 75 &&
                        amountValue != null &&
                        monthsValue != null &&
                        monthsValue > 0
                    ) {
                        onAdd(title, description, amountValue, monthsValue)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save & Start Planning")
            }

            Spacer(Modifier.height(30.dp))
        }
    }
}