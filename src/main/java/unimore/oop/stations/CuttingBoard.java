package unimore.oop.stations;

import unimore.oop.entities.Ingredient;

public class CuttingBoard extends Station {
    @Override
    public void processTick() {
        // Chopping is instant – handled in placeItem
    }

    @Override
    public void placeItem(Ingredient item) {
        super.placeItem(item);
        if (item != null) {
            item.chop();
        }
    }
}
