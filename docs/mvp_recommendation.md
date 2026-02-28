# MVP recommendation & feature ideas

## Is quotes + habits together a good idea for MVP?

**Recommendation: For MVP, focus on habits first.**

- **Habits-only MVP** keeps scope clear: one core loop (track habits, streaks, widget). You can validate retention and engagement without splitting attention.
- **Adding quotes later, linked to habits**, is a strong idea: e.g. “Daily reflection” after completing a habit, or a short quote when the user opens the app based on their streak. That keeps the product coherent and gives a reason to open the app daily (habit + reflection).
- Doing **quotes and habits in parallel from day one** spreads effort across two value props and two data models; for MVP it’s simpler to ship habits first, then add quotes as a layer on top.

So: **MVP = habits (tracking, streaks, widget, maybe simple analytics). Phase 2 = add quotes in relation to habits (e.g. reflection, encouragement).**

---

## More features to consider

### Home screen
- **Today’s focus** – 1–3 habits to focus on today (e.g. “Complete these first”).
- **Quick actions** – “Increment all” (done), “Start morning routine” that opens a checklist.
- **Streak milestones** – e.g. “3 more days to reach 7-day streak!” with a small progress indicator.
- **Weekly summary card** – “This week you completed X/Y habits” with a simple bar or list.
- **Empty state** – When no habits: CTA to create first habit + optional “Discover habits” suggestions.
- **Greeting by time** – “Good morning” / “Good evening” based on time of day (with string resources).

### Habits screen
- **Habit templates** – e.g. “Meditation”, “Read 10 min”, “Exercise” to create from.
- **Reminders** – Optional time per habit (notifications).
- **Edit habit** – Change name or reset streak.
- **Archive habit** – Hide without deleting (keep history).
- **Habit notes** – Optional short note when incrementing (e.g. “Felt good”).
- **Sort/filter** – By streak, name, or “needs attention” (missed yesterday).

---

## Implementation note

For this codebase, quote-related UI has been removed from the home and main navigation for the habits-focused MVP. Quote domain/data can be re-used later when adding “quotes in relation to habits”.
