package com.apachi.thinkora.domain.repository

import com.apachi.thinkora.domain.model.DailyStreak
import com.apachi.thinkora.domain.model.Quote
import kotlinx.coroutines.flow.Flow

interface QuoteRepository {
    fun getDailyQuote(interests: List<String>): Flow<Quote>
    fun getFavoriteQuotes(): Flow<List<Quote>>
    suspend fun toggleFavorite(quoteId: String)
    suspend fun markQuoteAsRead(quoteId: String)
    fun getDailyStreak(): Flow<DailyStreak>
    fun getCustomQuotes(): Flow<List<Quote>>
    suspend fun addQuote(content: String, author: String, category: String)
    suspend fun deleteQuote(quoteId: String)
    suspend fun deleteAllCustomQuotes()
    fun getQuotesByCategory(category: String): Flow<List<Quote>>
}

