package com.plcoding.widgetswithcompose.domain.model

data class Habit(
    val id: String,
    val name: String,
    val streak: Int,
    val createdTimestamp: Long
)
