package com.plcoding.widgetswithcompose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.plcoding.widgetswithcompose.data.local.dao.QuoteDao
import com.plcoding.widgetswithcompose.data.local.dao.ReadHistoryDao
import com.plcoding.widgetswithcompose.data.local.entity.QuoteEntity
import com.plcoding.widgetswithcompose.data.local.entity.ReadHistoryEntity

@Database(
    entities = [QuoteEntity::class, ReadHistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class QuoteDatabase : RoomDatabase() {
    abstract val quoteDao: QuoteDao
    abstract val readHistoryDao: ReadHistoryDao
}
