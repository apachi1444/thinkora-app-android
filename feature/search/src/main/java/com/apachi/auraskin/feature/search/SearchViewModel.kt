package com.apachi.auraskin.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apachi.auraskin.domain.model.Habit
import com.apachi.auraskin.domain.model.Quote
import com.apachi.auraskin.domain.use_case.SearchHabitsUseCase
import com.apachi.auraskin.domain.use_case.SearchQuotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchQuotesUseCase: SearchQuotesUseCase,
    private val searchHabitsUseCase: SearchHabitsUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _selectedTab = MutableStateFlow(SearchTab.ALL)
    val selectedTab = _selectedTab.asStateFlow()

    val searchResults: StateFlow<SearchState> = _query
        .debounce(300)
        .combine(_selectedTab) { q, tab -> q to tab }
        .flatMapLatest { (q, tab) ->
            if (q.isBlank()) {
                flowOf(SearchState())
            } else {
                combine(
                    searchQuotesUseCase(q),
                    searchHabitsUseCase(q)
                ) { quotes, habits ->
                    SearchState(
                        quotes = if (tab == SearchTab.ALL || tab == SearchTab.QUOTES) quotes else emptyList(),
                        habits = if (tab == SearchTab.ALL || tab == SearchTab.HABITS) habits else emptyList(),
                        isLoading = false
                    )
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SearchState(isLoading = false)
        )

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    fun onTabSelect(tab: SearchTab) {
        _selectedTab.value = tab
    }
}

data class SearchState(
    val quotes: List<Quote> = emptyList(),
    val habits: List<Habit> = emptyList(),
    val isLoading: Boolean = true
)

enum class SearchTab {
    ALL, QUOTES, HABITS
}
