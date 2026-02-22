package com.wealth.sampatika.ui.plan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.*
import com.wealth.sampatika.ui.plan.model.*
import java.math.RoundingMode

class PlanViewModel : ViewModel() {

    var goals by mutableStateOf<List<GoalPlan>>(emptyList())
        private set

    fun addGoal(
        title: String,
        description: String,
        type: GoalType,
        totalAmount: Double,
        totalMonths: Int
    ) {
        if (totalMonths <= 0 || totalAmount <= 0.0) return

        val monthly = totalAmount / totalMonths
        val yearly = monthly * 12

        val schedule = (1..totalMonths).map {
            MonthlyProgress(
                monthIndex = it,
                expectedAmount = monthly
            )
        }

        val newGoal = GoalPlan(
            title = title,
            description = description,
            type = type,
            totalAmount = totalAmount,
            totalMonths = totalMonths,
            monthlyTarget = monthly,
            yearlyTarget = yearly,
            progressHistory = schedule
        )

        goals = listOf(newGoal) + goals
    }

    fun updateGoal(
        goalId: String,
        newTitle: String,
        newDescription: String,
        newTotalAmount: Double,
        newTotalMonths: Int
    ) {

        goals = goals.map { goal ->

            if (goal.id == goalId) {

                val savedMonthsList = goal.progressHistory.filter { it.isPaid }
                val savedAmount = savedMonthsList.sumOf { it.paidAmount }
                val savedMonthsCount = savedMonthsList.size

                // 🚫 Validation rules
                if (newTotalAmount < savedAmount) return@map goal
                if (newTotalMonths < savedMonthsCount + 1) return@map goal

                val remainingAmount = newTotalAmount - savedAmount
                val remainingMonths = newTotalMonths - savedMonthsCount

                val newMonthly =
                    if (remainingMonths > 0)
                        (remainingAmount / remainingMonths)
                            .toBigDecimal()
                            .setScale(2, RoundingMode.HALF_UP)
                            .toDouble()
                    else 0.0

                val updatedHistory =
                    (1..newTotalMonths).map { index ->

                        val oldMonth =
                            goal.progressHistory
                                .find { it.monthIndex == index }

                        when {
                            oldMonth != null && oldMonth.isPaid ->
                                oldMonth // KEEP SAVED DATA

                            oldMonth != null ->
                                oldMonth.copy(expectedAmount = newMonthly)

                            else ->
                                MonthlyProgress(
                                    monthIndex = index,
                                    expectedAmount = newMonthly
                                )
                        }
                    }

                goal.copy(
                    title = newTitle,
                    description = newDescription,
                    totalAmount = newTotalAmount,
                    totalMonths = newTotalMonths,
                    monthlyTarget = newMonthly,
                    yearlyTarget = newMonthly * 12,
                    progressHistory = updatedHistory
                )

            } else goal
        }
    }

    fun deleteGoal(goalId: String) {
        goals = goals.filterNot { it.id == goalId }
    }

    fun updateMonthStatus(
        goalId: String,
        monthIndex: Int,
        status: SaveStatus
    ) {

        goals = goals.map { goal ->

            if (goal.id == goalId) {

                val updatedHistory = goal.progressHistory.map {

                    if (it.monthIndex == monthIndex) {

                        when (status) {

                            SaveStatus.SAVED -> it.copy(
                                isPaid = true,
                                paidAmount = it.expectedAmount,
                                actionTime = System.currentTimeMillis(),
                                status = SaveStatus.SAVED
                            )

                            SaveStatus.NOT_SAVED -> it.copy(
                                isPaid = false,
                                paidAmount = 0.0,
                                actionTime = System.currentTimeMillis(),
                                status = SaveStatus.NOT_SAVED
                            )
                        }

                    } else it
                }

                val paidAmount =
                    updatedHistory.filter { it.isPaid }.sumOf { it.paidAmount }

                val remaining =
                    goal.totalAmount - paidAmount

                val remainingMonths =
                    updatedHistory.count { !it.isPaid }

                val newMonthly =
                    if (remainingMonths > 0)
                        (remaining / remainingMonths)
                            .toBigDecimal()
                            .setScale(2, RoundingMode.HALF_UP)
                            .toDouble()
                    else 0.0

                goal.copy(
                    progressHistory = updatedHistory.map {
                        if (!it.isPaid)
                            it.copy(expectedAmount = newMonthly)
                        else it
                    }
                )

            } else goal
        }
    }
}