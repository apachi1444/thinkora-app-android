package com.plcoding.widgetswithcompose

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.plcoding.widgetswithcompose.domain.repository.QuoteRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteQuotesWidget : GlanceAppWidget() {
    
    // In a real app we would pass data via state or parameters. 
    // Ideally use GlanceStateDefinition.
    // For simplicity, I'll allow the Content() to fetch from Repo via EntryPoint?
    // Glance architecture usually favors "Receiver" pushing state to "Widget".
    // I'll define state using Preferences? 
    // Or just simple "Wait for update".
    // I can't easily inject into the Widget class itself unless I use specific Hilt integration for Glance (which is newer).
    
    // Simplest Clean Architecture way:
    // Receiver (@AndroidEntryPoint) -> fetches data -> updateAppWidgetState()

    @Composable
    fun MyContent() {
         // Placeholder. Real implementation depends on state.
         // Since I can't easily inject here without more setup, I will keep it basic.
         // Assuming state contains "content" and "author".
         
         val content = androidx.glance.currentState(androidx.datastore.preferences.core.stringPreferencesKey("content")) ?: "No favorites yet!"
         val author = androidx.glance.currentState(androidx.datastore.preferences.core.stringPreferencesKey("author")) ?: ""

         Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(Color.DarkGray)
                .padding(16.dp),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            Text(
                text = content,
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    color = ColorProvider(Color.White),
                    fontSize = 18.sp
                )
            )
            if (author.isNotEmpty()) {
                 Text(
                    text = "- $author",
                    style = TextStyle(
                        fontStyle = androidx.glance.text.FontStyle.Italic,
                        color = ColorProvider(Color.LightGray),
                        fontSize = 14.sp
                    )
                )
            }
        }
    }

    @Composable
    override fun Content() {
        MyContent()
    }
}

@AndroidEntryPoint
class FavoriteQuotesWidgetReceiver : GlanceAppWidgetReceiver() {
    
    override val glanceAppWidget: GlanceAppWidget = FavoriteQuotesWidget()
    
    @Inject
    lateinit var quoteRepository: QuoteRepository

    override fun onUpdate(context: Context, appWidgetManager: android.appwidget.AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        updateQuotes(context)
    }
    
    private fun updateQuotes(context: Context) {
        // Launch a coroutine to fetch and update
        // Since onUpdate is not suspend, use runBlocking or Scope.
        // GoAsync is handled by GlanceAppWidgetReceiver?
        // Actually GlanceAppWidgetReceiver.onUpdate is non-suspend in some versions? 
        // In alpha05 it might be.
        // Safest: Use CoroutineScope(Dispatchers.IO).launch
        
        CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
             val favorites = quoteRepository.getFavoriteQuotes().first()
             if (favorites.isNotEmpty()) {
                 val randomQuote = favorites.random()
                 
                 androidx.glance.appwidget.GlanceAppWidgetManager(context).getGlanceIds(FavoriteQuotesWidget::class.java).forEach { glanceId ->
                     androidx.glance.appwidget.state.updateAppWidgetState(context, glanceId) { prefs ->
                         prefs[androidx.datastore.preferences.core.stringPreferencesKey("content")] = randomQuote.content
                         prefs[androidx.datastore.preferences.core.stringPreferencesKey("author")] = randomQuote.author
                     }
                     FavoriteQuotesWidget().update(context, glanceId)
                 }
             }
        }
    }
}
