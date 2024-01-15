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
            String query = "SELECT * FROM pagina WHERE titolo LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + titoloInserito + "%");
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.wasNull()){
                System.out.println("no");
            }
            while (rs.next()) {
                String titolo = rs.getString("titolo");
                LocalDateTime dataOra = rs.getTimestamp("dataOraCreazione").toLocalDateTime();
                int idAutore = rs.getInt("idAutore");
                String query2 = "SELECT * FROM utente WHERE idUtente = ? GROUP BY idUtente LIMIT 1";
                PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                preparedStatement2.setInt(1, idAutore);
                ResultSet rs1 = preparedStatement2.executeQuery();
                rs1.next();
                Pagina pagina = new Pagina(titolo, dataOra, rs1.getString("nome"), rs1.getString("cognome"),rs1.getString("login"), rs1.getString("password"), rs1.getString("email"), rs1.getDate("dataNascita"));
                PagineTrovate.add(pagina);
                System.out.println(pagina.getTitolo());
            }
        } catch (Exception e) {
            System.out.println("Errore durante l'esecuzione della query: " + e.getMessage());
            e.printStackTrace();
        }
        connection.close();
        return PagineTrovate;
    }
}
