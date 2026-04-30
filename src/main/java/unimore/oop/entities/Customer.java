package unimore.oop.entities;

import java.util.List;

public class Customer {
    private final List<String> recipe;
    private int patience;
    private static final int MAX_PATIENCE = 30;

    public Customer(List<String> recipe) {
        this.recipe = recipe;
        this.patience = MAX_PATIENCE;
    }

    public List<String> getRecipe() { return recipe; }
    public int getPatience() { return patience; }

    public void decreasePatience() {
        if (patience > 0) patience--;
    }

    public boolean isAngry() { return patience <= 0; }
}
