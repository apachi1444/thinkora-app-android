package com.apachi.auraskin.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apachi.auraskin.data.local.entity.SkinLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SkinLogDao {
    @Query("SELECT * FROM skin_logs ORDER BY date DESC")
    fun getAllLogs(): Flow<List<SkinLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSkinLog(log: SkinLogEntity): Long
    
    @Query("SELECT * FROM skin_logs WHERE date = :date LIMIT 1")
    suspend fun getLogByDate(date: String): SkinLogEntity?
}
