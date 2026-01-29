package com.apachi.thinkora.domain.model

data class Habit(
    val id: String,
    val name: String,
    val streak: Int,
    val createdTimestamp: Long
)
