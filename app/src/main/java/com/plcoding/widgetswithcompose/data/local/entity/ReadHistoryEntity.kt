package com.plcoding.widgetswithcompose.data.local.entity

import androidx.room.Entity

@Entity(tableName = "read_history", primaryKeys = ["date", "quoteId"])
data class ReadHistoryEntity(
    val date: Long, // Epoch Day
    val quoteId: String
)
