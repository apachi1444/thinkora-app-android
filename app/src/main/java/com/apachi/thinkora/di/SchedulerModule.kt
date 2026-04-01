package com.apachi.thinkora.di

import com.apachi.thinkora.domain.scheduler.HabitReminderScheduler
import com.apachi.thinkora.scheduler.HabitReminderSchedulerImpl
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
