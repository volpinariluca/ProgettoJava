package unimore.oop.stations;

import unimore.oop.entities.Ingredient;

public abstract class Station {
    protected Ingredient currentItem;

    public abstract void processTick();

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
