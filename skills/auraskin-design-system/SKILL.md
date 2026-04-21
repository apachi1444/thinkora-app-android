# Skill: AuraSkin UI Implementation Guide

## Purpose
This skill instructs any AI agent on how to properly implement screens for the AuraSkin Android app. It enforces the **Luminous Calm** design system and prevents hardcoded values.

---

## 🚫 ABSOLUTE RULES — Never Break These

### 1. NEVER Hardcode Colors
```kotlin
// ❌ FORBIDDEN — hardcoded hex colors
Color(0xFFF8F9FC)
Color.White
Color(0xFFEF4444)
Color(0xFF76B599)

// ✅ CORRECT — always use MaterialTheme tokens
MaterialTheme.colorScheme.background
MaterialTheme.colorScheme.surface
MaterialTheme.colorScheme.error
MaterialTheme.colorScheme.primary
```

### 2. NEVER Hardcode Typography
```kotlin
// ❌ FORBIDDEN
fontSize = 24.sp
fontWeight = FontWeight.Bold

// ✅ CORRECT — use the type scale from MaterialTheme
style = MaterialTheme.typography.headlineMedium   // Already Bold
style = MaterialTheme.typography.titleMedium       // Already SemiBold
style = MaterialTheme.typography.bodyLarge          // Already Regular
style = MaterialTheme.typography.labelSmall         // Already Bold + letterspaced
```

### 3. NEVER Hardcode Shapes
```kotlin
// ❌ FORBIDDEN
RoundedCornerShape(16.dp)
RoundedCornerShape(24.dp)

// ✅ CORRECT — use MaterialTheme shapes
MaterialTheme.shapes.medium   // 16dp — buttons, chips, icon containers
MaterialTheme.shapes.large    // 24dp — cards, containers, sections
MaterialTheme.shapes.extraLarge // 32dp — drawer corners, full dialogs
```

### 4. NEVER Hardcode Strings
```kotlin
// ❌ FORBIDDEN
Text("Your Habits")
Text("Settings")

// ✅ CORRECT — always use string resources
Text(stringResource(R.string.home_your_habits))
Text(stringResource(R.string.settings_title))
```
All user-facing strings must be in `core/designsystem/src/main/res/values/strings.xml` with Arabic translation in `values-ar/strings.xml`.

### 5. ALWAYS Use Design System Components
```kotlin
// ❌ FORBIDDEN — raw Material3 components for primary actions
Button(onClick = { ... }) { Text("Save") }
TopAppBar(title = { ... })

// ✅ CORRECT — use AuraSkin design system components
AuraSkinButton(onClick = { ... }) { Text(stringResource(R.string.common_save)) }
AuraSkinTopAppBar(title = { Text(...) })
AuraSkinTextField(value = ..., onValueChange = ...)
AuraSkinSelectionCard(title = ..., isSelected = ...)
```

---

## 🎨 Color Token Reference

### When to Use Each Token

| Purpose | Token | Example |
|---|---|---|
| Page background | `colorScheme.background` | Screen root `Column` / `Box` |
| Card / elevated surface | `colorScheme.surface` | Cards, dialogs, bottom sheets |
| Grouped container (section bg) | `colorScheme.surfaceVariant` | Settings section wrapper |
| Primary CTA / active nav | `colorScheme.primary` | Buttons, active tab, links |
| Text on primary buttons | `colorScheme.onPrimary` | Button label text |
| Heading & body text | `colorScheme.onSurface` | All primary text |
| Secondary/muted text | `colorScheme.onSurfaceVariant` | Subtitles, labels, timestamps |
| Accent / highlight | `colorScheme.tertiary` | Selected card border, streak accent |
| Borders & dividers | `colorScheme.outline` | Card borders, section separators |
| Outline at lower emphasis | `colorScheme.outlineVariant` | Ghost borders, subtle rings |
| Error / destructive | `colorScheme.error` | Delete buttons, error states |
| Streak / warning emphasis | Use `AuraSkinLightWarning` from Color.kt | Fire icons, streak count |

### Gradient Backgrounds (Streak Card)
For the global progress card, use a gradient from `primary` to `primary` with opacity:
```kotlin
Brush.linearGradient(
    colors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
    )
)
```

---

## 📐 Layout Patterns

### Screen Structure
Every screen follows this structure:
```kotlin
@Composable
fun FeatureScreen(...) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())  // or LazyColumn
            .padding(horizontal = 24.dp)            // 24dp horizontal padding
    ) {
        // Content with generous vertical spacing (24-40dp between sections)
    }
}
```

