package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * ConnessioneDatabase contiene le funzionalitá relativa all'apertura del database.
 */
public class ConnessioneDatabase {
    // ATTRIBUTI
    private static ConnessioneDatabase instance;
    public Connection connection = null;
    private String nome;
    private String password;
    private String url;
    private String driver;

    // COSTRUTTORE PRIVATO
    private ConnessioneDatabase() throws SQLException {
        leggiConfigurazione();
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, nome, password);
            System.out.println("Connessione riuscita");

        } catch (ClassNotFoundException ex) {
            System.out.println("Database Connection Creation Failed : " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Metodo per leggere la configurazione da un file
    private void leggiConfigurazione() {
        String percorsoFileConfigurazione = "C:\\Users\\david\\Desktop\\progetto_OO\\parametriDatabase.txt";
        Map<String, String> configurazione = new HashMap<>();

        try {
            FileInputStream fileInput = new FileInputStream(new File(percorsoFileConfigurazione));
            InputStreamReader inputReader = new InputStreamReader(fileInput);
            try (BufferedReader bufferedReader = new BufferedReader(inputReader)) {

                String linea;

                while ((linea = bufferedReader.readLine()) != null) {
                    String[] parts = linea.split("=");
                    if (parts.length >= 2) {
                        String chiave = parts[0].trim();
                        String valore = parts[1].trim();
                        configurazione.put(chiave, valore);
                    }
                }

                bufferedReader.close();
            }
            inputReader.close();
            fileInput.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Imposta i valori letti dalla configurazione
        nome = configurazione.get("nome");
        password = configurazione.get("password");
        url = configurazione.get("url");
        driver = configurazione.get("driver");
    }

    /**
     * Se la connessione é aperta restituisce l'oggetto ConnessioneDatabase o istanzia un nuovo oggetto ConnessioneDataBase.
     *
     * @return instance
     * @throws SQLException  eccezione data da SQL
     */
    public static ConnessioneDatabase getInstance() throws SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new ConnessioneDatabase();
        }
        return instance;
    }

    /**
     * Restituisce l'oggetto connection sul quale eseguire le query.
     *
     * @return connection
     */
    public Connection getConnection() {
        return connection;
    }
}