package implementazionePostgresDAO;


import MODEL.Autore;
import MODEL.Pagina;
import dao.WikiDAO;
import database.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

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

    @Override
    public ArrayList<Pagina> ricercaTitoli(String titoloInserito) throws SQLException {

        ArrayList<Pagina> PagineTrovate = new ArrayList<>();

        try {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM pagina WHERE titolo LIKE \"%titoloInserito%\"");
            ResultSet rs = query.executeQuery();
            if(rs.wasNull()){

            }
            while (rs.next()) {
                String titolo = rs.getString("titolo");
                LocalDateTime dataOra = rs.getTimestamp("dataOraCreazione").toLocalDateTime();
                int idAutore = rs.getInt("idAutore");
                PreparedStatement query1 = connection.prepareStatement("SELECT * FROM autore WHERE idAutore = \"idAutore\" GROUP BY idAutore LIMIT 1");
                ResultSet rs1 = query1.executeQuery();
                Pagina pagina = new Pagina(titolo, dataOra, rs1.getString("nome"), rs1.getString("cognome"),rs1.getString("login"), rs1.getString("password"), rs1.getString("email"), rs1.getDate("dataNascita"));
                PagineTrovate.add(pagina);
            }
        } catch (Exception e) {
            System.out.println("Errore durante l'esecuzione della query: " + e.getMessage());
            e.printStackTrace();
        }
        return PagineTrovate;
    }
}
