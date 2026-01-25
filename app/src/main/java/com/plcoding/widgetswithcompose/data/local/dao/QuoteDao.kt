package com.plcoding.widgetswithcompose.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.plcoding.widgetswithcompose.data.local.entity.QuoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {
    @Query("SELECT * FROM quotes WHERE category IN (:categories)")
    fun getQuotesByCategories(categories: List<String>): Flow<List<QuoteEntity>>

    @Query("SELECT * FROM quotes WHERE isFavorite = 1")
    fun getFavorites(): Flow<List<QuoteEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuote(quote: QuoteEntity)

    @Query("UPDATE quotes SET isFavorite = NOT isFavorite WHERE id = :quoteId")
    suspend fun toggleFavorite(quoteId: String)


    @Query("SELECT * FROM quotes")
    fun getAllQuotes(): Flow<List<QuoteEntity>>

}
