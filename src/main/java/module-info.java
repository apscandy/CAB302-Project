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

    opens com.cab302.cab302project to javafx.fxml;
    exports com.cab302.cab302project;
    exports com.cab302.cab302project.controller;
    opens com.cab302.cab302project.controller to javafx.fxml;
    exports com.cab302.cab302project.model;
    opens com.cab302.cab302project.model to javafx.fxml;
    opens com.cab302.cab302project.controller.deck to javafx.fxml;
}