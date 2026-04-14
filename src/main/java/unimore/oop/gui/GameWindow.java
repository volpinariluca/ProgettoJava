package unimore.oop.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class GameWindow {
    
    private BorderPane root;

    public GameWindow() {
        root = new BorderPane();
        
        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("Kitchen Frenzy");
        titleLabel.setFont(new Font("Arial", 40));
        
        Label subtitleLabel = new Label("Cooking stations and UI will go here.\nLogic is completely separated.");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-alignment: center;");
        
        centerBox.getChildren().addAll(titleLabel, subtitleLabel);
        
        root.setCenter(centerBox);
    }
    
    public BorderPane getRoot() {
        return root;
    }
}
