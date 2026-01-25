package com.wealth.sampatika.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WatchlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWatchlist(item: WatchlistEntity)

    @Query("SELECT * FROM watchlist")
    suspend fun getWatchlist(): List<WatchlistEntity>

    @Query("DELETE FROM watchlist WHERE id = :coinId")
    suspend fun removeFromWatchlist(coinId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist WHERE id = :coinId)")
    suspend fun isInWatchlist(coinId: Int): Boolean
}