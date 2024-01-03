package implementazionePostgresDAO;


import dao.WikiDAO;
import database.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WikiimplementazionePostgresDAO implements WikiDAO {
    private Connection connection;

    public WikiimplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void eseguiQueryDB() {
        try {
            PreparedStatement query = connection.prepareStatement("SELECT login FROM \"utente\"");
            System.out.println("La query funziona");
            ResultSet rs = query.executeQuery();
            System.out.println("La query funziona - Step 1");
            while (rs.next()) {
                System.out.println(rs.getString("login"));
            }
            System.out.println("La query funziona - Step 4");
            System.out.println("La query funziona - Step 4");
        } catch (Exception e) {
            System.out.println("Errore durante l'esecuzione della query: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
