# Kitchen Frenzy! 🍔👨‍🍳

## Project Goals
"Kitchen Frenzy" is an addictive, fast-paced time-management game built entirely in JavaFX. The goal of the project is to demonstrate advanced Object-Oriented Programming (OOP) concepts such as inheritance, interfaces, state management, and design patterns, all while providing a highly engaging user experience. 

The player must fulfill customer food orders before their patience runs out by moving raw ingredients across different cooking stations (e.g., Grills, Cutting Boards), managing timers, and combining final products.

## Design Decisions
To adhere to the 250-500 logic line limit and maintain high readability, the project strictly separates the Model (logic) from the View/Controller (JavaFX):

*   **Model-View-Controller (MVC):** The game logic relies purely on self-managing data structures and is decoupled from JavaFX.
*   **State Pattern:** Used robustly to manage the condition of ingredients (`RawState`, `CookingState`, `CookedState`, `BurnedState`). 
*   **Factory Pattern:** For generating random customer orders and recipes.
*   **Observer Pattern:** Used to connect the logic timers (ingredient cooking times or customer patience) safely to the JavaFX UI for visual updates.

## Internal Structure
*   **`engine/`**: The core game loop, handling the flow of time and overarching states (Score, Game Over, Progress).
*   **`entities/`**: The objects that exist within the world (e.g., `Customer`, `Ingredient`, `Recipe`).
*   **`stations/`**: Interactable nodes that modify the state of entities via strategies/interfaces.
*   **`gui/`**: JavaFX views and controllers that read from the engine and handle user input.
