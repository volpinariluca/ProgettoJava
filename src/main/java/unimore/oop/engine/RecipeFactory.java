package unimore.oop.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecipeFactory {
    private static final Random random = new Random();

    public static List<String> generateRandomRecipe() {
        return switch (random.nextInt(3)) {
            case 0 -> new ArrayList<>(List.of("Meat", "Bread"));
            case 1 -> new ArrayList<>(List.of("Meat"));
            case 2 -> new ArrayList<>(List.of("Lettuce", "Bread", "Tomato"));
            // default required for exhaustive switch expression; unreachable at runtime
            default -> new ArrayList<>(List.of("Meat"));
        };
    }
}
