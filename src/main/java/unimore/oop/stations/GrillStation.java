package unimore.oop.stations;

import unimore.oop.entities.CookingState;

public class GrillStation extends Station {
    @Override
    public void processTick() {
        if (currentItem != null
                && (currentItem.getState() == CookingState.RAW
                    || currentItem.getState() == CookingState.COOKING)) {
            currentItem.cook();
        }
    }
}
