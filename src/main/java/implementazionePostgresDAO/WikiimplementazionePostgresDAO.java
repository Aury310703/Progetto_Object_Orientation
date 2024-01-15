package implementazionePostgresDAO;


import MODEL.*;
import dao.WikiDAO;
import database.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

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

    @Override
    public String getTestoPagina(Pagina paginaSelezionata) throws SQLException {
        String testoPagina = null;

//        if()
        String query = "SELECT idutente FROM utente WHERE login = ? AND ruolo = " + "autore";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        Autore autore = paginaSelezionata.getAutore();
        System.out.println(autore.getNome());
        preparedStatement.setString(1, autore.getLogin());
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        int idAutore = rs.getInt("idutente");

        query = "SELECT * FROM frase_corrente F JOIN pagina P ON F.idpagina = P.idpagina WHERE = P.idAutore = ?";
        preparedStatement.setInt(1, idAutore);
        rs = preparedStatement.executeQuery();
        if(rs == null){

        }
        Frase_Corrente frase = null;
        ModificaProposta fraseProposta = null;
        while(rs.next()){
            frase = new Frase_Corrente(rs.getString("stringainserita"), rs.getInt("numerazione"), paginaSelezionata, rs.getDate("dataonserimento"), rs.getTime("irainserimento"));
            paginaSelezionata.addFrasi(frase);
            String query2 = "SELECT * FROM modificaproposta WHERE idPagina = ? AND stringainserita = ? AND numerazione = ?";
            PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
            preparedStatement2.setInt(1, rs.getInt("idPagina"));
            preparedStatement2.setString(2, rs.getString("stringainserita"));
            preparedStatement2.setInt(3, rs.getInt("numerazione"));
            ResultSet rs2 = preparedStatement2.executeQuery();

            String query3 = "SELECT * FROM Utente WHERE idutente = ?";
            PreparedStatement preparedStatement3 = connection.prepareStatement(query3);
            preparedStatement3.setInt(1, rs2.getInt("idutente"));
            ResultSet rsUtente = preparedStatement3.executeQuery();

            Utente utente = new Utente(rsUtente.getString("nome"), rsUtente.getString("cognome"), rsUtente.getString("login"), rsUtente.getString("password"), rsUtente.getString("email"), rsUtente.getDate("datanascita"));

            fraseProposta = new ModificaProposta(rs2.getDate("dataproposta"), rs2.getTime("oraproposta"), autore, utente, frase, rs2.getString("fraseProposta"));
            fraseProposta.setDataProposta(rs2.getDate("dataValutazione"));
            fraseProposta.setOraProposta(rs2.getTime("oravalutazione"));
            frase.addProposte(fraseProposta);
        }

        String fraseTemp = "";
        for(Frase_Corrente f : paginaSelezionata.getFrasi()){
            for(ModificaProposta fc : frase.getProposte()){
                Date dataFraseCorrente = f.getDataInserimento();
                Date dataModifica = fc.getDataValutazione();
                if(dataFraseCorrente.compareTo(dataModifica) > 0){
                    fraseTemp = f.getStringa_inserita();
                }else{
                    fraseTemp = fc.getStringa_inserita();
                }
                testoPagina = testoPagina + fraseTemp;
            }
        }
        return testoPagina;
    }
}
