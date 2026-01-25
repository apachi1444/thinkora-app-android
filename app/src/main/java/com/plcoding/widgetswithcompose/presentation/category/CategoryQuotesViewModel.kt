package com.plcoding.widgetswithcompose.presentation.category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.widgetswithcompose.domain.model.Quote
import com.plcoding.widgetswithcompose.domain.use_case.GetQuotesByCategoryUseCase
import com.plcoding.widgetswithcompose.domain.use_case.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryQuotesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getQuotesByCategoryUseCase: GetQuotesByCategoryUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CategoryQuotesState())
    val state = _state.asStateFlow()
    
    val categoryName: String = checkNotNull(savedStateHandle["categoryName"])

    init {
        loadQuotes(categoryName)
    }

    private fun loadQuotes(category: String) {
        // Use the useCase 
        getQuotesByCategoryUseCase(category).onEach { quotes ->
            _state.value = _state.value.copy(quotes = quotes, categoryName = category)
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: CategoryQuotesEvent) {
        when(event) {
            is CategoryQuotesEvent.ToggleFavorite -> {
                viewModelScope.launch {
                    toggleFavoriteUseCase(event.quote.id)
                }
            }
        }
    }
}

data class CategoryQuotesState(
    val quotes: List<Quote> = emptyList(),
    val categoryName: String = ""
)

sealed class CategoryQuotesEvent {
    data class ToggleFavorite(val quote: Quote): CategoryQuotesEvent()
}
