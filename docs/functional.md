# Functional Documentation

## 🎨 Design System: AuraSkin Luminous Calm

- **Typography**: Manrope / Inter, sans-serif
- **Shapes**: Cards 24px, Buttons 16px, Avatars 50%
- **Modes**: Full Light + Dark mode support
- **Primary**: Sage Green `#76B599` (light) / `#81BBA1` (dark)
- **Accent**: Soft Rose `#EFA39E` (light) / Deep Rose `#5A2A28` (dark)

---

## ✨ Features

### 🔹 Home Dashboard
- **Personalized Greeting**: Time-of-day greeting with user name ("Hi, Sarah 👋 / Let's glow today!")
- **Global Streak Card**: Shows current daily streak with fire/star icon and motivational subtitle
- **Today's Rituals**: Quick view of the top 3 habits for today with inline increment buttons
- **Increment All**: One-tap button to mark all habits as done for fast daily logging
- **Navigation**: Hamburger menu → Drawer, Bell icon → Notifications

### 🔹 Habit Tracking
- **Habits List**: Full scrollable list of all habits with streak counters (fire icon + orange count)
- **Swipe-to-Delete**: Swipe left on any habit to reveal delete action with undo snackbar
- **Add Habit Dialog**: Create new habits with name and starting streak value
- **Bulk Increment**: "Increment all habits" button at the top of the list
- **Home Screen Widget**: Glance-powered interactive widget for tracking habits without opening the app

### 🔹 Habit Manager (NEW — Designed)
- **Tabbed Routine View**: Morning / Night / Lifestyle categories
- **Progress Bars**: Visual completion percentage per routine category
- **Streak Indicators**: Per-habit streak tracking within each tab
- **Deep-Dive Organization**: Detailed routine management beyond the simple list

### 🔹 Daily Skin Log (NEW — Designed)
- **Skin Condition Sliders**: Rate daily skin condition on multiple axes
- **Mood Selector**: Track emotional state alongside skin data
- **Contextual Notes**: Free-text notes for each daily entry
- **Correlation Ready**: Data feeds into Insights for habit-vs-skin analysis

### 🔹 My Cabinet (NEW — Designed)
- **Product Inventory**: Track skincare products with remaining volume indicators
- **Expiry Alerts**: Proactive notifications based on Period After Opening (PAO)
- **Product Levels**: Visual progress bars showing product usage
- **Smart Tracking**: Know when to repurchase before running out

### 🔹 Morning Guide (NEW — Designed)
- **Step-by-Step Assistant**: Interactive walkthrough of the daily routine
- **Built-in Timers**: Per-step timers (e.g., "Leave serum for 60s")
- **Product Recommendations**: Auto-suggests products from the user's Cabinet
- **Guided Experience**: Reduces decision fatigue in morning routines

### 🔹 Insights & Analytics (NEW — Enhanced Design)
- **Habit Bar Charts**: Weekly completion bars per habit (last 7 days)
- **Skin Score Line Graph**: Overlay of skin condition trends vs. habit consistency
- **AI Wellness Tips**: Contextual suggestions based on correlation analysis
- **Correlation Engine**: "Your skin improved 23% on days you used SPF" style insights

### 🔹 Mindset Vault
- **Saved Quotes**: Browse and manage favorite affirmations
- **Achievements Tab**: View unlocked/locked badges and milestones
- **Hero Quote Card**: Large gradient-background quote with heart + share buttons
- **Saved Gems**: Horizontal scroll of saved quotes at a glance

### 🔹 Custom Quotes
- **My Quotes**: View and manage personally created quotes
- **Add Quote Form**: Create quotes with content, author, and category (Personal / Motivation / Work / Life)
- **Delete with Confirmation**: Remove quotes with proper error states

### 🔹 Gamification & Achievements
- **Badge System**: Unlockable achievements based on habit streaks and app usage
- **Locked/Unlocked States**: Visual distinction between earned and pending badges
- **Milestone Examples**: "7-Day Streak 🔥", "First Habit ⭐", "30-Day Marathon 🏆", "Quote Collector 📚"

### 🔹 Settings
- **Dark/Light Mode**: Manual toggle with full theme support
- **Language**: English / Arabic localization
- **Daily Reminder**: Toggle + time picker for notification scheduling
- **Notifications Management**: Link to notifications center
- **Privacy Policy**: External link
- **Delete Account**: Destructive action with confirmation dialog

### 🔹 Notifications
- **Central Alert Feed**: System updates, reminder pings, and milestone celebrations
- **Mark All Read**: Batch action for clearing notification state
- **Do Not Disturb**: Toggle for silencing all notifications
- **Relative Timestamps**: "Just now", "2h ago", date for older items

### 🔹 User Experience
- **4-Step Onboarding Flow**: Skin Focus → Skin Type → Routine Level → Commitment + Name
- **Zoom Drawer Navigation**: Animated side menu with profile, achievements, analytics, and more
- **Search**: Full-text search across quotes and habits with category filtering
- **Offline Support**: All data stored locally via Room database
- **Home Screen Widgets**: Glance-powered habit tracking widget

---

## 🧭 Navigation Architecture

### Bottom Navigation (5 Tabs)
| Tab | Icon | Screen | Purpose |
|---|---|---|---|
| HOME | 🏠 House | Home Dashboard | Daily hub, streak, rituals |
| LOG | 📸 Camera | Daily Skin Log | Daily skin condition entry |
| HABITS | ✅ Checklist | Habits List | Habit management |
| INSIGHTS | 📊 Chart | Insights & Analytics | Trends, correlations |
| VAULT | 🔖 Bookmark | Mindset Vault | Quotes + achievements |

### Side Drawer (via Hamburger Menu)
- My Profile (edit)
- Achievements
- Detailed Analytics
- My Quotes
- My Cabinet
- Morning Guide
- FAQs
- Invite Friends
- Logout

---

## 🚀 Roadmap & Provisions

### Phase 1 — MVP (Current)
- [x] Habits: CRUD, streaks, widget, bulk increment
- [x] Home Dashboard: greeting, streak, today's habits
- [x] Onboarding: 4-step personalization
- [x] Settings: dark mode, language, reminders
- [x] Notifications: list, mark read, DND
- [x] Achievements: badge system
- [x] Analytics: weekly bar charts
- [x] Custom Quotes: add, view, delete

### Phase 2 — Skin Intelligence
- [ ] Daily Skin Log: condition sliders, mood, notes
- [ ] Insights & Analytics: correlation charts, AI tips
- [ ] My Cabinet: product inventory, PAO alerts
- [ ] Morning Guide: step-by-step with timers
- [ ] Habit Manager: tabbed routine categories

### Phase 3 — Growth
- [ ] Social Sharing: share quotes/progress as images
- [ ] Cloud Sync: Firebase Firestore backup
- [ ] Sign In / Sign Up: email + Google + Apple auth
- [ ] Data Export: CSV/JSON download
