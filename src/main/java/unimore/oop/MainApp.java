package unimore.oop;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import unimore.oop.gui.GameWindow;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        GameWindow gameWindow = new GameWindow();
        Scene scene = new Scene(gameWindow.getRoot(), 800, 600);
        
        primaryStage.setTitle("Kitchen Frenzy!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
