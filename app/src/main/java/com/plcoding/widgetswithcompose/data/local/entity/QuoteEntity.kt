package com.plcoding.widgetswithcompose.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.plcoding.widgetswithcompose.domain.model.Quote

@Entity(tableName = "quotes")
data class QuoteEntity(
    @PrimaryKey val id: String,
    val content: String,
    val author: String,
    val category: String,
    val isFavorite: Boolean
) {
    fun toDomain(isRead: Boolean): Quote {
        return Quote(
            id = id,
            content = content,
            author = author,
            category = category,
            isFavorite = isFavorite,
            isRead = isRead
        )
    }
}
