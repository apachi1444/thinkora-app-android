package com.apachi.thinkora.feature.home.widget

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
import com.apachi.thinkora.domain.repository.QuoteRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

import androidx.glance.LocalContext
import androidx.glance.appwidget.GlanceTheme
import com.apachi.thinkora.R

class FavoriteQuotesWidget : GlanceAppWidget() {
    
    @Composable
    fun MyContent() {
         val context = LocalContext.current
         val content = androidx.glance.currentState(androidx.datastore.preferences.core.stringPreferencesKey("content")) ?: context.getString(R.string.widget_no_favorites)
         val author = androidx.glance.currentState(androidx.datastore.preferences.core.stringPreferencesKey("author")) ?: ""

         Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.surface)
                .padding(16.dp),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            Text(
                text = content,
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    color = GlanceTheme.colors.onSurface,
                    fontSize = 18.sp
                )
            )
            if (author.isNotEmpty()) {
                 Text(
                    text = "- $author",
                    style = TextStyle(
                        fontStyle = androidx.glance.text.FontStyle.Italic,
                        color = GlanceTheme.colors.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                )
            }
        }
    }

    @Composable
    override fun Content() {
        GlanceTheme {
            MyContent()
        }
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
