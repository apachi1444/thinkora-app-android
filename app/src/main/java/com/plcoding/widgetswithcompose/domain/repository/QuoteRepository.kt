package com.plcoding.widgetswithcompose.domain.repository

import com.plcoding.widgetswithcompose.domain.model.DailyStreak
import com.plcoding.widgetswithcompose.domain.model.Quote
import kotlinx.coroutines.flow.Flow

interface QuoteRepository {
    fun getDailyQuote(interests: List<String>): Flow<Quote>
    fun getFavoriteQuotes(): Flow<List<Quote>>
    suspend fun toggleFavorite(quoteId: String)
    suspend fun markQuoteAsRead(quoteId: String)
    fun getDailyStreak(): Flow<DailyStreak>
}
