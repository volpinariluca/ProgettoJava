# AGENTS.md

## Project snapshot
`Kitchen Frenzy!` is a JavaFX time-management game built with Maven and Java 17.  
The app is module-based (`module unimore.oop`) and starts from `unimore.oop.MainApp`.  
There is no `src/test` tree; verify changes with `mvn -q -DskipTests compile` and a manual run.

## Line-count budget
| Layer | Budget |
|-------|--------|
| `engine/`, `entities/`, `stations/` (logic) | **≤ 500 lines** |
| `gui/` and any SFX/presentation code | **Unlimited** |

Count only non-blank, non-comment lines when tracking the budget.

## Architecture you must preserve
- Strict MVC split: `engine/` + `entities/` + `stations/` = Model; `gui/` = View/Controller.
- Keep game rules in `engine/` and domain state in `entities/`; `gui/GameWindow.java` wires UI clicks to engine/station actions.
- `MainApp → GameWindow → GameEngine` is the startup path; there is no FXML scene graph in use today.

## Core flow
- `GameEngine.gameTick()` processes every registered `Station`, decreases `Customer` patience, and spawns new customers via `RecipeFactory.generateRandomRecipe()`.
- `GameWindow` registers `GrillStation`, `CuttingBoard`, and `AssemblyStation` with the engine and owns the `AnimationTimer` loop (one tick per second).
- Recipe matching is exact on ingredient names and required states:
  - `Meat` → must be `COOKED`
  - `Lettuce` / `Tomato` → must be `CHOPPED`
  - `Bread` → stays `RAW`

## Domain patterns
- `Ingredient` is a state machine: `RAW → COOKING → COOKED → BURNED`; `CHOPPED` is a special terminal state for produce.
- `Station` is the abstract interaction point; `GrillStation` advances cooking, `CuttingBoard` chops instantly on placement, and `AssemblyStation` accumulates a plate list.
- `RecipeFactory` is a stateless factory returning `List<String>` recipes using a `switch` expression.

## Project-specific conventions
- Use `unimore.oop.*` package naming and keep exports aligned with `module-info.java`.
- JavaFX UI is built programmatically in `GameWindow`; prefer extending that pattern instead of introducing FXML unless you also update `module-info.java` and the launch flow.
- The UI is a direct state mirror of the engine: `updateUI()` rebuilds labels and buttons each tick rather than using bindings.

## Build and run
```bash
# Sanity-check compile (fast)
mvn -q -DskipTests compile

# Full package
mvn clean package

# Launch the game
mvn javafx:run
```

## When making changes
- If you add a new `Station` subclass, register it in `GameWindow` and call `engine.registerStation(...)` so it participates in ticking.
- If you change ingredient states or recipe rules, update both `Ingredient.getRequiredState()` and the matching logic in `GameEngine.tryServeMeal()`.
- Keep the GUI and engine in sync; the UI assumes methods like `getCurrentItem()`, `retrievePlate()`, and `getActiveCustomers()` remain available.
- After any logic change, recount logic lines to confirm the budget is still respected.
