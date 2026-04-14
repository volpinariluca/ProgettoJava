module unimore.oop {
    requires javafx.controls;
    requires javafx.fxml;

    opens unimore.oop to javafx.fxml;
    exports unimore.oop;
    exports unimore.oop.gui;
}
