package com.wealth.sampatika.ui.plan.model

data class MonthlyProgress(
    val monthIndex: Int,
    val expectedAmount: Double,
    val isPaid: Boolean = false,
    val paidAmount: Double = 0.0,
    val actionTime: Long? = null,
    val status: SaveStatus? = null,
)

enum class SaveStatus {
    SAVED,
    NOT_SAVED
}
