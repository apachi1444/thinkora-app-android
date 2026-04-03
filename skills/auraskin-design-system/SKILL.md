---
name: auraskin-design-system
description: |
  AuraSkin design system patterns — AuraSkinTheme, typography, color tokens, and custom UI components (AuraSkinButton, AuraSkinTextField, etc.). Use this skill whenever working on UI components, screens, styling, or fixing design inconsistencies. Trigger on phrases like "design system", "theme", "colors", "AuraSkinTheme", "Typography", "button", "textfield", "styling", or "UI component".
---

# AuraSkin — Design System

## Core Principle
AuraSkin relies on a centralized Design System located in the `core:designsystem` module to maintain a cohesive, skin-health-focused, premium brand identity. 

**Never** hardcode colors, typography, or dimensions directly into feature modules. **Never** use raw `androidx.compose.material3.x` components directly if an `AuraSkin*` equivalent exists.

---

## Theming (`AuraSkinTheme`)

All screens must be wrapped in `AuraSkinTheme` at the root, and previews should use it as well. 

```kotlin
@Composable
fun AuraSkinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
)
```

**Colors:**
The color palette represents holistic wellness and skin health (soothing greens, soft pinks, clean whites, and deep elegant dark modes).
Use colors via `MaterialTheme.colorScheme` where possible. Do not import raw `Color` objects outside of the design system.

---

## Components

Custom components enforce consistent styling (corner radius, elevation, typography). 

### Buttons
Do not use `androidx.compose.material3.Button` directly in feature screens. Use the AuraSkin equivalents.

```kotlin
// Primary action button
AuraSkinButton(
    text = "Save Log",
    onClick = { ... },
    modifier = Modifier.fillMaxWidth()
)

// Secondary outline button
AuraSkinOutlinedButton(
    text = "Cancel",
    onClick = { ... }
)
```

### Text Fields
For user inputs (e.g., skin notes, habit titles), use `AuraSkinTextField` inside forms.

```kotlin
AuraSkinTextField(
    value = state.notes,
    onValueChange = { onAction(SkinAction.OnNotesChange(it)) },
    label = "Skin Notes",
    placeholder = "How does your skin feel today?"
)
```

### Top App Bar
Use `AuraSkinTopAppBar` for consistent header styling.

```kotlin
AuraSkinTopAppBar(
    title = "Daily Log",
    onBackClick = { onNavigateBack() }
)
```

---

## Typography

AuraSkin uses a modern, readable typography set (e.g., Google Fonts: Inter or Outfit) configured in `Type.kt`. 

Never use hardcoded `FontSize`. Use `MaterialTheme.typography`:
- `headlineLarge` / `headlineMedium`: Screen titles.
- `titleMedium`: Section headers, card titles.
- `bodyLarge`: Primary reading text.
- `bodyMedium` / `bodySmall`: Secondary text, meta info, dates.

---

## Rules & Checklist

1. **Brand Migration**: Thinkora is now AuraSkin. Any remaining `Thinkora` prefixes (e.g., `ThinkoraButton`, `ThinkoraTheme`) are legacy and must be renamed.
2. **Local Component Usage**: Before importing `androidx.compose.material3.Button` or `TextField`, check `core:designsystem/component` for an AuraSkin wrapper.
3. **No Hardcoded Values**: Layout padding should use established token dimensions (e.g., `16.dp`, `24.dp`), not arbitrary numbers.
4. **Preview Wrapper**: Always wrap `@Preview` composables in `AuraSkinTheme { ... }`.
