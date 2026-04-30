package unimore.oop.stations;

import unimore.oop.entities.Ingredient;

public abstract class Station {
    protected Ingredient currentItem;

    public abstract void processTick();

    /**
     * Placing an item replaces whatever is currently on the station.
     * This is intentional: the player can remove items by picking them up first.
     */
    public void placeItem(Ingredient item) {
        this.currentItem = item;
    }

    public Ingredient getCurrentItem() { return currentItem; }

    public Ingredient retrieveItem() {
        Ingredient item = currentItem;
        currentItem = null;
        return item;
    }
}
