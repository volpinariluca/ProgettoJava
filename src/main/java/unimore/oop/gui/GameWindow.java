package unimore.oop.gui;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import unimore.oop.engine.GameEngine;
import unimore.oop.entities.Customer;
import unimore.oop.entities.CookingState;
import unimore.oop.entities.Ingredient;
import unimore.oop.stations.AssemblyStation;
import unimore.oop.stations.CuttingBoard;
import unimore.oop.stations.GrillStation;

import java.util.List;

public class GameWindow {

    // ── Layout ──────────────────────────────────────────────────────────────
    private final BorderPane root;
    private final Label scoreLabel    = new Label("Score: 0");
    private final Label handLabel     = new Label("Hand: (empty)");
    private final VBox  customersBox  = new VBox(8);
    private final VBox  grillBox      = new VBox(6);
    private final VBox  boardBox      = new VBox(6);
    private final VBox  assemblyBox   = new VBox(6);

    // ── Logic ────────────────────────────────────────────────────────────────
    private final GameEngine    engine   = new GameEngine();
    private final GrillStation  grill    = new GrillStation();
    private final CuttingBoard  board    = new CuttingBoard();
    private final AssemblyStation assembly = new AssemblyStation();

    /** Ingredient being carried by the player */
    private Ingredient hand;

    // ── Tick counter (60 fps → 1 game tick per second) ──────────────────────
    private long lastTickNs = 0;
    private static final long TICK_INTERVAL_NS = 1_000_000_000L;

    public GameWindow() {
        engine.registerStation(grill);
        engine.registerStation(board);
        engine.registerStation(assembly);

        root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e2e;");

        root.setTop(buildTopBar());
        root.setLeft(buildIngredientPanel());
        root.setCenter(buildStationsPanel());
        root.setRight(buildCustomersPanel());
        root.setBottom(buildHandBar());

        engine.startGame();
        startTimer();
    }

    public BorderPane getRoot() { return root; }

    // ── Timer ────────────────────────────────────────────────────────────────

    private void startTimer() {
        new AnimationTimer() {
            @Override
            public void handle(long nowNs) {
                if (nowNs - lastTickNs >= TICK_INTERVAL_NS) {
                    lastTickNs = nowNs;
                    engine.gameTick();
                    updateUI();
                }
            }
        }.start();
    }

    // ── UI builders ──────────────────────────────────────────────────────────

    private HBox buildTopBar() {
        HBox bar = new HBox(20);
        bar.setPadding(new Insets(12, 20, 12, 20));
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setStyle("-fx-background-color: #313244;");

        Label title = new Label("🍔 Kitchen Frenzy!");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#cdd6f4"));

        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        scoreLabel.setTextFill(Color.web("#a6e3a1"));

        bar.getChildren().addAll(title, scoreLabel);
        return bar;
    }

    private VBox buildIngredientPanel() {
        VBox panel = new VBox(8);
        panel.setPadding(new Insets(12));
        panel.setStyle("-fx-background-color: #181825;");

        Label title = sectionLabel("🥩 Ingredients");
        panel.getChildren().add(title);

        for (String name : List.of("Meat", "Bread", "Lettuce", "Tomato")) {
            panel.getChildren().add(createIngredientButton(name));
        }
        return panel;
    }

    private HBox buildStationsPanel() {
        grillBox.setPadding(new Insets(10));
        grillBox.setStyle(stationStyle("#45475a"));

        boardBox.setPadding(new Insets(10));
        boardBox.setStyle(stationStyle("#45475a"));

        assemblyBox.setPadding(new Insets(10));
        assemblyBox.setStyle(stationStyle("#45475a"));

        buildStationButtons();

        HBox stations = new HBox(12, grillBox, boardBox, assemblyBox);
        stations.setPadding(new Insets(12));
        stations.setAlignment(Pos.TOP_CENTER);
        return stations;
    }

    private VBox buildCustomersPanel() {
        VBox panel = new VBox(8);
        panel.setPadding(new Insets(12));
        panel.setPrefWidth(180);
        panel.setStyle("-fx-background-color: #181825;");

        Label title = sectionLabel("👥 Customers");
        panel.getChildren().addAll(title, customersBox);
        return panel;
    }

    private HBox buildHandBar() {
        HBox bar = new HBox(12);
        bar.setPadding(new Insets(10, 20, 10, 20));
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setStyle("-fx-background-color: #313244;");

        handLabel.setTextFill(Color.web("#cdd6f4"));
        handLabel.setFont(Font.font("Arial", 14));

        Button trashBtn = styledButton("🗑 Discard", "#f38ba8");
        trashBtn.setOnAction(e -> discardHand());

        bar.getChildren().addAll(handLabel, trashBtn);
        return bar;
    }

    // ── Station button helpers ───────────────────────────────────────────────

