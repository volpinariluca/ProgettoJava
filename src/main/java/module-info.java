module unimore.oop {
    requires javafx.controls;
    requires javafx.fxml;

    opens unimore.oop to javafx.fxml;
    exports unimore.oop;
    exports unimore.oop.gui;
    exports unimore.oop.engine;
    exports unimore.oop.entities;
    exports unimore.oop.stations;
}
