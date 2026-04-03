package com.apachi.auraskin.domain.scheduler

/**
 * Schedules or cancels the daily habit reminder notification.
 * Implementation (e.g. WorkManager) lives in the app module.
 */
interface HabitReminderScheduler {
    fun schedule(enabled: Boolean, hour: Int, minute: Int)
}
