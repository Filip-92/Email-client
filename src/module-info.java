module JavaFxEmailClientCourse {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.web;
    requires activation;
    requires javax.mail;
    requires java.desktop;

    opens com_peszke;
    opens com_peszke.view;
    opens com_peszke.controller;
    opens com_peszke.model;
}