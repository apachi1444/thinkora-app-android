package com.apachi.auraskin.domain.repository

import com.apachi.auraskin.domain.model.DailyStreak
import com.apachi.auraskin.domain.model.Quote
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
    fun searchQuotes(query: String): Flow<List<Quote>>
}

