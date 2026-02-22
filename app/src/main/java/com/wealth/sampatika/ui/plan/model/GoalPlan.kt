package com.wealth.sampatika.ui.plan.model

import java.util.UUID

data class GoalPlan(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String = "",
    val type: GoalType,
    val totalAmount: Double,
    val totalMonths: Int,
    val monthlyTarget: Double,
    val yearlyTarget: Double,
    val createdAt: Long = System.currentTimeMillis(),
    val progressHistory: List<MonthlyProgress> = emptyList(),
    val backgroundUrl: String? = null
) {

    /**
     * Total amount completed till now
     */
    val completedAmount: Double
        get() = progressHistory
            .filter { it.isPaid }
            .sumOf { it.paidAmount }

    /**
     * Remaining amount to be saved
     */
    val remainingAmount: Double
        get() = (totalAmount - completedAmount)
            .coerceAtLeast(0.0)

    /**
     * Progress percentage (0f - 1f)
     */
    val progressPercent: Float
        get() = if (totalAmount > 0)
            (completedAmount / totalAmount).toFloat()
        else 0f

    /**
     * Completed months count
     */
    val completedMonths: Int
        get() = progressHistory.count { it.isPaid }

    /**
     * Remaining months
     */
    val remainingMonths: Int
        get() = totalMonths - completedMonths
}