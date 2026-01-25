package com.plcoding.widgetswithcompose.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.plcoding.widgetswithcompose.data.local.dao.QuoteDao
import com.plcoding.widgetswithcompose.data.local.dao.ReadHistoryDao
import com.plcoding.widgetswithcompose.data.local.entity.QuoteEntity
import com.plcoding.widgetswithcompose.data.local.entity.ReadHistoryEntity
import com.plcoding.widgetswithcompose.data.preferences.dataStore
import com.plcoding.widgetswithcompose.domain.model.DailyStreak
import com.plcoding.widgetswithcompose.domain.model.Quote
import com.plcoding.widgetswithcompose.domain.repository.QuoteRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

class QuoteRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val quoteDao: QuoteDao,
    private val readHistoryDao: ReadHistoryDao
) : QuoteRepository {

    private val TODAY_DATE = longPreferencesKey("today_date")
    private val TODAY_QUOTE_ID = stringPreferencesKey("today_quote_id")

    override fun getDailyQuote(interests: List<String>): Flow<Quote> = flow {
        val today = getTodayEpochDay()
        val prefs = context.dataStore.data.first()
        val lastDate = prefs[TODAY_DATE] ?: 0L
        var quoteId = prefs[TODAY_QUOTE_ID]

        if (lastDate != today || quoteId == null) {
            // New Day or First Run
            quoteId = pickNewDailyQuote(interests)
            context.dataStore.edit {
                it[TODAY_DATE] = today
                it[TODAY_QUOTE_ID] = quoteId
            }
        }

        // Emit from DB observing changes
        // Combine Quote and ReadHistory to map to Domain Quote
        emitAll(getQuoteDomainWithReadStatus(quoteId, today))
    }

    private fun getQuoteDomainWithReadStatus(quoteId: String, today: Long): Flow<Quote> {
        return combine(
            quoteDao.getAllQuotes(), // Optimally we should observe specific ID, but DAO lacks Flow<Quote?> by Id. 
            // Better: DAO getQuoteById(id): Flow<QuoteEntity>. I'll assume I update DAO or filter here. 
            // Wait, for efficiency I should add getQuoteById to DAO.
            // For now, I'll filter list.
            readHistoryDao.getAllHistory()
        ) { quotes, history ->
            val entity = quotes.find { it.id == quoteId } 
                ?: seedAndGetFallback(quoteId) // Safety net
            val isRead = history.any { it.quoteId == quoteId && it.date == today }
            entity.toDomain(isRead)
        }
    }

    private suspend fun pickNewDailyQuote(interests: List<String>): String {
        // Ensure we have quotes
        val existingCount = quoteDao.getAllQuotes().first().size
        if (existingCount == 0) {
            seedQuotes()
        }

        // Fetch candidates
        val candidates = quoteDao.getQuotesByCategories(interests).first().ifEmpty {
            quoteDao.getAllQuotes().first() // Fallback to all if no match
        }

        if (candidates.isEmpty()) return "" // Should not happen after seed

        return candidates.random().id
    }

    private suspend fun seedAndGetFallback(id: String): QuoteEntity {
        // If the ID stored in prefs is gone (db cleared?), re-seed
        seedQuotes()
        return quoteDao.getAllQuotes().first().find { it.id == id } 
            ?: quoteDao.getAllQuotes().first().first() // Absolute fallback
    }

    private suspend fun seedQuotes() {
        val seed = listOf(
            QuoteEntity(UUID.randomUUID().toString(),
                "The best way to get started is to quit talking and begin doing.",
                "Walt Disney",
                "Business",
                isFavorite = false,
                isRead = false
            ),
            QuoteEntity(UUID.randomUUID().toString(),
                "The best way to get started is to quit talking and begin doing.",
                "Walt Disney",
                "Business",
                isFavorite = false,
                isRead = false
            ),
            QuoteEntity(UUID.randomUUID().toString(),
                "The best way to get started is to quit talking and begin doing.",
                "Walt Disney",
                "Business",
                isFavorite = false,
                isRead = false
            ),
            QuoteEntity(UUID.randomUUID().toString(),
                "The best way to get started is to quit talking and begin doing.",
                "Walt Disney",
                "Business",
                isFavorite = false,
                isRead = false
            ),
            QuoteEntity(UUID.randomUUID().toString(),
                "The best way to get started is to quit talking and begin doing.",
                "Walt Disney",
                "Business",
                isFavorite = false,
                isRead = false
            )
        )
        seed.forEach { quoteDao.insertQuote(it) }
    }

    override fun getFavoriteQuotes(): Flow<List<Quote>> {
        return quoteDao.getFavorites().map { entities ->
            entities.map { it.toDomain(isRead = false) } // Read status irrelevant for favorites list usually
        }
    }

    override suspend fun toggleFavorite(quoteId: String) {
        quoteDao.toggleFavorite(quoteId)
    }

    override suspend fun markQuoteAsRead(quoteId: String) {
        val today = getTodayEpochDay()
        readHistoryDao.insert(ReadHistoryEntity(today, quoteId))
        
        // No need to update 'isRead' on QuoteEntity, it's computed dynamically
    }

    override fun getDailyStreak(): Flow<DailyStreak> {
        return readHistoryDao.getAllHistory().map { history ->
            calculateStreak(history)
        }
    }

    private fun calculateStreak(history: List<ReadHistoryEntity>): DailyStreak {
        if (history.isEmpty()) return DailyStreak(0, 0)
        
        val sortedDates = history.map { it.date }.distinct().sortedDescending()
        var streak = 0
        var expectedDate = getTodayEpochDay()
        
        // Check if read today
        if (sortedDates.contains(expectedDate)) {
            streak++
            expectedDate-- // Check yesterday
        } else {
            // Not read today yet. Check if read yesterday.
            expectedDate-- 
            if (!sortedDates.contains(expectedDate)) {
                 // Streak broken (neither today nor yesterday)
                 return DailyStreak(0, sortedDates.firstOrNull() ?: 0)
            }
        }

        // Verify sequence
        for (date in sortedDates) {
             if (date == expectedDate + 1) continue // Already counted (Today)
             if (date == expectedDate) {
                 streak++
                 expectedDate--
             } else {
                 break
             }
        }
        
        return DailyStreak(streak, sortedDates.first())
    }

    private fun getTodayEpochDay(): Long {
        val calendar = Calendar.getInstance()
        // Clear time components for pure date comparison
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis / (1000 * 60 * 60 * 24)
    }
}
