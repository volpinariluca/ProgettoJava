package unimore.oop.stations;

import unimore.oop.entities.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class AssemblyStation extends Station {
    private final List<Ingredient> plateIngredients = new ArrayList<>();

    @Override
    public void processTick() {
        // No passive processing needed
    }

    @Override
    public void placeItem(Ingredient item) {
        if (item != null) {
            plateIngredients.add(item);
        }
    }

    public List<Ingredient> getPlateIngredients() { return plateIngredients; }

    public List<Ingredient> retrievePlate() {
        List<Ingredient> plate = new ArrayList<>(plateIngredients);
        plateIngredients.clear();
        return plate;
    }
}
