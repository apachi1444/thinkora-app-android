# Agent: Quotes & Mindset Feature Specialist

## Role
You own the **quotes and mindset system** in the AuraSkin app. The goal is no longer just general inspiration, but specifically self-love, skin-positivity, stress relief, and fostering patience for skin healing. 

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
    val category: String, // e.g. "SKIN_POSITIVITY", "STRESS_RELIEF", "PATIENCE"
    val isFavorite: Boolean = false,
    val isCustom: Boolean = false,       // user-created quotes
    val isRead: Boolean = false
)
```

---

## Feature State ✅
- [x] Seeded skin-positivity and self-care quotes in Room DB
- [x] Category browsing (`feature:category`)
- [x] Favorites toggle (`ToggleFavoriteUseCase`)
- [x] Custom quotes: add (author, content, category) + delete
- [x] `FavoriteQuotesWidget` (Glance) on home screen
- [x] Firebase event `custom_quote_added` logged on creation

## Roadmap
- [ ] **Mood-Linked Quotes** — Fetch a specific category (like "STRESS_RELIEF") based on the daily `SkinLog` mood rating if it is "BAD" or "TERRIBLE".
- [ ] **Social Sharing** — share as image → see `agent-social-sharing.md`
- [ ] **Full-text search** — unified with `agent-search.md`

---

## Rules
- Custom quotes must have `isCustom = true` set before insert.
- Quote categories must be aligned with skin wellness themes.
- Favorites toggle must update DB via `ToggleFavoriteUseCase`, never directly.
