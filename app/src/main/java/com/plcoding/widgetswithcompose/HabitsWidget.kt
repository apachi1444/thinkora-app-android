package com.plcoding.widgetswithcompose

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.*
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.plcoding.widgetswithcompose.data.local.QuoteDatabase
import com.plcoding.widgetswithcompose.domain.model.Habit
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import javax.inject.Inject

object HabitsWidget : GlanceAppWidget() {

    val currentIndexKey = intPreferencesKey("current_habit_index")

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val currentIndex = currentState(key = currentIndexKey) ?: 0
        
        // Note: We can't directly inject or use Flow in Glance widgets
        // We'll need to fetch habits synchronously or use a workaround
        // For now, showing a placeholder structure
        
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
            
            Spacer(modifier = GlanceModifier.height(16.dp))
            
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
                verticalAlignment = Alignment.Vertical.CenterVertically
            ) {
                Button(
                    text = "<",
                    onClick = actionRunCallback(NavigatePrevCallback::class.java)
                )
                
                Spacer(modifier = GlanceModifier.width(16.dp))
                
                Column(
                    modifier = GlanceModifier.defaultWeight(),
                    horizontalAlignment = Alignment.Horizontal.CenterHorizontally
                ) {
                    Text(
                        text = "Habit Name",
                        style = TextStyle(
                            fontWeight = FontWeight.Medium,
                            color = ColorProvider(Color.Black),
                            fontSize = 16.sp
                        )
                    )
                    Spacer(modifier = GlanceModifier.height(8.dp))
                    Text(
                        text = "🔥 0 days",
                        style = TextStyle(
                            color = ColorProvider(Color(0xFFF97316)),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                
                Spacer(modifier = GlanceModifier.width(16.dp))
                
                Button(
                    text = ">",
                    onClick = actionRunCallback(NavigateNextCallback::class.java)
                )
            }
            
            Spacer(modifier = GlanceModifier.height(16.dp))
            
            Button(
                text = "+ Increment",
                onClick = actionRunCallback(IncrementHabitCallback::class.java)
            )
        }
    }
}

class HabitsWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = HabitsWidget
}

class NavigatePrevCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(context, glanceId) { prefs ->
            val currentIndex = prefs[HabitsWidget.currentIndexKey] ?: 0
            if (currentIndex > 0) {
                prefs[HabitsWidget.currentIndexKey] = currentIndex - 1
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
        updateAppWidgetState(context, glanceId) { prefs ->
            val currentIndex = prefs[HabitsWidget.currentIndexKey] ?: 0
            // TODO: Get actual habits count
            prefs[HabitsWidget.currentIndexKey] = currentIndex + 1
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
        // TODO: Get current habit ID and increment in database
        // For now, just update widget
        HabitsWidget.update(context, glanceId)
    }
}
