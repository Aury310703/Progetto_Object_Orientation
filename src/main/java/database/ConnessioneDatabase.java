package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnessioneDatabase {
    // ATTRIBUTI
    private static ConnessioneDatabase instance;
    public Connection connection = null;
    private String nome = "postgres";
    private String password = "napolia1";
    private String url = "jdbc:postgresql://localhost:5432/progetto_prova";
    private String driver = "org.postgresql.Driver";

    // COSTRUTTORE
    private ConnessioneDatabase() throws SQLException {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, nome, password);
            System.out.println("connessione riuscita");

        } catch (ClassNotFoundException ex) {
            System.out.println("Database Connection Creation Failed : " + ex.getMessage());
            ex.printStackTrace();
        }

    }


    public static ConnessioneDatabase getInstance() throws SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new ConnessioneDatabase();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}


