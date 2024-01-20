package implementazionePostgresDAO;


import MODEL.*;
import dao.WikiDAO;
import database.ConnessioneDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        String query = "SELECT idutente, login FROM utente WHERE login = ? LIMIT 1";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        Autore autore = paginaSelezionata.getAutore();
        preparedStatement.setString(1, autore.getLogin());
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        int idAutore = rs.getInt("idutente");

        if(paginaSelezionata.getFrasi().isEmpty()) {
            query = "SELECT * FROM frasecorrente F JOIN pagina P ON F.idPagina = P.idPagina WHERE idAutore = ? AND P.titolo = ? ORDER BY f.numerazione";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idAutore);
            preparedStatement.setString(2, paginaSelezionata.getTitolo());
            rs = preparedStatement.executeQuery();

            Frase_Corrente frase = null;
            ModificaProposta fraseProposta = null;
            while (rs.next()) {
                frase = new Frase_Corrente(rs.getString("stringainserita"), rs.getInt("numerazione"), paginaSelezionata, rs.getDate("datainserimento").toLocalDate(), rs.getTime("orainserimento"));

                String queryModifica = "SELECT * FROM modificaproposta WHERE idPagina = ? AND stringainserita = ? AND numerazione = ?";
                PreparedStatement preparedStatementModifica = connection.prepareStatement(queryModifica);
                preparedStatementModifica.setInt(1, rs.getInt("idPagina"));
                preparedStatementModifica.setString(2, rs.getString("stringainserita"));
                preparedStatementModifica.setInt(3, rs.getInt("numerazione"));
                ResultSet rsModifica = preparedStatementModifica.executeQuery();
                while (rsModifica.next()) {
                    int idUtente = rsModifica.getInt("utentep");
                    String queryUtente = "SELECT * FROM Utente WHERE idutente = ?";
                    PreparedStatement preparedStatementUtente = connection.prepareStatement(queryUtente);
                    preparedStatementUtente.setInt(1, idUtente);
                    ResultSet rsUtente = preparedStatementUtente.executeQuery();
                    while (rsUtente.next()) {
                        Utente utente = new Utente(rsUtente.getString("nome"), rsUtente.getString("cognome"), rsUtente.getString("login"), rsUtente.getString("password"), rsUtente.getString("email"), rsUtente.getDate("datanascita"));
                        int stato = rsModifica.getInt("stato");
                        fraseProposta = new ModificaProposta(rsModifica.getDate("dataproposta").toLocalDate(), rsModifica.getTime("oraproposta").toLocalTime(), autore, utente, frase, rsModifica.getString("stringaProposta"), frase.getNumerazione());
                        if(stato ==  1) {
                            fraseProposta.setDataValutazione(rsModifica.getDate("dataValutazione").toLocalDate());
                            fraseProposta.setOraValutazione(rsModifica.getTime("oravalutazione").toLocalTime());
                        }
                        fraseProposta.setStato(stato);
                        //frase.addProposte(fraseProposta);
                    }

                }
            }
        }
        ArrayList<Frase> frasiTesto= new ArrayList<>();
        int controllo = 0;
        Frase fr_salvata = null;
        for(Frase_Corrente f : paginaSelezionata.getFrasi()){
            LocalDate data_max = f.getDataInserimento();
            for(ModificaProposta fc : f.getProposte()){
                if(fc.getStato() == 1) {
                    controllo = 1;
                    LocalDate dataModifica = fc.getDataValutazione();
                    if (data_max.compareTo(dataModifica) > 0) {
                        fr_salvata = f;
                    } else {
                        fr_salvata = fc;
                    }
                }
            }
            if(controllo == 0){
                frasiTesto.add(f.getNumerazione(), f);
            }else{
                frasiTesto.add(f.getNumerazione(), fr_salvata);
            }
            controllo = 0;
        }
       return frasiTesto;
    }

    public Utente verificaLoggato(String login, String password) throws SQLException {
        String query = "SELECT * FROM Utente WHERE LOWER(login) = ? AND password = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, login.toLowerCase());
        preparedStatement.setString(2, password);
        ResultSet rs = preparedStatement.executeQuery();
        Utente utenteLoggato = null;
        while(rs.next()){
            if(rs != null)
                utenteLoggato= new Utente(rs.getString("nome"), rs.getString("cognome"), login, password, rs.getString("email"), rs.getDate("dataNascita"));
        }
        return utenteLoggato;
    }

    @Override
    public void registrazione(String nome, String cognome, String nomeUtente, String password, String email, Date dataNascita) throws SQLException {
        String query = "INSERT INTO Utente (nome,cognome,login,password,email,dataNascita) VALUES (?,?,?,?,?,?)";
        PreparedStatement preparedStatement = null;
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1,nome);
        preparedStatement.setString(2,cognome);
        preparedStatement.setString(3,nomeUtente);
        preparedStatement.setString(4,password);
        preparedStatement.setString(5,email);
        long timestamp = dataNascita.getTime();
        java.sql.Date sqlDate = new java.sql.Date(timestamp);
        preparedStatement.setDate(6, sqlDate);
        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Inserimento riuscito!");
        } else {
            System.out.println("Nessuna riga inserita.");
        }
    }

    public boolean inviaProposta(Pagina paginaSelezionata, Frase_Corrente fraseSelezionata, String fraseProposta, Utente utenteLoggato) throws SQLException {
        boolean controllo = false;
        String query = "SELECT idutente, login FROM utente WHERE login = ? LIMIT 1";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, paginaSelezionata.getAutore().getLogin());
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        int idAutore = rs.getInt("idutente");
        System.out.println("idAutore = " + idAutore);

        preparedStatement.setString(1, utenteLoggato.getLogin());
        rs = preparedStatement.executeQuery();
        rs.next();
        int idUtente = rs.getInt("idUtente");
        System.out.println("idUtente = " + idUtente);

        query = "SELECT idPagina FROM pagina WHERE idAutore = ? AND titolo = ? AND dataOraCreazione = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, idAutore);
        preparedStatement.setString(2, paginaSelezionata.getTitolo());
        preparedStatement.setTimestamp(3, Timestamp.valueOf(paginaSelezionata.getDataCreazione()));
        rs = preparedStatement.executeQuery();
        rs.next();
        int idPagina = rs.getInt("idPagina");

        query = "INSERT INTO modificaProposta(stringaProposta, utenteP, autoreV, stringaInserita, numerazione, idPagina) VALUES (?,?,?,?,?,?)";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, fraseProposta);
        preparedStatement.setInt(2, idUtente);
        preparedStatement.setInt(3, idAutore);
        preparedStatement.setString(4, fraseSelezionata.getStringa_inserita());
        preparedStatement.setInt(5, fraseSelezionata.getNumerazione());
        preparedStatement.setInt(6, idPagina);

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Inserimento riuscito!");
            controllo = true;
        } else {
            System.out.println("Nessuna riga inserita.");
        }

        ModificaProposta modificaProposta = new ModificaProposta(LocalDate.now(), LocalTime.now(), paginaSelezionata.getAutore(), utenteLoggato, fraseSelezionata, fraseProposta, fraseSelezionata.getNumerazione());
        if(idAutore == idUtente){
            modificaProposta.setOraValutazione(LocalTime.now());
            modificaProposta.setDataValutazione(LocalDate.now());
            modificaProposta.setStato(1);
        }
        return controllo;
    }

}
