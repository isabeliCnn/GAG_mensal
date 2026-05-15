package org.example;

import org.example.database.FlywayConfig;
import org.example.ui.LoginFrame;

public class Main {
    public static void main(String[] args) {
        FlywayConfig.rodarMigrations();
        java.awt.EventQueue.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
