package implementazionePostgresDAO;

import MODEL.Autore;
import controller.Controller;
import dao.WikiDAO;
import database.ConnessioneDatabase;

import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;

public class WikiimplementazionePostgresDAO implements WikiDAO {
    private Connection connection;
    public Controller controller;

    public WikiimplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getNomeUtente(String login, String password){
        String query = "SELECT nome FROM Utente WHERE LOWER(login) = ? AND password = ?";
        PreparedStatement preparedStatement = null;
        String nome;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, login.toLowerCase());
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();
            nome = rs.getString("nome");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return nome;
    }

    public String getCognomeUtente(String login, String password){
        String query = "SELECT cognome FROM Utente WHERE LOWER(login) = ? AND password = ?";
        PreparedStatement preparedStatement = null;
        String cognome;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, login.toLowerCase());
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();
            cognome = rs.getString("cognome");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cognome;
    }

    public String getEmailUtente(String login, String password){
        String query = "SELECT email FROM Utente WHERE LOWER(login) = ? AND password = ?";
        PreparedStatement preparedStatement = null;
        String email;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, login.toLowerCase());
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();
            email = rs.getString("email");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return email;
    }

    public Date getDataNascitaUtente(String login, String password){
        String query = "SELECT dataNascita FROM Utente WHERE LOWER(login) = ? AND password = ?";
        PreparedStatement preparedStatement = null;
        Date datanascita;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, login.toLowerCase());
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();
            datanascita = rs.getDate("datanascita");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return datanascita;
    }

    public String getRuolotente(String login, String password){
        String query = "SELECT ruolo FROM Utente WHERE LOWER(login) = ? AND password = ?";
        PreparedStatement preparedStatement = null;
        String ruolo;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, login.toLowerCase());
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();
            ruolo = rs.getString("ruolo");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ruolo;
    }

    @Override
    public void ricercaTitoli(String titoloInserito, ArrayList<String> titoli, ArrayList<LocalDateTime> dateOreCreazioni, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> login, ArrayList<String> password, ArrayList<String> email, ArrayList<Date> date) throws SQLException{
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
                titoli.add(titolo);
                dateOreCreazioni.add(dataOra);
                nomi.add(rs1.getString("nome"));
                cognomi.add(rs1.getString("cognome"));
                login.add(rs1.getString("login"));
                password.add(rs1.getString("password"));
                email.add(rs1.getString("email"));
                date.add(rs1.getDate("dataNascita"));
            }
        } catch (Exception e) {
            System.out.println("Errore durante l'esecuzione della query: " + e.getMessage());
            e.printStackTrace();
        }
        connection.close();
    }

    @Override
    public void getFrasiCorrenti(String login, String titolo, LocalDateTime dataOraCreazione, ArrayList<String> frasiInserite, ArrayList<LocalDate> dateInserimento, ArrayList<Time> oreInserimento) throws SQLException {
        String query = "SELECT idutente, login FROM utente WHERE login = ? LIMIT 1";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, login);
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        int idAutore = rs.getInt("idutente");

        String quesryPagina = "SELECT idPagina FROM PAGINA WHERE idAutore = ? AND dataOraCreazione = ? AND titolo = ?";
        PreparedStatement preparedStatementPagina = connection.prepareStatement(quesryPagina);
        preparedStatementPagina.setInt(1, idAutore);
        preparedStatementPagina.setTimestamp(2, Timestamp.valueOf(dataOraCreazione));
        preparedStatementPagina.setString(3, titolo);
        ResultSet rsPagina = preparedStatementPagina.executeQuery();
        rsPagina.next();
        int idPagina = rsPagina.getInt("idPagina");

        if(frasiInserite.isEmpty()) {
            query = "SELECT * FROM frasecorrente F JOIN pagina P ON F.idPagina = P.idPagina WHERE idAutore = ? AND P.titolo = ? AND F.idPagina = ? ORDER BY f.numerazione";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idAutore);
            preparedStatement.setString(2, titolo);
            preparedStatement.setInt(3, idPagina);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                frasiInserite.add(rs.getString(rs.getString("stringaInserita")));
                dateInserimento.add(rs.getDate("dataInserimento").toLocalDate());
                oreInserimento.add(rs.getTime("orainserimento"));
            }
        }
        connection.close();
    }

    public void getModificheModificate(String login, String titolo, LocalDateTime dataOraCreazione, ArrayList<String> frasiProposte, ArrayList<LocalDate> dateProposte, ArrayList<LocalTime> oreProposte, ArrayList<LocalDate> datevalutazione, ArrayList<LocalTime> orevalutazione, ArrayList<Integer> stati, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> logins, ArrayList<String> password, ArrayList<String> email, ArrayList<Date> date) throws SQLException {
        String query = "SELECT idutente, login FROM utente WHERE login = ? LIMIT 1";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, login);
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        int idAutore = rs.getInt("idutente");

        String quesryPagina = "SELECT idPagina FROM PAGINA WHERE idAutore = ? AND dataOraCreazione = ? AND titolo = ?";
        PreparedStatement preparedStatementPagina = connection.prepareStatement(quesryPagina);
        preparedStatementPagina.setInt(1, idAutore);
        preparedStatementPagina.setTimestamp(2, Timestamp.valueOf(dataOraCreazione));
        preparedStatementPagina.setString(3, titolo);
        ResultSet rsPagina = preparedStatementPagina.executeQuery();
        rsPagina.next();
        int idPagina = rsPagina.getInt("idPagina");
        while (rs.next()) {
            String queryModifica = "SELECT * FROM modificaproposta WHERE idPagina = ? AND stringainserita = ? AND numerazione = ? ORDER BY datavalutazione ASC, oravalutazione ASC";
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
                rsUtente.next();
                    nomi.add(rsUtente.getString("nome"));
                    cognomi.add(rsUtente.getString("cognome"));
                    logins.add(rsUtente.getString("login"));
                    password.add(rsUtente.getString("password"));
                    email.add(rsUtente.getString("email"));
                    date.add(rsUtente.getDate("datanascita"));
                    stati.add(rsModifica.getInt("stato"));
                    dateProposte.add(rsModifica.getDate("dataproposta").toLocalDate());
                    oreProposte.add(rsModifica.getTime("oraproposta").toLocalTime());
                    frasiProposte.add(rsModifica.getString("stringaProposta"));
                    datevalutazione.add(rsModifica.getDate("dataValutazione").toLocalDate());
                    orevalutazione.add(rsModifica.getTime("oraValutazione").toLocalTime());
            }
        }
    }

    public boolean verificaLoggato(String nome, String cognome, String login, String password, String email, Date datNascita, String ruolo) throws SQLException {
        boolean controllo = false;
        String query = "SELECT * FROM Utente WHERE LOWER(login) = ? AND password = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, login.toLowerCase());
        preparedStatement.setString(2, password);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            controllo = true;
            nome = rs.getString("nome");
            cognome = rs.getString("cognome");
            email = rs.getString("email");
            datNascita = rs.getDate("dataNascita");
            ruolo = rs.getString("ruolo");
        }
        connection.close();
        return controllo;
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
        connection.close();
    }

    public boolean inviaProposta(int numerazione, String fraseSelezionata, String fraseProposta, String loginUtente, String loginAutore, String titolo, LocalDateTime dataOraCreazione) throws SQLException {
        boolean controllo = false;
        String query = "SELECT idutente FROM utente WHERE login = ? LIMIT 1";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, loginAutore);
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        int idAutore = rs.getInt("idutente");
        System.out.println("idAutore = " + idAutore);

        preparedStatement.setString(1, loginUtente);
        rs = preparedStatement.executeQuery();
        rs.next();
        int idUtente = rs.getInt("idUtente");
        System.out.println("idUtente = " + idUtente);

        query = "SELECT idPagina FROM pagina WHERE idAutore = ? AND titolo = ? AND dataOraCreazione = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, idAutore);
        preparedStatement.setString(2, titolo);
        preparedStatement.setTimestamp(3, Timestamp.valueOf(dataOraCreazione));
        rs = preparedStatement.executeQuery();
        rs.next();
        int idPagina = rs.getInt("idPagina");

        query = "INSERT INTO modificaProposta(stringaProposta, utenteP, autoreV, stringaInserita, numerazione, idPagina) VALUES (?,?,?,?,?,?)";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, fraseProposta);
        preparedStatement.setInt(2, idUtente);
        preparedStatement.setInt(3, idAutore);
        preparedStatement.setString(4, fraseSelezionata);
        preparedStatement.setInt(5, numerazione);
        preparedStatement.setInt(6, idPagina);

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Inserimento riuscito!");
            controllo = true;
        } else {
            System.out.println("Nessuna riga inserita.");
        }
        connection.close();
        return controllo;
    }

    public void creazionePagina(Pagina paginaCreata) throws SQLException{
        String queryPagina = "INSERT INTO Pagina (titolo, dataOraCreazione, idAutore) VALUES (?,?,?)";
        PreparedStatement preparedStatementPagina = null;
        preparedStatementPagina = connection.prepareStatement(queryPagina);
        preparedStatementPagina.setString(1,paginaCreata.getTitolo());
        preparedStatementPagina.setTimestamp(2, Timestamp.valueOf(paginaCreata.getDataCreazione()));

        String query = "SELECT idutente, login FROM utente WHERE login = ? LIMIT 1";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, paginaCreata.getAutore().getLogin());
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        int idAutore = rs.getInt("idutente");
        preparedStatementPagina.setInt(3,idAutore);
        int rowsAffected = preparedStatementPagina.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Inserimento riuscito!");
        } else {
            System.out.println("Nessuna riga inserita.");
        }

        query = "SELECT idPagina FROM pagina WHERE idAutore = ? AND titolo = ? AND dataOraCreazione = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, idAutore);
        preparedStatement.setString(2, paginaCreata.getTitolo());
        preparedStatement.setTimestamp(3, Timestamp.valueOf(paginaCreata.getDataCreazione()));
        rs = preparedStatement.executeQuery();
        rs.next();
        int idPagina = rs.getInt("idPagina");

        for (Frase_Corrente fraseCorrente : paginaCreata.getFrasi()){
            String queryFrase = "INSERT INTO fraseCorrente (stringaInserita, numerazione, datainserimento, orainserimento, idPagina) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatementFrase = null;
            preparedStatementFrase = connection.prepareStatement(queryFrase);
            preparedStatementFrase.setString(1,fraseCorrente.getStringa_inserita());
            preparedStatementFrase.setInt(2,fraseCorrente.getNumerazione());
            preparedStatementFrase.setDate(3, java.sql.Date.valueOf(fraseCorrente.getDataInserimento()));
            preparedStatementFrase.setTime(4,fraseCorrente.getOraInserimento());
            preparedStatementFrase.setInt(5, idPagina);
            int rowsAffectedFrase = preparedStatementFrase.executeUpdate();

            if (rowsAffectedFrase > 0) {
                System.out.println("Inserimento riuscito!");
            } else {
                System.out.println("Nessuna riga inserita.");
            }
        }
        connection.close();
    }

    public void addPaginaVisualizzata(String titolo, LocalDateTime DataOraCreazione, String loginAutorePagina, String loginUtenteViusalizzatore) throws SQLException{
        String quesryPagina = "SELECT * FROM PAGINA WHERE titolo = ? AND dataOraCreazione = ? AND idAutore = ?";
        PreparedStatement preparedStatementPagina = connection.prepareStatement(quesryPagina);
        preparedStatementPagina.setString(1, titolo);
        preparedStatementPagina.setTimestamp(2, Timestamp.valueOf(DataOraCreazione));

        String queryAutore = "SELECT idutente FROM UTENTE WHERE login = ? LIMIT 1";
        PreparedStatement preparedStatementAutore = connection.prepareStatement(queryAutore);
        preparedStatementAutore.setString(1,loginAutorePagina);
        ResultSet rsAutore = preparedStatementAutore.executeQuery();
        rsAutore.next();
        int idAutore = rsAutore.getInt("idutente");

        preparedStatementPagina.setInt(3, idAutore);
        ResultSet rsPagina = preparedStatementPagina.executeQuery();
        rsPagina.next();
        int idPagina = rsPagina.getInt("idPagina");

        String queryUtente = "SELECT idutente FROM UTENTE WHERE login = ? LIMIT 1";
        PreparedStatement preparedStatementUtente = connection.prepareStatement(queryUtente);
        preparedStatementUtente.setString(1, loginUtenteViusalizzatore));
        ResultSet rsUtente = preparedStatementUtente.executeQuery();
        rsUtente.next();
        int idUtente = rsUtente.getInt("idutente");

        String queryVisona = "INSERT INTO VISIONA(idPagina, idUtente) VALUES (?,?)";
        PreparedStatement preparedStatementVisona = connection.prepareStatement(queryVisona);
        preparedStatementVisona.setInt(1, idPagina);
        preparedStatementVisona.setInt(2,idUtente);

        int rowsAffectedFrase = preparedStatementVisona.executeUpdate();

        if (rowsAffectedFrase > 0) {
            System.out.println("Inserimento riuscito!");
        } else {
            System.out.println("Nessuna riga inserita.");
        }
        connection.close();
    }


    public void storicoPagineVisualizzate(String loginUtente, ArrayList<String> titoli, ArrayList<LocalDateTime> dateOreCreazioni, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> nomiUtente, ArrayList<String> password, ArrayList<String> email, ArrayList<Date> dataNascita, ArrayList<LocalDate> dateVisioni, ArrayList<LocalTime> oreVisioni) throws SQLException{
        ArrayList<String> pagineVisualizzate = new ArrayList<>();

        try {
            String queryUtente = "SELECT idutente FROM utente WHERE login = ? LIMIT 1";
            PreparedStatement preparedStatementUtente = connection.prepareStatement(queryUtente);
            preparedStatementUtente.setString(1, loginUtente);
            ResultSet rsUtente = preparedStatementUtente.executeQuery();
            rsUtente.next();
            int idUtente = rsUtente.getInt("idutente");
            System.out.println("idAutore = " + idUtente);

            String query = "SELECT * FROM visiona WHERE idUtente = ? ORDER BY dataVisone DESC, oraVisione DESC";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idUtente);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.wasNull()){
                System.out.println("no");
            }
            while (rs.next()) {
                String quesryPagina = "SELECT * FROM PAGINA WHERE idPagina = ?";
                PreparedStatement preparedStatementPagina = connection.prepareStatement(quesryPagina);
                preparedStatementPagina.setInt(1, rs.getInt("idPagina"));
                ResultSet rsPagina = preparedStatementPagina.executeQuery();
                rsPagina.next();
                String queryAutore = "SELECT * FROM utente WHERE idUtente = ? GROUP BY idUtente LIMIT 1";
                PreparedStatement preparedStatementAutore = connection.prepareStatement(queryAutore);
                preparedStatementAutore.setInt(1, rsPagina.getInt("idAutore"));
                ResultSet rsAutore = preparedStatementAutore.executeQuery();
                rsAutore.next();
                titoli.add(rsPagina.getString("titolo"));
                dateOreCreazioni.add(rsPagina.getTimestamp("dataOraCreazione").toLocalDateTime());
                nomi.add(rsAutore.getString("nome"));
                cognomi.add(rsAutore.getString("cognome"));
                nomiUtente.add(rsAutore.getString("login"));
                password.add(rsAutore.getString("password"));
                dataNascita.add(rsAutore.getDate("dataNascita"));
                dateVisioni.add(rs.getDate("dataVisione").toLocalDate());
                oreVisioni.add(rs.getTime("oraVisione").toLocalTime());
            }
        } catch (Exception e) {
            System.out.println("Errore durante l'esecuzione della query: " + e.getMessage());
            e.printStackTrace();
        }
        connection.close();
    }

    @Override
    public void getModificate(String login, ArrayList<String> titolo,ArrayList<LocalDateTime> dataOraCreazione, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> nomiUtente, ArrayList<String> password, ArrayList<String> email,  ArrayList<Date> dataNascita, ArrayList<LocalDate> dataProposta, ArrayList<LocalTime> oraProposta , ArrayList<String> stringaInserita, ArrayList<Integer> numerazione, ArrayList<Integer> stato, ArrayList<String> stringaProposta, ArrayList<LocalDate> dataValutazione, ArrayList<LocalTime> oraValutazione, ArrayList<LocalDate>  dataInserimento, ArrayList<Time> oraInseriento) throws SQLException {
        try{
            String query = "SELECT * FROM Pagina p JOIN ModificaProposta m ON p.idPagina = m.idPagina WHERE utentep = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            String queryUtente = "SELECT idutente FROM utente WHERE login = ? LIMIT 1";
            PreparedStatement preparedStatementUtente = connection.prepareStatement(queryUtente);
            preparedStatementUtente.setString(1, login);
            ResultSet rsUtente = preparedStatementUtente.executeQuery();
            rsUtente.next();
            int idUtente = rsUtente.getInt("idutente");
            preparedStatement.setInt(1,idUtente);
            ResultSet rsPagina = preparedStatement.executeQuery();
            while (rsPagina.next()){
                String queryAutore = "SELECT * FROM utente WHERE idUtente = ? GROUP BY idUtente LIMIT 1";
                PreparedStatement preparedStatementAutore = connection.prepareStatement(queryAutore);
                preparedStatementAutore.setInt(1, rsPagina.getInt("idAutore"));;
                ResultSet rsAutore = preparedStatementAutore.executeQuery();
                rsAutore.next();
                titolo.add(rsPagina.getString("titolo"));
                dataOraCreazione.add(rsPagina.getTimestamp("dataOraCreazione").toLocalDateTime());
                nomi.add(rsAutore.getString("nome"));
                cognomi.add(rsAutore.getString("cognome"));
                nomiUtente.add(rsAutore.getString("login"));
                password.add(rsAutore.getString("password"));
                email.add(rsAutore.getString("email"));
                dataNascita.add(rsAutore.getDate("dataNascita"));
                stringaProposta.add(rsPagina.getString("stringaProposta"));
                dataProposta.add(rsPagina.getDate("dataProposta").toLocalDate());
                oraProposta.add(rsPagina.getTime("oraProposta").toLocalTime());
                dataValutazione.add(rsPagina.getDate("dataValutazione").toLocalDate());
                oraValutazione.add(rsPagina.getTime("oraValutazione").toLocalTime());
                String queryFraseCorrente = "SELECT * FROM Frasecorrente WHERE idPagina = ? AND stringaInserita = ? AND numerazione = ?";
                PreparedStatement preparedStatementFraseCorrente = connection.prepareStatement(queryFraseCorrente);
                preparedStatementFraseCorrente.setInt(1, rsPagina.getInt("idPagina"));
                preparedStatementFraseCorrente.setString(2, rsPagina.getString("stringaInserita"));
                preparedStatementFraseCorrente.setInt(3, rsPagina.getInt("numerazione"));
                ResultSet rsFraseCorrente = preparedStatementFraseCorrente.executeQuery();
                rsFraseCorrente.next();
                stringaInserita.add(rsFraseCorrente.getString("stringaInserita"));
                numerazione.add(rsFraseCorrente.getInt("numerazione"));
                stato.add(rsFraseCorrente.getInt("stato"));
            }
        }catch (Exception e){
            System.out.println("Errore durante l'esecuzione della query: " + e.getMessage());
            e.printStackTrace();
        }
        connection.close();
    }

    public ArrayList<ModificaProposta> getProposte(Pagina paginaSelezionata, Utente utenteLoggato) throws SQLException{
        ArrayList<ModificaProposta> modifiche = new ArrayList<>();

        String queryAutore = "SELECT idutente FROM utente WHERE login = ? LIMIT 1";
        PreparedStatement preparedStatementAutore = connection.prepareStatement(queryAutore);
        preparedStatementAutore.setString(1, paginaSelezionata.getAutore().getLogin());
        ResultSet rsAutore = preparedStatementAutore.executeQuery();
        rsAutore.next();
        int idAutore = rsAutore.getInt("idutente");

        String queryPagina = "SELECT idPagina FROM pagina WHERE titolo = ? AND dataOraCreazione = ? AND idAutore = ?";
        PreparedStatement preparedStatementPagina = connection.prepareStatement(queryPagina);
        preparedStatementPagina.setString(1, paginaSelezionata.getTitolo());
        preparedStatementPagina.setTimestamp(2, Timestamp.valueOf(paginaSelezionata.getDataCreazione()));
        preparedStatementPagina.setInt(3, idAutore);
        ResultSet rsPagina = preparedStatementPagina.executeQuery();
        rsPagina.next();
        int idPagina = rsPagina.getInt("idPagina");

        String queryUtente = "SELECT idutente FROM utente WHERE login = ? LIMIT 1";
        PreparedStatement preparedStatementUtente = connection.prepareStatement(queryUtente);
        preparedStatementUtente.setString(1,utenteLoggato.getLogin());
        ResultSet rsUtente = preparedStatementUtente.executeQuery();
        rsUtente.next();
        int idUtente = rsUtente.getInt("idutente");

        String queryModifiche = "SELECT * FROM modificaProposta WHERE autorev = ? AND utentep = ? AND idPagina = ? ORDER BY DataProposta DESC, oraProposta DESC";
        PreparedStatement preparedStatementModiche = connection.prepareStatement(queryModifiche);
        preparedStatementModiche.setInt(1, idAutore);
        preparedStatementModiche.setInt(2, idUtente);
        preparedStatementModiche.setInt(3, idPagina);
        ResultSet rsModifiche = preparedStatementModiche.executeQuery();

        while(rsModifiche.next()){
            String queryFraseCorrente = "SELECT * FROM modificaProposta WHERE autorev = ? AND idPagina = ? AND numerazione = ? AND datavalutazione <= ? AND oravalutazione < ? AND stato = 1 ORDER BY datavalutazione DESC, oravalutazione DESC LIMIT 1";
            PreparedStatement preparedStatementFraseCorrente = connection.prepareStatement(queryFraseCorrente);
            preparedStatementFraseCorrente.setInt(1, idAutore);
            preparedStatementFraseCorrente.setInt(2, idPagina);
            preparedStatementFraseCorrente.setInt(3, rsModifiche.getInt("numerazione"));
            preparedStatementFraseCorrente.setDate(4, rsModifiche.getDate("dataProposta"));
            preparedStatementFraseCorrente.setTime(5, rsModifiche.getTime("oraProposta"));
            ResultSet rsFraseCorrente = preparedStatementFraseCorrente.executeQuery();
            Frase_Corrente fraseCorrente = null;
            if(rsFraseCorrente.next()){
                System.out.println("entra");
                System.out.println("yeee");
                fraseCorrente = new Frase_Corrente(rsFraseCorrente.getString("stringaproposta"), rsFraseCorrente.getInt("numerazione"), paginaSelezionata, rsFraseCorrente.getDate("datavalutazione").toLocalDate(), rsFraseCorrente.getTime("oraValutazione"));
                System.out.println("-----------" + fraseCorrente.getStringa_inserita());
            }else {
                System.out.println("non entra");
                queryFraseCorrente = "SELECT stringaInserita, numerazione, dataInserimento, oraInserimento FROM FraseCorrente WHERE idPagina = ? AND stringaInserita = ? AND numerazione = ?";
                preparedStatementFraseCorrente = connection.prepareStatement(queryFraseCorrente);
                preparedStatementFraseCorrente.setInt(1, idPagina);
                preparedStatementFraseCorrente.setString(2, rsModifiche.getString("stringaInserita"));
                preparedStatementFraseCorrente.setInt(3, rsModifiche.getInt("numerazione"));
                rsFraseCorrente = preparedStatementFraseCorrente.executeQuery();
                rsFraseCorrente.next();
                fraseCorrente = new Frase_Corrente(rsFraseCorrente.getString("stringainserita"), rsFraseCorrente.getInt("numerazione"), paginaSelezionata, rsFraseCorrente.getDate("datainserimento").toLocalDate(), rsFraseCorrente.getTime("orainserimento"));
                System.out.println("++++++++++++" + fraseCorrente.getStringa_inserita());

            }

            ModificaProposta modificaProposta = new ModificaProposta(rsModifiche.getDate("dataProposta").toLocalDate(), rsModifiche.getTime("oraProposta").toLocalTime(), paginaSelezionata.getAutore(), utenteLoggato, fraseCorrente, rsModifiche.getString("StringaProposta"), rsModifiche.getInt("numerazione"), rsModifiche.getInt("stato"));
            if(modificaProposta.getStato() != 0) {
                modificaProposta.setDataValutazione(rsModifiche.getDate("dataValutazione").toLocalDate());
                modificaProposta.setOraProposta(rsModifiche.getTime("oraValutazione").toLocalTime());
            }
            modifiche.add(modificaProposta);
        }
        connection.close();
        return modifiche;
    }

    public ArrayList<Pagina> storicoPagineCreate(Autore autoreLoggato) throws SQLException{

        ArrayList<Pagina> PagineCreate = new ArrayList<>();

        try {
            String queryUtente = "SELECT idutente FROM utente WHERE login = ? LIMIT 1";
            PreparedStatement preparedStatementUtente = connection.prepareStatement(queryUtente);
            preparedStatementUtente.setString(1,autoreLoggato.getLogin());
            ResultSet rsUtente = preparedStatementUtente.executeQuery();
            rsUtente.next();
            int idAutore = rsUtente.getInt("idutente");

            String queryPagina = "SELECT * FROM pagina WHERE idAutore = ? ORDER BY titolo ASC";
            PreparedStatement preparedStatementPagina = connection.prepareStatement(queryPagina);
            preparedStatementPagina.setInt(1, idAutore);
            ResultSet rsPagina = preparedStatementPagina.executeQuery();

            while (rsPagina.next()) {
                String titolo = rsPagina.getString("titolo");
                LocalDateTime dataOra = rsPagina.getTimestamp("dataOraCreazione").toLocalDateTime();
                Pagina pagina = new Pagina(titolo, dataOra, autoreLoggato);
                PagineCreate.add(pagina);
                System.out.println(pagina.getTitolo());
            }
        } catch (Exception e) {
            System.out.println("Errore durante l'esecuzione della query: " + e.getMessage());
            e.printStackTrace();
        }
        connection.close();
        return PagineCreate;
    }

    public boolean controllaNotifiche(String login) throws SQLException{
        boolean notificheRicevute = false;
        String queryNotifiche = "SELECT * FROM modificaProposta M NATURAL JOIN notifica N WHERE M.autorev = ?  AND M.stato = 0 ORDER BY N.data ASC, N.ora ASC";
        PreparedStatement preparedStatementNotifiche = connection.prepareStatement(queryNotifiche);

        String queryAutore = "SELECT idutente FROM utente WHERE login = ? LIMIT 1";
        PreparedStatement preparedStatementAutore = connection.prepareStatement(queryAutore);
        preparedStatementAutore.setString(1,login);
        ResultSet rsAutore = preparedStatementAutore.executeQuery();
        rsAutore.next();
        int idAutore = rsAutore.getInt("idutente");

        preparedStatementNotifiche.setInt(1, idAutore);
        ResultSet rsNotifiche = preparedStatementNotifiche.executeQuery();
        while(rsNotifiche.next()){
            String queryUtente = "SELECT * FROM utente WHERE idUtente = ? LIMIT 1";
            PreparedStatement preparedStatementUtente = connection.prepareStatement(queryUtente);
            preparedStatementUtente.setInt(1,rsNotifiche.getInt("utentep"));
            ResultSet rsUtente = preparedStatementUtente.executeQuery();
            rsUtente.next();

            String queryModifiche = "SELECT * FROM modificaProposta WHERE idModifica = ?";
            PreparedStatement preparedStatementModiche = connection.prepareStatement(queryModifiche);
            preparedStatementModiche.setInt(1, rsNotifiche.getInt("idModifica"));
            ResultSet rsModifiche = preparedStatementModiche.executeQuery();

            String queryPagina = "SELECT * FROM pagina WHERE idPagina = ?";
            PreparedStatement preparedStatementPagina = connection.prepareStatement(queryPagina);
            preparedStatementPagina.setInt(1, rsNotifiche.getInt("idPagina"));
            ResultSet rsPagina = preparedStatementPagina.executeQuery();
            rsPagina.next();

            Pagina pagina = new Pagina(rsPagina.getString("titolo"), rsPagina.getTimestamp("dataOraCreazione").toLocalDateTime(), autoreLoggato);

            while(rsModifiche.next()) {
                String queryFraseCorrente = "SELECT * FROM modificaProposta WHERE autorev = ? AND idPagina = ? AND numerazione = ? AND datavalutazione <= ? AND oravalutazione < ? AND stato = 1 ORDER BY datavalutazione DESC, oravalutazione DESC LIMIT 1";
                PreparedStatement preparedStatementFraseCorrente = connection.prepareStatement(queryFraseCorrente);
                preparedStatementFraseCorrente.setInt(1, idAutore);
                preparedStatementFraseCorrente.setInt(2, rsNotifiche.getInt("idPagina"));
                preparedStatementFraseCorrente.setInt(3, rsModifiche.getInt("numerazione"));
                preparedStatementFraseCorrente.setDate(4, rsModifiche.getDate("dataProposta"));
                preparedStatementFraseCorrente.setTime(5, rsModifiche.getTime("oraProposta"));
                ResultSet rsFraseCorrente = preparedStatementFraseCorrente.executeQuery();
                Frase_Corrente fraseCorrente = null;
                if (rsFraseCorrente.next()) {
                    System.out.println("entra");
                    System.out.println("yeee");
                    fraseCorrente = new Frase_Corrente(rsFraseCorrente.getString("stringaproposta"), rsFraseCorrente.getInt("numerazione"), pagina, rsFraseCorrente.getDate("datavalutazione").toLocalDate(), rsFraseCorrente.getTime("oraValutazione"));
                    System.out.println("-----------" + fraseCorrente.getStringa_inserita());
                } else {
                    System.out.println("non entra");
                    queryFraseCorrente = "SELECT stringaInserita, numerazione, dataInserimento, oraInserimento FROM FraseCorrente WHERE idPagina = ? AND stringaInserita = ? AND numerazione = ?";
                    preparedStatementFraseCorrente = connection.prepareStatement(queryFraseCorrente);
                    preparedStatementFraseCorrente.setInt(1, rsNotifiche.getInt("idPagina"));
                    preparedStatementFraseCorrente.setString(2, rsModifiche.getString("stringaInserita"));
                    preparedStatementFraseCorrente.setInt(3, rsModifiche.getInt("numerazione"));
                    rsFraseCorrente = preparedStatementFraseCorrente.executeQuery();
                    rsFraseCorrente.next();
                    fraseCorrente = new Frase_Corrente(rsFraseCorrente.getString("stringainserita"), rsFraseCorrente.getInt("numerazione"), pagina, rsFraseCorrente.getDate("datainserimento").toLocalDate(), rsFraseCorrente.getTime("orainserimento"));
                    System.out.println("++++++++++++" + fraseCorrente.getStringa_inserita());

                }

                Utente utente = new Utente(rsUtente.getString("nome"), rsUtente.getString("cognome"), rsUtente.getString("login"), rsUtente.getString("password"), rsUtente.getString("email"), rsUtente.getDate("datanascita"));
                ModificaProposta modificaProposta = new ModificaProposta(rsNotifiche.getDate("dataProposta").toLocalDate(), rsNotifiche.getTime("oraproposta").toLocalTime(), autoreLoggato, utente, fraseCorrente, rsNotifiche.getString("stringaProposta"), rsNotifiche.getInt("numerazione"));
                Notifica notifica = new Notifica(autoreLoggato, modificaProposta, rsPagina.getString("titolo"));
                notifica.setDataInvio(rsNotifiche.getDate("data").toLocalDate());
                notifica.setOraInvio(rsNotifiche.getTimestamp("ora"));
                System.out.println("---------------" + notifica.getModifica().getOraProposta());
            }
        }

        if(!(autoreLoggato.getNotificheRicevute().isEmpty()))
            notificheRicevute = true;
        return notificheRicevute;
    }

    public boolean aggiornaStato(ArrayList<Notifica> notifiche, int cambiaStato) throws SQLException{
        boolean controllo = false;
        String queryNotifica = "SELECT idModifica FROM Notifica WHERE idAutore = ? AND data = ? AND ora = ? AND titolo = ?";
        PreparedStatement preparedStatementNotifica = connection.prepareStatement(queryNotifica);

        String queryAutore = "SELECT idutente FROM utente WHERE login = ? LIMIT 1";
        PreparedStatement preparedStatementAutore = connection.prepareStatement(queryAutore);
        Notifica notificaCorrente = notifiche.get(0);
        preparedStatementAutore.setString(1,notificaCorrente.getAutore().getLogin());
        ResultSet rsAutore = preparedStatementAutore.executeQuery();
        rsAutore.next();
        int idAutore = rsAutore.getInt("idutente");


        preparedStatementNotifica.setInt(1, idAutore);
        System.out.println("idAutore= " + idAutore);
        System.out.println("data = " + notificaCorrente.getDataInvio());
        System.out.println("ora = " + notificaCorrente.getOraInvio());
        preparedStatementNotifica.setDate(2, java.sql.Date.valueOf(notificaCorrente.getDataInvio()));
        preparedStatementNotifica.setTimestamp(3, notificaCorrente.getOraInvio());
        preparedStatementNotifica.setString(4, notificaCorrente.getTitolo());
        ResultSet rsNotifica = preparedStatementNotifica.executeQuery();
        if(rsNotifica.next()) {
            int idModifica = rsNotifica.getInt("idModifica");
            String queryUpdateModifica = "UPDATE modificaProposta SET stato = ? WHERE idmodifica = ?";
            PreparedStatement preparedStatementUpdateModifica = connection.prepareStatement(queryUpdateModifica);
            preparedStatementUpdateModifica.setInt(1, cambiaStato);
            preparedStatementUpdateModifica.setInt(2, idModifica);


            int rowsAffected = preparedStatementUpdateModifica.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Aggiornamento riuscito!");
//            notificaCorrente.getAutore().getNotificheRicevute().remove(0);
                controllo = true;
            } else {
                System.out.println("Nessuna riga aggiornata.");
            }
        }else{
            System.out.println("vuoto ");
        }

        connection.close();
        return controllo;
    }

}