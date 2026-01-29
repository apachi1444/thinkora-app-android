package com.apachi.thinkora

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.apachi.thinkora.data.local.QuoteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object HabitsWidget : GlanceAppWidget() {

    val currentIndexKey = intPreferencesKey("current_habit_index")
    val habitNameKey = stringPreferencesKey("habit_name")
    val habitStreakKey = intPreferencesKey("habit_streak")
    val habitsCountKey = intPreferencesKey("habits_count")


    @Composable
    fun WidgetContent() {
        val currentIndex = currentState(key = currentIndexKey) ?: 0
        val habitName = currentState(key = habitNameKey) ?: "No habits"
        val habitStreak = currentState(key = habitStreakKey) ?: 0
        val habitsCount = currentState(key = habitsCountKey) ?: 0
        
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            Text(
                text = "Your Habits",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = ColorProvider(Color.Black),
                    fontSize = 18.sp
                )
            )
            
            if (habitsCount > 1) {
                Spacer(modifier = GlanceModifier.height(4.dp))
                Text(
                    text = "${currentIndex + 1} / $habitsCount",
                    style = TextStyle(
                        color = ColorProvider(Color.Gray),
                        fontSize = 12.sp
                    )
                )
            }
            
            Spacer(modifier = GlanceModifier.height(16.dp))
            
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
                verticalAlignment = Alignment.Vertical.CenterVertically
            ) {
                if (habitsCount > 1) {
                    Button(
                        text = "<",
                        onClick = actionRunCallback(NavigatePrevCallback::class.java)
                    )
                    Spacer(modifier = GlanceModifier.width(12.dp))
                }
                
                Column(
                    modifier = GlanceModifier.defaultWeight(),
                    horizontalAlignment = Alignment.Horizontal.CenterHorizontally
                ) {
                    Text(
                        text = habitName,
                        style = TextStyle(
                            fontWeight = FontWeight.Medium,
                            color = ColorProvider(Color.Black),
                            fontSize = 16.sp
                        )
                    )
                    Spacer(modifier = GlanceModifier.height(8.dp))
                    Text(
                        text = "🔥 $habitStreak days",
                        style = TextStyle(
                            color = ColorProvider(Color(0xFFF97316)),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                
                if (habitsCount > 1) {
                    Spacer(modifier = GlanceModifier.width(12.dp))
                    Button(
                        text = ">",
                        onClick = actionRunCallback(NavigateNextCallback::class.java)
                    )
                }
            }
            
            Spacer(modifier = GlanceModifier.height(16.dp))
            
            if (habitsCount > 0) {
                Button(
                    text = "+ Increment",
                    onClick = actionRunCallback(IncrementHabitCallback::class.java)
                )
            }
        }
    }

    @Composable
    override fun Content() {
        WidgetContent()
    }
}

class HabitsWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = HabitsWidget
        
    override fun onUpdate(
        context: Context,
        appWidgetManager: android.appwidget.AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        updateHabitsData(context)
    }
    
    private fun updateHabitsData(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = getDatabase(context)
            val habits = db.habitDao.getAllHabits().first()
            
            androidx.glance.appwidget.GlanceAppWidgetManager(context)
                .getGlanceIds(HabitsWidget::class.java)
                .forEach { glanceId ->
                    updateAppWidgetState(context, glanceId) { prefs ->
                        val currentIndex = prefs[HabitsWidget.currentIndexKey] ?: 0
                        prefs[HabitsWidget.habitsCountKey] = habits.size
                        
                        if (habits.isNotEmpty()) {
                            val safeIndex = currentIndex.coerceIn(0, habits.size - 1)
                            val currentHabit = habits[safeIndex]
                            prefs[HabitsWidget.habitNameKey] = currentHabit.name
                            prefs[HabitsWidget.habitStreakKey] = currentHabit.streak
                            prefs[HabitsWidget.currentIndexKey] = safeIndex
                        } else {
                            prefs[HabitsWidget.habitNameKey] = "No habits yet"
                            prefs[HabitsWidget.habitStreakKey] = 0
                        }
                    }
                    HabitsWidget.update(context, glanceId)
                }
        }
    }
}

class NavigatePrevCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val db = getDatabase(context)
        val habits = withContext(Dispatchers.IO) {
            db.habitDao.getAllHabits().first()
        }
        
        updateAppWidgetState(context, glanceId) { prefs ->
            val currentIndex = prefs[HabitsWidget.currentIndexKey] ?: 0
            val habitsCount = habits.size
            
            if (habitsCount > 0) {
                val newIndex = if (currentIndex > 0) currentIndex - 1 else habitsCount - 1
                prefs[HabitsWidget.currentIndexKey] = newIndex
                
                // Update displayed habit
                val currentHabit = habits[newIndex]
                prefs[HabitsWidget.habitNameKey] = currentHabit.name
                prefs[HabitsWidget.habitStreakKey] = currentHabit.streak
            }
        }
        HabitsWidget.update(context, glanceId)
    }
}

class NavigateNextCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val db = getDatabase(context)
        val habits = withContext(Dispatchers.IO) {
            db.habitDao.getAllHabits().first()
        }
        
        updateAppWidgetState(context, glanceId) { prefs ->
            val currentIndex = prefs[HabitsWidget.currentIndexKey] ?: 0
            val habitsCount = habits.size
            
            if (habitsCount > 0) {
                val newIndex = (currentIndex + 1).rem(habitsCount)
                prefs[HabitsWidget.currentIndexKey] = newIndex
                
                // Update displayed habit
                val currentHabit = habits[newIndex]
                prefs[HabitsWidget.habitNameKey] = currentHabit.name
                prefs[HabitsWidget.habitStreakKey] = currentHabit.streak
            }
        }
        HabitsWidget.update(context, glanceId)
    }
}

class IncrementHabitCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val db = getDatabase(context)
        val habits = withContext(Dispatchers.IO) {
            db.habitDao.getAllHabits().first()
        }
        
        updateAppWidgetState(context, glanceId) { prefs ->
            val currentIndex = prefs[HabitsWidget.currentIndexKey] ?: 0
            
            if (habits.isNotEmpty() && currentIndex < habits.size) {
                val currentHabit = habits[currentIndex]
                withContext(Dispatchers.IO) {
                    db.habitDao.incrementStreak(currentHabit.id)
                }
                prefs[HabitsWidget.habitStreakKey] = currentHabit.streak + 1
            }
        }
        HabitsWidget.update(context, glanceId)
    }
}

// Helper function to get database instance
private fun getDatabase(context: Context): QuoteDatabase {
    return androidx.room.Room.databaseBuilder(
        context.applicationContext,
        QuoteDatabase::class.java,
        "quote_db"
    ).fallbackToDestructiveMigration().build()
}

