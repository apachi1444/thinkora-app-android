package com.apachi.auraskin.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.apachi.auraskin.domain.model.Mood

@Entity(tableName = "skin_logs")
data class SkinLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String,
    val photoUri: String?,
    val conditionScore: Int,
    val notes: String = "",
    val mood: Mood = Mood.NEUTRAL,
    val triggers: String = "[]", // Stored as a JSON Array string for Room simplicity
    val createdAt: Long
)
