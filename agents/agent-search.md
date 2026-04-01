# Agent: Search Feature Specialist

## Role
You own the **unified search system** — full-text search across quotes and habits, the SearchScreen UI, and real-time query handling.

---

## Current Implementation

### Files
| File | Layer |
|---|---|
| `SearchScreen.kt` | UI (`feature:search`) |

> Note: `SearchViewModel` does not yet exist — create it when implementing search logic.

---

## SearchScreen Design
- Single top search bar (persistent)
- Result tabs: **Quotes** | **Habits** | **All**
- Real-time filtering as user types (debounce 300ms)
- Empty state: "No results for '{query}'"
- Recent searches stored in DataStore (max 5 entries)

---

## Implementation Plan

### SearchViewModel (to be created)
```kotlin
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchQuotesUseCase: SearchQuotesUseCase,
    private val searchHabitsUseCase: SearchHabitsUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    val results: StateFlow<SearchResults> = _query
        .debounce(300)
        .flatMapLatest { q ->
            combine(
                searchQuotesUseCase(q),
                searchHabitsUseCase(q)
            ) { quotes, habits -> SearchResults(quotes, habits) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SearchResults())
}
```

### Use Cases (to be created in `core:domain`)
```kotlin
class SearchQuotesUseCase @Inject constructor(private val repo: QuoteRepository) {
    operator fun invoke(query: String): Flow<List<Quote>> =
        repo.searchQuotes(query)
}

class SearchHabitsUseCase @Inject constructor(private val repo: HabitRepository) {
    operator fun invoke(query: String): Flow<List<Habit>> =
        repo.searchHabits(query)
}
```

### Room FTS (Full-Text Search)
```kotlin
// In QuoteDao
@Query("SELECT * FROM quotes WHERE content LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%'")
fun searchQuotes(query: String): Flow<List<QuoteEntity>>

// In HabitDao
@Query("SELECT * FROM habits WHERE name LIKE '%' || :query || '%'")
fun searchHabits(query: String): Flow<List<HabitEntity>>
```

---

## Rules
- Search results must update reactively — no "Search" button, instant results
- Debounce query with 300ms delay to avoid excessive DB hits
- Empty query state shows recent searches, not empty results
- Search logic lives in ViewModel + use cases, never in composable directly
