package com.apachi.auraskin.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "habit_completions")
data class HabitCompletionEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val habitId: String,
    val completionTimestamp: Long
)
