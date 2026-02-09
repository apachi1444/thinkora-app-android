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

@HiltViewModel
class CustomQuotesViewModel @Inject constructor(
    private val repository: QuoteRepository
) : ViewModel() {

    val customQuotes: StateFlow<List<Quote>> = repository.getCustomQuotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addQuote(content: String, author: String, category: String) {
        viewModelScope.launch {
            repository.addQuote(content, author, category)
        }
    }

    fun deleteQuote(quoteId: String) {
        viewModelScope.launch {
            repository.deleteQuote(quoteId)
        }
    }
}
