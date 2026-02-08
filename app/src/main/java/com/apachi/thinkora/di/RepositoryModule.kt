package com.apachi.thinkora.di

import com.apachi.thinkora.data.preferences.UserPreferencesRepositoryImpl
import com.apachi.thinkora.data.repository.QuoteRepositoryImpl
import com.apachi.thinkora.domain.repository.QuoteRepository
import com.apachi.thinkora.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindQuoteRepository(
        quoteRepositoryImpl: QuoteRepositoryImpl
    ): QuoteRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userPreferencesRepositoryImpl: UserPreferencesRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindHabitRepository(
        habitRepositoryImpl: com.apachi.thinkora.data.repository.HabitRepositoryImpl
    ): com.apachi.thinkora.domain.repository.HabitRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        notificationRepositoryImpl: com.apachi.thinkora.data.repository.NotificationRepositoryImpl
    ): com.apachi.thinkora.domain.repository.NotificationRepository

    @Binds
    @Singleton
    abstract fun bindGamificationRepository(
        gamificationRepositoryImpl: com.apachi.thinkora.data.repository.GamificationRepositoryImpl
    ): com.apachi.thinkora.domain.repository.GamificationRepository
}
