package com.plcoding.widgetswithcompose.di

import android.content.Context
import androidx.room.Room
import com.plcoding.widgetswithcompose.data.local.QuoteDatabase
import com.plcoding.widgetswithcompose.data.local.dao.QuoteDao
import com.plcoding.widgetswithcompose.data.local.dao.ReadHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideQuoteDatabase(@ApplicationContext context: Context): QuoteDatabase {
        return Room.databaseBuilder(
            context,
            QuoteDatabase::class.java,
            "quote_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideQuoteDao(db: QuoteDatabase): QuoteDao {
        return db.quoteDao
    }

    @Provides
    @Singleton
    fun provideReadHistoryDao(db: QuoteDatabase): ReadHistoryDao {
        return db.readHistoryDao
    }
}
