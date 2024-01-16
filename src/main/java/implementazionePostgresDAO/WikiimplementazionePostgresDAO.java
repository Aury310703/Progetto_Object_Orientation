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
            String query = "SELECT * FROM pagina WHERE LOWER(titolo) LIKE ?"; //LOWER TRASFORMA TITOLO IN MINUSCOLO
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + titoloInserito.toLowerCase() + "%");
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
    public ArrayList<Frase> getTestoPagina(Pagina paginaSelezionata) throws SQLException {

        String query = "SELECT idutente FROM utente WHERE login = ? LIMIT 1";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        Autore autore = paginaSelezionata.getAutore();
        preparedStatement.setString(1, autore.getLogin());
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        int idAutore = rs.getInt("idutente");

        query ="SELECT * FROM frasecorrente F JOIN pagina P ON F.idPagina = P.idPagina WHERE idAutore = ? AND P.titolo = ? ORDER BY f.numerazione";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, idAutore);
        preparedStatement.setString(2,paginaSelezionata.getTitolo());
        rs = preparedStatement.executeQuery();

        Frase_Corrente frase = null;
        ModificaProposta fraseProposta = null;
        while(rs.next()){
            System.out.println("->"+rs.getString("stringainserita")+rs.getInt("numerazione"));
            frase = new Frase_Corrente(rs.getString("stringainserita"), rs.getInt("numerazione"), paginaSelezionata, rs.getDate("datainserimento"), rs.getTime("orainserimento"));

            String queryModifica = "SELECT * FROM modificaproposta WHERE idPagina = ? AND stringainserita = ? AND numerazione = ? AND stato = 1";
            PreparedStatement preparedStatementModifica = connection.prepareStatement(queryModifica);
            preparedStatementModifica.setInt(1, rs.getInt("idPagina"));
            preparedStatementModifica.setString(2, rs.getString("stringainserita"));
            preparedStatementModifica.setInt(3, rs.getInt("numerazione"));
            ResultSet rsModifica = preparedStatementModifica.executeQuery();
            while(rsModifica.next()){
                int idUtente = rsModifica.getInt("utentep");
                String queryUtente = "SELECT * FROM Utente WHERE idutente = ?";
                PreparedStatement preparedStatementUtente = connection.prepareStatement(queryUtente);
                preparedStatementUtente.setInt(1, idUtente);
                ResultSet rsUtente = preparedStatementUtente.executeQuery();
                while(rsUtente.next()){
                    Utente utente = new Utente(rsUtente.getString("nome"), rsUtente.getString("cognome"), rsUtente.getString("login"), rsUtente.getString("password"), rsUtente.getString("email"), rsUtente.getDate("datanascita"));

                    fraseProposta = new ModificaProposta(rsModifica.getDate("dataproposta"), rsModifica.getTime("oraproposta"), autore, utente, frase, rsModifica.getString("stringaProposta"));
                    fraseProposta.setDataValutazione(rsModifica.getDate("dataValutazione"));
                    fraseProposta.setOraValutazione(rsModifica.getTime("oravalutazione"));
                    //frase.addProposte(fraseProposta);
                }

            }
        }
        ArrayList<Frase> frasiTesto= new ArrayList<>();
        int controllo = 0;
        Frase fr_salvata = null;
        for(Frase_Corrente f : paginaSelezionata.getFrasi()){
            Date data_max = f.getDataInserimento();
            for(ModificaProposta fc : f.getProposte()){
                controllo = 1;
                Date dataModifica = fc.getDataValutazione();

                if (data_max.compareTo(dataModifica) > 0) {
                   fr_salvata = f;
                } else {
                    fr_salvata = fc;
                }
            }
            if(controllo == 0){
                System.out.println("ciao1");
                frasiTesto.add(f.getNumerazione(), f);
            }else{
                System.out.println("ciao2");
                frasiTesto.add(f.getNumerazione(), fr_salvata);
            }
            controllo = 0;
        }
       return frasiTesto;
    }
}
