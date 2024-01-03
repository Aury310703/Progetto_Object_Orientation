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
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void eseguiQueryDB(){
        try {
            PreparedStatement query = connection.prepareStatement("select * from auotore");
            ResultSet rs = query.executeQuery();
            while (rs.next()) {
                System.out.println("cazzo funziona");
            }
            rs.close();
            query.close();
            connection.close();
        } catch(Exception e){
            System.out.println("Errore: ");
        }
    }
}
