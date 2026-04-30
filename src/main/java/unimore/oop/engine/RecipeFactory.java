package unimore.oop.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RecipeFactory {
    private static final Random random = new Random();

    public static List<String> generateRandomRecipe() {
        return switch (random.nextInt(3)) {
            case 0 -> new ArrayList<>(Arrays.asList("Meat", "Bread"));
            case 1 -> new ArrayList<>(List.of("Meat"));
            case 2 -> new ArrayList<>(Arrays.asList("Lettuce", "Bread", "Tomato"));
            default -> new ArrayList<>(List.of("Meat"));
        };
    }
}