### Card Pattern ("Nested Surfaces")
Cards use surface-on-surfaceVariant nesting — a white card inside a light gray section:
```kotlin
// Section container
Column(
    modifier = Modifier
        .fillMaxWidth()
        .clip(MaterialTheme.shapes.large)                   // 24dp corners
        .background(MaterialTheme.colorScheme.surfaceVariant)
        .padding(8.dp)
) {
    // Individual item card inside
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)              // 16dp corners
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        // Content
    }
}
```

### Icon Container Pattern
Icons sit inside tinted circles:
```kotlin
Box(
    modifier = Modifier
        .size(48.dp)
        .clip(MaterialTheme.shapes.medium)  // 16dp rounded square
        .background(MaterialTheme.colorScheme.surfaceVariant),
    contentAlignment = Alignment.Center
) {
    Icon(
        imageVector = Icons.Outlined.WaterDrop,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary
    )
}
```

### Section Label Pattern
Section headers use ALL CAPS label style:
```kotlin
Text(
    text = stringResource(R.string.settings_appearance).uppercase(),
    style = MaterialTheme.typography.labelSmall,
    color = MaterialTheme.colorScheme.onSurfaceVariant,
    modifier = Modifier.padding(start = 8.dp, bottom = 12.dp)
)
```

---

## 🔲 Elevation & Shadows

Follow the **"No-Line Rule"** — never use `Divider()` or `1px` borders. Instead:
- **Separate items with space** (8dp gap between cards in a section)
- **Use tonal layering** (surfaceVariant container → surface cards)
- **Ambient shadow** for floating elements: `elevation = 0.dp` with `shadowElevation` in `graphicsLayer`

If a border is absolutely needed, use a "ghost border":
```kotlin
border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f))
```

---

## 🧭 Navigation Tokens

### Bottom Navigation (5 tabs)
| Tab | Route | Icon (Outlined) | Icon (Filled/Active) |
|---|---|---|---|
| Home | `home_screen` | `Icons.Outlined.Home` | `Icons.Filled.Home` |
| Log | `skin_log_screen` | `Icons.Outlined.Edit` | `Icons.Filled.Edit` |
| Habits | `habits_screen` | `Icons.Outlined.CheckCircle` | `Icons.Filled.CheckCircle` |
| Insights | `insights_screen` | `Icons.Outlined.Analytics` | `Icons.Filled.Analytics` |
| Vault | `vault_screen` | `Icons.Outlined.AutoStories` | `Icons.Filled.AutoStories` |

Active tab: `colorScheme.primary` with `surfaceVariant` background pill
Inactive tab: `colorScheme.onSurfaceVariant`

---

## 📋 Checklist Before Submitting Any Screen

- [ ] Zero hardcoded `Color(0x...)` values — all from `MaterialTheme.colorScheme`
- [ ] Zero hardcoded font sizes — all from `MaterialTheme.typography`
- [ ] Zero hardcoded corner radii — all from `MaterialTheme.shapes`
- [ ] Zero hardcoded user-facing strings — all from `stringResource(R.string.xxx)`
- [ ] Uses `AuraSkinButton` for primary CTAs
- [ ] Uses `AuraSkinTopAppBar` for sub-page headers
- [ ] Uses `AuraSkinTextField` for text inputs
- [ ] No `Divider()` composables — use spacing + tonal layering
- [ ] Proper dark mode support (all colors from theme, no `Color.White`)
- [ ] All interactive elements have contentDescription for accessibility
- [ ] Horizontal padding is 24dp (not 16dp)
- [ ] Section gaps are 24-40dp
- [ ] Cards use `MaterialTheme.shapes.large` (24dp)
- [ ] Icon containers use `MaterialTheme.shapes.medium` (16dp)

---

## 📁 File Organization

```
feature/<name>/
├── src/main/java/com/apachi/auraskin/feature/<name>/
│   ├── <Name>Screen.kt          # @Composable screen
│   ├── <Name>ViewModel.kt       # ViewModel + State + Events
│   └── components/              # Screen-specific sub-composables (optional)
└── build.gradle
```

Data flows through use cases only:
```
Screen → ViewModel → UseCase → Repository (interface in domain) → Impl (in data)
```

Never import from `core:data` in a feature module. Always go through `core:domain`.
