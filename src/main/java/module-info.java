module com.example.miniprojetjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.mariadb.jdbc;
    requires org.slf4j;
    requires jbcrypt;

    opens com.example.miniprojetjava to javafx.fxml, javafx.graphics;
    exports com.example.miniprojetjava.controller;
    exports com.example.miniprojetjava.service;
    exports com.example.miniprojetjava.util;
    opens com.example.miniprojetjava.util to javafx.fxml;
    opens com.example.miniprojetjava.controller to javafx.fxml;

    opens com.example.miniprojetjava.model to javafx.base;
}