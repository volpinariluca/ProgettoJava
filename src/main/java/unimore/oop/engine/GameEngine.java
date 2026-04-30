package unimore.oop.engine;

import unimore.oop.entities.CookingState;
import unimore.oop.entities.Customer;
import unimore.oop.entities.Ingredient;
import unimore.oop.stations.AssemblyStation;
import unimore.oop.stations.Station;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class GameEngine {
    private final List<Station> stations = new ArrayList<>();
    private final List<Customer> activeCustomers = new ArrayList<>();
    private int score = 0;
    private int gameTicks = 0;
    private int spawnInterval = 15;
    private boolean gameStarted = false;
    private AssemblyStation assemblyStation;
    private Consumer<String> eventLogger = msg -> {};

    public void registerStation(Station s) {
        stations.add(s);
        if (s instanceof AssemblyStation as) {
            assemblyStation = as;
        }
    }

    public void startGame() { gameStarted = true; }
    public boolean isGameStarted() { return gameStarted; }
    public int getScore() { return score; }
    public List<Customer> getActiveCustomers() { return activeCustomers; }
    public void setEventLogger(Consumer<String> logger) { this.eventLogger = logger; }

    public void gameTick() {
        if (!gameStarted) return;

        for (Station s : stations) {
            s.processTick();
        }

        Iterator<Customer> it = activeCustomers.iterator();
        while (it.hasNext()) {
            Customer c = it.next();
            c.decreasePatience();
            if (c.isAngry()) {
                score -= 10;
                it.remove();
                eventLogger.accept("A customer left angrily! -10 Score.");
            }
        }

        gameTicks++;
        if (gameTicks % spawnInterval == 0) {
            spawnCustomer();
            if (spawnInterval > 5 && gameTicks % (spawnInterval * 2) == 0) {
                spawnInterval--;
            }
        }
    }

    private void spawnCustomer() {
        activeCustomers.add(new Customer(RecipeFactory.generateRandomRecipe()));
    }

    public boolean tryServeMeal(Customer customer) {
        if (assemblyStation == null) return false;
        List<Ingredient> plate = assemblyStation.retrievePlate();
        List<String> required = new ArrayList<>(customer.getRecipe());

        for (Ingredient ing : plate) {
            CookingState needed = ing.getRequiredState();
            if (required.contains(ing.getName()) && ing.getState() == needed) {
                required.remove(ing.getName());
            }
        }

        // All required items present and plate has no extra ingredients
        if (required.isEmpty() && plate.size() == customer.getRecipe().size()) {
            score += 20;
            activeCustomers.remove(customer);
            eventLogger.accept("Order served! +20 Score.");
            return true;
        }

        // Wrong order – put ingredients back on the plate
        for (Ingredient ing : plate) {
            assemblyStation.placeItem(ing);
        }
        return false;
    }
}
