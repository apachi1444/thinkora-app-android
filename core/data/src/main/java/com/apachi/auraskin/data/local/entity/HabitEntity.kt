package com.apachi.auraskin.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.apachi.auraskin.domain.model.HabitCategory

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val category: HabitCategory = HabitCategory.LIFESTYLE,
    val streak: Int,
    val createdTimestamp: Long
)
