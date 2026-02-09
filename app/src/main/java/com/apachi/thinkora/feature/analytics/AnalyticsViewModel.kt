package com.apachi.thinkora.feature.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apachi.thinkora.domain.model.Habit
import com.apachi.thinkora.domain.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.util.Calendar

data class HabitAnalytics(
    val habit: Habit,
    val last7DaysCompletions: List<Int> // Count per day for last 7 days (including today)
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    // Helper to get aggregated data
    val analyticsData: StateFlow<List<HabitAnalytics>> = repository.getAllHabits()
        .flatMapLatest { habits ->
             val flows = habits.map { habit ->
                 repository.getCompletions(habit.id).map { timestamps ->
                     HabitAnalytics(habit, computeLast7Days(timestamps))
                 }
             }
             combine(flows) { it.toList() }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private fun computeLast7Days(timestamps: List<Long>): List<Int> {
        val counts = MutableList(7) { 0 }
        val calendar = Calendar.getInstance()
        // Reset to midnight today
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        
        val todayStart = calendar.timeInMillis
        val msPerDay = 24 * 60 * 60 * 1000L

        timestamps.forEach { ts ->
            val diff = todayStart - ts
            // If ts is today (diff < 0 if ts > todayStart, but completions are in past usually)
            // Wait, if ts is TODAY: ts >= todayStart. So diff <= 0.
            if (ts >= todayStart) {
                counts[6]++ // Today is last index
            } else {
                val daysAgo = ((todayStart - ts) / msPerDay).toInt() + 1
                // daysAgo 1 = yesterday. 
                // Index = 6 - (daysAgo -1)? No.
                // 6 = Today (0 days ago)
                // 5 = Yesterday (1 day ago)
                // ...
                // 0 = 6 days ago
                if (daysAgo in 1..6) {
                    counts[6 - daysAgo]++
                }
            }
        }
        return counts
    }
}
