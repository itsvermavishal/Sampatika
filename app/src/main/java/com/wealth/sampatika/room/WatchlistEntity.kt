package com.wealth.sampatika.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "watchlist")
data class WatchlistEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val symbol: String,
    val price: Double,
    val percentChange24h: Double
) : Serializable