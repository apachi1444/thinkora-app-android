package com.apachi.thinkora.di

import android.content.Context
import androidx.room.Room
import com.apachi.thinkora.data.local.QuoteDatabase
import com.apachi.thinkora.data.local.dao.QuoteDao
import com.apachi.thinkora.data.local.dao.ReadHistoryDao
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
        ).fallbackToDestructiveMigration()
         .build()
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

    @Provides
    @Singleton
    fun provideHabitDao(db: QuoteDatabase): com.apachi.thinkora.data.local.dao.HabitDao {
        return db.habitDao
    }
}
