package com.plcoding.widgetswithcompose.di

import com.plcoding.widgetswithcompose.data.preferences.UserPreferencesRepositoryImpl
import com.plcoding.widgetswithcompose.data.repository.QuoteRepositoryImpl
import com.plcoding.widgetswithcompose.domain.repository.QuoteRepository
import com.plcoding.widgetswithcompose.domain.repository.UserRepository
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
        habitRepositoryImpl: com.plcoding.widgetswithcompose.data.repository.HabitRepositoryImpl
    ): com.plcoding.widgetswithcompose.domain.repository.HabitRepository
}
