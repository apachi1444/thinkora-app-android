package com.apachi.thinkora.feature.quotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apachi.thinkora.domain.model.Quote
import com.apachi.thinkora.domain.repository.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.apachi.thinkora.core.ads.AdManager
import android.app.Activity

@HiltViewModel
class CustomQuotesViewModel @Inject constructor(
    private val repository: QuoteRepository,
    private val adManager: AdManager
) : ViewModel() {

    val customQuotes: StateFlow<List<Quote>> = repository.getCustomQuotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addQuote(content: String, author: String, category: String, activity: Activity? = null, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.addQuote(content, author, category)
            if (activity != null) {
                adManager.showInterstitial(activity) {
                    onComplete()
                }
            } else {
                onComplete()
            }
        }
    }

    fun deleteQuote(quoteId: String) {
        viewModelScope.launch {
            repository.deleteQuote(quoteId)
        }
    }
}
