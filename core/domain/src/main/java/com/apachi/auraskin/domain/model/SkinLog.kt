package com.apachi.auraskin.domain.model

enum class Mood { GREAT, GOOD, NEUTRAL, BAD, TERRIBLE }

data class SkinLog(
    val id: Long = 0,
    val date: String,               // ISO-8601 "YYYY-MM-DD"
    val photoUri: String?,          
    val conditionScore: Int,        // 1 (severe) to 5 (clear)
    val notes: String = "",
    val mood: Mood = Mood.NEUTRAL,
    val triggers: List<String> = emptyList(), 
    val createdAt: Long = System.currentTimeMillis()
)
