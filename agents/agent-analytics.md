# Agent: Analytics Feature Specialist

## Role
You own the **analytics and charts** system — tracking habit completion, skin log scores, and specifically building the correlation between routine consistency and physical skin improvements.

---

## Current Implementation

### Files
| File | Layer |
|---|---|
| `HabitCompletionEntity` | Room Entity (`core:data`) |
| `SkinLogEntity` | Room Entity (`core:data`) |
| `AnalyticsScreen.kt` | UI (`feature:analytics`) |
| `AnalyticsViewModel.kt` | ViewModel (`feature:analytics`) |

---

## Roadmap
- [ ] **Correlation Charts (Vico)** — Display a dual-axis chart. Bar chart for Weekly Habit Completions overlaid with a Line chart of the Skin Condition Score (1-5).
- [ ] **Monthly Heatmap View** — A calendar heatmap showing color-coded skin scores per day.
- [ ] **Success Rate** — "You completed X% of your skincare routines this week."
- [ ] **Export CSV** — Share raw data with dermatologists → coordinate with `agent-settings.md`.

---

## Vico Implementation Idea
```kotlin
// AnalyticsScreen.kt
Chart(
    chart = columnChart() + lineChart(), // Overlaying columns (habits) + line (skin score)
    model = composedChartModel,
    startAxis = rememberStartAxis(),
    bottomAxis = rememberBottomAxis()
)
```

---

## Rules
- Never compute correlations in the UI composable — all logic in `AnalyticsViewModel`.
- Chart data must be expressed as `StateFlow<List<ChartDataPoint>>`.
- Ensure querying of both Skin Logs and Habit Completions occurs via Domain UseCases.
