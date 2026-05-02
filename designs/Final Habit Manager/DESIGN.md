# Design System Strategy: The Ethereal Holistic Framework

## 1. Overview & Creative North Star: "The Digital Sanctuary"
This design system moves away from the rigid, boxed-in layouts of traditional utility apps and toward an **Editorial Sanctuary**. The Creative North Star is focused on "Breathable Luxury"—an experience that feels as much like a high-end wellness editorial as it does a functional tool.

To achieve this, we reject "template" thinking. We embrace **intentional asymmetry**, where content isn't always perfectly centered, and **tonal depth**, where layers feel organic rather than engineered. By using a sophisticated typography scale and overlapping glass elements, we create a sense of space that encourages the user to slow down, breathe, and engage with intention.

---

## 2. Color & Surface Philosophy
The palette is rooted in the "Zen" spectrum, utilizing desaturated, nature-inspired tones to trigger a parasympathetic nervous system response.

### The "No-Line" Rule
**Explicit Instruction:** Use of 1px solid borders for sectioning is strictly prohibited. Borders are a relic of low-resolution design; they create visual "noise" that disrupts the sense of calm. 
- **Definition through Tone:** Boundaries must be defined solely through background color shifts. For example, a `surface-container-low` section sitting on a `surface` background provides all the separation needed.
- **Definition through Space:** Use the Spacing Scale to create "islands" of content that feel held by the background, not trapped by lines.

### Surface Hierarchy & Nesting
Treat the UI as a physical stack of fine, semi-translucent papers. 
- **Nesting:** Place `surface-container-lowest` (#FFFFFF) cards inside a `surface-container-low` (#F6F3F1) section to create a soft, natural lift.
- **Glassmorphism:** For top navigation and floating action bars, use the `surface` color at 70% opacity with a `20px backdrop-blur`. This allows the "soul" of the content to bleed through as the user scrolls, maintaining a connection to the whole.

### Signature Textures
Avoid flat, "dead" fills for primary actions. Utilize subtle linear gradients (e.g., `primary` #2F6854 to `primary-container` #7EB8A0) at a 135-degree angle. This mimics the way light hits a leaf or a silk ribbon, providing a premium "glow" that flat hex codes cannot replicate.

---

## 3. Typography: Editorial Authority
We utilize **Manrope** for its geometric balance and humanist warmth. Our hierarchy is designed to guide the eye like a magazine layout.

- **Display (lg/md):** Reserved for moments of reflection or morning greetings. Large, low-contrast (Deep Slate) type that demands the screen but doesn't shout.
- **Headline (sm/md):** Used for ritual titles. These should often be paired with significant top-padding to let the words "float."
- **Body (lg):** The workhorse for skincare routines and mental health guides. Increased line-height (1.6x) is mandatory to ensure readability and a "relaxed" visual density.
- **Labels:** Always in `Deep Slate` (#2A3B36) with slight letter-spacing (0.05rem) to ensure they feel intentional, not just "small text."

---

## 4. Elevation & Depth: Tonal Layering
We do not use "Drop Shadows" in the traditional sense. We use **Ambient Occlusion**.

- **The Layering Principle:** Depth is achieved by stacking. A `surface-container-highest` element feels closer to the user than a `surface-container-lowest` element.
- **Ambient Shadows:** If a card must float, use a shadow color tinted with the `on-surface` tone (#1B1C1B). 
    - *Specs:* Blur: 40px, Y: 12px, Opacity: 4%. This creates a "glow" of shadow rather than a harsh edge.
- **The "Ghost Border" Fallback:** If a UI element (like a search bar) risks disappearing, use a "Ghost Border": `outline-variant` (#BFC9C3) at 15% opacity. Never 100%.

---

## 5. Component Guidelines

### Buttons (The "Soft-Touch" CTA)
- **Primary:** Gradient fill (Sage Green spectrum), `xl` (1.5rem) corner radius. No shadow.
- **Secondary:** `surface-container-highest` fill with `Deep Slate` text. 
- **Padding:** High horizontal padding (24px+) to create a wide, stable footprint.

### Cards & Ritual Lists
- **Rule:** Forbid divider lines.
- **Styling:** Separate list items using 8px of vertical whitespace and a slight background tint change on hover/active states. Cards should use the `lg` (1rem) roundedness scale to feel soft to the touch.

### Input Fields (The "Quiet" Input)
- **Styling:** Use `surface-container-low` as the field fill. No bottom line. The label should sit elegantly above in `label-md`.
- **Focus State:** Transition the "Ghost Border" from 15% to 40% opacity and shift the background to `surface-container-lowest`.

### Wellness Specific: The "Glass Header"
- A persistent top bar using `surface` at 80% opacity with `backdrop-blur: 12px`. It should have no bottom border; instead, a subtle `primary` color glow should emanate from the active navigation icon.

---

## 6. Do’s and Don’ts

### Do:
- **Embrace White Space:** If a screen feels "busy," add 16px of padding to every element.
- **Use Asymmetric Layouts:** In hero sections, align text to the left and let a "Zen" image (skincare texture, botanical leaf) overlap the right edge of the screen.
- **Soft Transitions:** All state changes (hover, active, screen transitions) must use a `cubic-bezier(0.4, 0, 0.2, 1)` easing over 300ms.

### Don’t:
- **No Pure Black:** Never use #000000. Use `Deep Slate` (#2A3B36) to maintain the "Zen" atmosphere.
- **No Hard Corners:** Avoid the `none` or `sm` roundedness scales unless for very specific technical data. High-end wellness is soft.
- **No High-Contrast Separators:** Never use a dark line to separate a header from a body. Use a transition from `surface-bright` to `surface-container`.