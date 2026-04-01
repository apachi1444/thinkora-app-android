# Agent: Quotes Feature Specialist

## Role
You own the **quotes system** in the Thinkora app — the data model, categories, favorites, custom quotes (user-added), and the browsing UI.

---

## Current Implementation

### Files
| File | Layer |
|---|---|
| `Quote.kt` | Domain Model (`core:domain`) |
| `QuoteEntity` | Room Entity (`core:data`) |
| `QuoteDao` | Room DAO (`core:data`) |
| `QuoteRepository` | Interface (`core:domain`) |
| `QuoteRepositoryImpl` | Implementation (`core:data`) |
| `ToggleFavoriteUseCase` | Use Case (`core:domain`) |
| `MarkQuoteAsReadUseCase` | Use Case (`core:domain`) |

### Domain Model: `Quote`
```kotlin
data class Quote(
    val id: Long = 0,
    val content: String,
    val author: String,
    val category: String,
    val isFavorite: Boolean = false,
    val isCustom: Boolean = false,       // user-created quotes
    val isRead: Boolean = false
)
```

---

## Feature State ✅
- [x] Seeded quotes in Room DB
- [x] Category browsing (`feature:category`)
- [x] Favorites toggle (`ToggleFavoriteUseCase`)
- [x] Custom quotes: add (author, content, category) + delete
- [x] `FavoriteQuotesWidget` (Glance) on home screen
- [x] Firebase event `custom_quote_added` logged on creation

## Roadmap
- [ ] **Social Sharing** — share as image → see `agent-social-sharing.md`
- [ ] **Quote of the Day** — seeded daily from DB, shown on HomeScreen
- [ ] **Full-text search** — unified with `agent-search.md`

---

## Rules
- Custom quotes must have `isCustom = true` set before insert
- Favorites toggle must update DB via `ToggleFavoriteUseCase`, never directly
- Quote categories come from a predefined sealed class, not free text
- `QuoteRepositoryImpl` must fire `AnalyticsManager.logCustomQuoteAdded()` on custom add
