package unimore.oop.entities;

public class Ingredient {
    private final String name;
    private CookingState state;
    private int cookProgress;

    private static final int TIME_TO_COOK = 5;
    private static final int TIME_TO_BURN = 12;

    public Ingredient(String name) {
        this.name = name;
        this.state = CookingState.RAW;
        this.cookProgress = 0;
    }

    public String getName() { return name; }
    public CookingState getState() { return state; }

    public void cook() {
        if (state == CookingState.RAW) {
            state = CookingState.COOKING;
        } else if (state == CookingState.COOKING) {
            cookProgress++;
            if (cookProgress >= TIME_TO_BURN) {
                state = CookingState.BURNED;
            } else if (cookProgress >= TIME_TO_COOK) {
                state = CookingState.COOKED;
            }
        }
    }

    public void chop() {
        if (state == CookingState.RAW && isProduce()) {
            state = CookingState.CHOPPED;
        }
    }

    public CookingState getRequiredState() {
        if (name.equals("Meat")) return CookingState.COOKED;
        if (isProduce()) return CookingState.CHOPPED;
        return CookingState.RAW;
    }

    private boolean isProduce() {
        return name.equals("Lettuce") || name.equals("Tomato");
    }

    @Override
    public String toString() {
        return name + " [" + state + "]";
    }
}