    private void buildStationButtons() {
        grillBox.getChildren().clear();
        boardBox.getChildren().clear();
        assemblyBox.getChildren().clear();

        grillBox.getChildren().add(sectionLabel("🔥 Grill"));
        boardBox.getChildren().add(sectionLabel("🔪 Cutting Board"));
        assemblyBox.getChildren().add(sectionLabel("🍽 Assembly"));

        // Grill
        Button grillBtn = styledButton("Place on Grill", "#fab387");
        grillBtn.setOnAction(e -> interactWithStation(grill));
        Button grillPickBtn = styledButton("Pick up from Grill", "#89b4fa");
        grillPickBtn.setOnAction(e -> pickFromStation(grill));
        updateStationView(grillBox, grill.getCurrentItem());
        grillBox.getChildren().addAll(grillBtn, grillPickBtn);

        // Cutting board
        Button chopBtn = styledButton("Place on Board", "#fab387");
        chopBtn.setOnAction(e -> interactWithStation(board));
        Button boardPickBtn = styledButton("Pick up from Board", "#89b4fa");
        boardPickBtn.setOnAction(e -> pickFromStation(board));
        updateStationView(boardBox, board.getCurrentItem());
        boardBox.getChildren().addAll(chopBtn, boardPickBtn);

        // Assembly
        Button assemblyBtn = styledButton("Place on Plate", "#fab387");
        assemblyBtn.setOnAction(e -> placeOnAssembly());
        Button clearBtn = styledButton("Clear Plate", "#f38ba8");
        clearBtn.setOnAction(e -> { assembly.retrievePlate(); updateUI(); });
        VBox plateList = new VBox(3);
        for (Ingredient ing : assembly.getPlateIngredients()) {
            Label l = new Label("• " + ing);
            l.setTextFill(Color.web("#cdd6f4"));
            plateList.getChildren().add(l);
        }
        assemblyBox.getChildren().addAll(plateList, assemblyBtn, clearBtn);
    }

    // ── Actions ──────────────────────────────────────────────────────────────

    private void interactWithStation(unimore.oop.stations.Station station) {
        if (hand == null) return;
        station.placeItem(hand);
        hand = null;
        updateUI();
    }

    private void pickFromStation(unimore.oop.stations.Station station) {
        if (station.getCurrentItem() == null) return;
        hand = station.retrieveItem();
        updateUI();
    }

    private void placeOnAssembly() {
        if (hand == null) return;
        assembly.placeItem(hand);
        hand = null;
        updateUI();
    }

    private void discardHand() {
        hand = null;
        updateUI();
    }

    private void servePlate(Customer customer) {
        boolean ok = engine.tryServeMeal(customer);
        if (!ok) {
            showFeedback("❌ Wrong order – try again!");
        }
        updateUI();
    }

    // ── Full UI refresh ───────────────────────────────────────────────────────

    private void updateUI() {
        scoreLabel.setText("Score: " + engine.getScore());
        handLabel.setText(hand == null ? "Hand: (empty)" : "Hand: " + hand);
        buildStationButtons();
        refreshCustomers();
    }

    private void refreshCustomers() {
        customersBox.getChildren().clear();
        for (Customer c : engine.getActiveCustomers()) {
            customersBox.getChildren().add(createCustomerCard(c));
        }
    }

    // ── Card / widget factories ───────────────────────────────────────────────

    private VBox createCustomerCard(Customer customer) {
        VBox card = new VBox(4);
        card.setPadding(new Insets(8));
        card.setStyle("-fx-background-color: #313244; -fx-background-radius: 6;");

        Label recipe = new Label("🧾 " + String.join(", ", customer.getRecipe()));
        recipe.setTextFill(Color.web("#cdd6f4"));
        recipe.setFont(Font.font("Arial", 12));

        double patiencePct = customer.getPatience() / 30.0;
        Color barColor = patiencePct > 0.5 ? Color.web("#a6e3a1")
                       : patiencePct > 0.25 ? Color.web("#f9e2af")
                       : Color.web("#f38ba8");
        Label patienceBar = new Label("⏳ " + customer.getPatience() + "s");
        patienceBar.setTextFill(barColor);

        Button serveBtn = styledButton("✅ Serve", "#a6e3a1");
        serveBtn.setOnAction(e -> servePlate(customer));

        card.getChildren().addAll(recipe, patienceBar, serveBtn);
        return card;
    }

    private Button createIngredientButton(String name) {
        String emoji = switch (name) {
            case "Meat"    -> "🥩";
            case "Bread"   -> "🍞";
            case "Lettuce" -> "🥬";
            case "Tomato"  -> "🍅";
            default        -> "🍴";
        };
        Button btn = styledButton(emoji + " " + name, "#89dceb");
        btn.setOnAction(e -> {
            hand = new Ingredient(name);
            updateUI();
        });
        return btn;
    }

    private void updateStationView(VBox box, Ingredient item) {
        if (item == null) return;
        Label lbl = new Label("📦 " + item);
        lbl.setTextFill(itemColor(item.getState()));
        lbl.setFont(Font.font("Arial", 13));
        box.getChildren().add(lbl);
    }

    // ── Styling helpers ──────────────────────────────────────────────────────

    private Button styledButton(String text, String hex) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: " + hex + "; -fx-text-fill: #1e1e2e;"
                + " -fx-font-weight: bold; -fx-background-radius: 4;");
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }

    private Label sectionLabel(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lbl.setTextFill(Color.web("#b4befe"));
        return lbl;
    }

    private String stationStyle(String bg) {
        return "-fx-background-color: " + bg + "; -fx-background-radius: 8;";
    }

    private Color itemColor(CookingState state) {
        return switch (state) {
            case RAW     -> Color.web("#cdd6f4");
            case COOKING -> Color.web("#f9e2af");
            case COOKED  -> Color.web("#a6e3a1");
            case BURNED  -> Color.web("#585b70");
            case CHOPPED -> Color.web("#89dceb");
        };
    }

    private void showFeedback(String msg) {
        // Lightweight non-blocking toast via a temporary label in the hand bar
        handLabel.setText(msg);
    }
}
