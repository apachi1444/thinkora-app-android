package com.apachi.thinkora.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apachi.thinkora.data.local.entity.ReadHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadHistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(history: ReadHistoryEntity)

    @Query("SELECT * FROM read_history WHERE date = :date")
    suspend fun getHistoryForDate(date: Long): List<ReadHistoryEntity>

    @Query("SELECT * FROM read_history ORDER BY date DESC")
    fun getAllHistory(): Flow<List<ReadHistoryEntity>>
    
    @Query("SELECT count(*) FROM read_history WHERE quoteId = :quoteId AND date = :date")
    suspend fun isQuoteReadOnDate(quoteId: String, date: Long): Int
}
