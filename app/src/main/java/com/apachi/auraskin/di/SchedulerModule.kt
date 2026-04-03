package com.apachi.auraskin.di

import com.apachi.auraskin.domain.scheduler.HabitReminderScheduler
import com.apachi.auraskin.scheduler.HabitReminderSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SchedulerModule {

    @Binds
    @Singleton
    abstract fun bindHabitReminderScheduler(
        impl: HabitReminderSchedulerImpl
    ): HabitReminderScheduler
}
