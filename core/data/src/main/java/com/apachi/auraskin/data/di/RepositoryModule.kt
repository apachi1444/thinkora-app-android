package com.apachi.auraskin.data.di

import com.apachi.auraskin.data.preferences.UserPreferencesRepositoryImpl
import com.apachi.auraskin.data.repository.QuoteRepositoryImpl
import com.apachi.auraskin.domain.repository.QuoteRepository
import com.apachi.auraskin.domain.repository.UserRepository
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
        habitRepositoryImpl: com.apachi.auraskin.data.repository.HabitRepositoryImpl
    ): com.apachi.auraskin.domain.repository.HabitRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        notificationRepositoryImpl: com.apachi.auraskin.data.repository.NotificationRepositoryImpl
    ): com.apachi.auraskin.domain.repository.NotificationRepository

    @Binds
    @Singleton
    abstract fun bindGamificationRepository(
        gamificationRepositoryImpl: com.apachi.auraskin.data.repository.GamificationRepositoryImpl
    ): com.apachi.auraskin.domain.repository.GamificationRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: com.apachi.auraskin.core.data.repository.SettingsRepositoryImpl
    ): com.apachi.auraskin.core.domain.repository.SettingsRepository
}
