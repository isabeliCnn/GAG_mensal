package org.example.database;

import org.flywaydb.core.Flyway;
import java.sql.Connection;
import java.sql.DriverManager;

public class Conexao {

    private static final String URL  = "jdbc:postgresql://localhost:5432/BAR&RESTAURANTE";    private static final String USER = "postgres";
    private static final String PASS = "1234";

    public static Connection getConexao() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            return null;
        }
    }

    public static void rodarMigrations() {
        Flyway flyway = Flyway.configure()
                .dataSource(URL, USER, PASS)
                .baselineOnMigrate(true)
                .load();
        flyway.migrate();
        System.out.println("Migrations executadas!");
    }
}