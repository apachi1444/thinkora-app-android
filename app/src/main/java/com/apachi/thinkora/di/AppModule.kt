package com.apachi.thinkora.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.apachi.thinkora.data.local.QuoteDatabase
import com.apachi.thinkora.data.local.dao.QuoteDao
import com.apachi.thinkora.data.local.dao.ReadHistoryDao
import com.apachi.thinkora.data.local.dao.AchievementDao
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
         .addCallback(object : RoomDatabase.Callback() {
             override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                 super.onCreate(db)
                 // Pre-populate achievements
                 val achievements = listOf(
                     "('first_step', 'First Step', 'Create your first habit', 'ic_flag', 'HABIT_COUNT', 1, 0, NULL)",
                     "('streak_3', 'Getting Serious', 'Reach a 3-day streak', 'ic_fire', 'STREAK', 3, 0, NULL)",
                     "('streak_7', 'Habit Master', 'Reach a 7-day streak', 'ic_star', 'STREAK', 7, 0, NULL)",
                     "('streak_30', 'Unstoppable', 'Reach a 30-day streak', 'ic_trophy', 'STREAK', 30, 0, NULL)"
                 )
                 achievements.forEach {
                     db.execSQL("INSERT INTO achievements (id, title, description, iconName, type, threshold, isUnlocked, unlockedDate) VALUES $it")
                 }
             }
         })
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

    @Provides
    @Singleton
    fun provideNotificationDao(db: QuoteDatabase): com.apachi.thinkora.data.local.dao.NotificationDao {
        return db.notificationDao
    }

    @Provides
    @Singleton
    fun provideAchievementDao(db: QuoteDatabase): AchievementDao {
        return db.achievementDao
    }

    @Provides
    @Singleton
    fun provideAdManager(adMobManager: com.apachi.thinkora.data.ads.AdMobManager): com.apachi.thinkora.data.ads.AdManager {
        return adMobManager
    }
}
