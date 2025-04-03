module com.cab302.cab302project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.desktop;
    requires java.sql;
    requires jdk.jdi;

    opens com.flashcards.cab to javafx.fxml;
    exports com.flashcards.cab;
    exports com.flashcards.cab.controller;
    opens com.flashcards.cab.controller to javafx.fxml;
    exports com.flashcards.cab.model;
    opens com.flashcards.cab.model to javafx.fxml;
}