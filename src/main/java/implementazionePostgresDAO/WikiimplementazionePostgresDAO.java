package implementazionePostgresDAO;

import controller.Controller;
import dao.WikiDAO;
import database.ConnessioneDatabase;

import java.sql.*;
import java.time.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;


public class WikiimplementazionePostgresDAO implements WikiDAO {
    private Connection connection;
    public Controller controller;

    /**
     * Instantiates a new Wikiimplementazione postgres dao.
     */
    public WikiimplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getNomeUtente(String login){
        String query = "SELECT nome FROM Utente WHERE LOWER(login) = ?";
        PreparedStatement preparedStatement = null;
        String nome = "";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, login.toLowerCase());
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                nome = rs.getString("nome");
                return nome;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {

        }
        return nome;
    }

    public String getCognomeUtente(String login){
        String query = "SELECT cognome FROM Utente WHERE LOWER(login) = ?";
        PreparedStatement preparedStatement = null;
        String cognome = "";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, login.toLowerCase());
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
                cognome = rs.getString("cognome");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cognome;
    }

    public String getEmailUtente(String login){
        String query = "SELECT email FROM Utente WHERE LOWER(login) = ?";
        PreparedStatement preparedStatement = null;
        String email = "";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, login.toLowerCase());

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
                email = rs.getString("email");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return email;
    }

    public Date getDataNascitaUtente(String login){
        String query = "SELECT dataNascita FROM Utente WHERE LOWER(login) = ?";
        PreparedStatement preparedStatement = null;
        Date datanascita = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, login.toLowerCase());
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
                datanascita = rs.getDate("datanascita");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return datanascita;
    }

    public String getRuolotente(String login){
        String query = "SELECT ruolo FROM Utente WHERE LOWER(login) = ?";
        PreparedStatement preparedStatement = null;
        String ruolo = "";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, login.toLowerCase());
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
                ruolo = rs.getString("ruolo");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ruolo;
    }

    public void getPagineCreate(String login, ArrayList<String> titoli, ArrayList<LocalDateTime> dataOraCreazione) throws SQLException{
        String query = "SELECT * FROM Utente WHERE LOWER(login) = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, login.toLowerCase());
        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next()) {
            int idAutore = rs.getInt("idutente");

            String querypagine = "SELECT titolo, dataOraCreazione FROM PAGINA WHERE idAutore = ?";
            PreparedStatement preparedStatementPagine = connection.prepareStatement(querypagine);
            preparedStatementPagine.setInt(1, idAutore);
            ResultSet rsPagine = preparedStatementPagine.executeQuery();
            while (rsPagine.next()) {
                titoli.add(rsPagine.getString("titolo"));
                dataOraCreazione.add(rsPagine.getTimestamp("dataOraCreazione").toLocalDateTime());
            }
        }
    }

    @Override
    public void ricercaTitoli(String titoloInserito, ArrayList<String> titoli, ArrayList<LocalDateTime> dateOreCreazioni, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> login, ArrayList<String> password, ArrayList<String> email, ArrayList<Date> date) throws SQLException{
        try {
            if(titoloInserito == null){
                titoloInserito = "";
            }
            String query = "SELECT * FROM pagina WHERE LOWER(titolo) LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + titoloInserito.toLowerCase() + "%");
            ResultSet rs = preparedStatement.executeQuery();

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
    public void getFrasiCorrenti(String login, String titolo, LocalDateTime dataOraCreazione, ArrayList<String> frasiInserite, ArrayList<LocalDate> dateInserimento, ArrayList<Time> oreInserimento, ArrayList<String> titoloCollegata, ArrayList<LocalDateTime> dataOraCreazioneCollegata, ArrayList<String> nomiCollegata, ArrayList<String> cognomiCollegata, ArrayList<String> loginCollegata, ArrayList<String> passwordCollegata, ArrayList<String> emailCollegata, ArrayList<Date> dateCollegata, ArrayList<Integer> numerazioneCollegata) throws SQLException {
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
            int i = 0;
            while (rs.next()) {
                frasiInserite.add(rs.getString("stringaInserita"));
                dateInserimento.add(rs.getDate("dataInserimento").toLocalDate());
                oreInserimento.add(rs.getTime("orainserimento"));
                String queryCollegamento = "SELECT paginaCollegata FROM Collegamento WHERE idPagina = ? AND numerazione = ? AND stringaInserita = ?";
                PreparedStatement preparedStatementCollegamento = connection.prepareStatement(queryCollegamento);
                preparedStatementCollegamento.setInt(1, rs.getInt("idPagina"));
                preparedStatementCollegamento.setInt(2, rs.getInt("numerazione"));
                preparedStatementCollegamento.setString(3, rs.getString("stringaInserita"));
                ResultSet rsCollegamento = preparedStatementCollegamento.executeQuery();
                if(rsCollegamento.next()){
                    String queryPaginaCollegata = "SELECT * FROM PAGINA JOIN UTENTE ON idAutore = idUtente WHERE idPagina = ?";
                    PreparedStatement prepareStatementPaginaCollegata = connection.prepareStatement(queryPaginaCollegata);
                    prepareStatementPaginaCollegata.setInt(1, rsCollegamento.getInt("paginaCollegata"));
                    ResultSet rsPaginaCollegata = prepareStatementPaginaCollegata.executeQuery();
                    rsPaginaCollegata.next();
                    titoloCollegata.add(rsPaginaCollegata.getString("titolo"));
                    dataOraCreazioneCollegata.add(rsPaginaCollegata.getTimestamp("dataOraCreazione").toLocalDateTime());
                    nomiCollegata.add(rsPaginaCollegata.getString("nome"));
                    cognomiCollegata.add(rsPaginaCollegata.getString("cognome"));
                    loginCollegata.add(rsPaginaCollegata.getString("login"));
                    passwordCollegata.add(rsPaginaCollegata.getString("password"));
                    emailCollegata.add(rsPaginaCollegata.getString("email"));
                    dateCollegata.add(rsPaginaCollegata.getDate("datanascita"));
                    numerazioneCollegata.add(i);
                }
                i++;
            }
        }
        connection.close();
    }

    public void getModificheModificate(String login, String titolo, LocalDateTime dataOraCreazione, String stringaInserita, int numerazione, ArrayList<String> frasiProposte, ArrayList<LocalDate> dateProposte, ArrayList<LocalTime> oreProposte, ArrayList<Optional<LocalDate>> datavalutazione, ArrayList<Optional<LocalTime>> orevalutazione, ArrayList<Integer> stati, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> logins, ArrayList<String> password, ArrayList<String> email, ArrayList<Date> date) throws SQLException {
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
        while (rsPagina.next()) {
            String queryModifica = "SELECT * FROM modificaproposta WHERE idPagina = ? AND stringainserita = ? AND numerazione = ? ORDER BY datavalutazione ASC, oravalutazione ASC";
            PreparedStatement preparedStatementModifica = connection.prepareStatement(queryModifica);
            preparedStatementModifica.setInt(1, rsPagina.getInt("idPagina"));
            preparedStatementModifica.setString(2, stringaInserita);
            preparedStatementModifica.setInt(3, numerazione);
            ResultSet rsModifica = preparedStatementModifica.executeQuery();

            while (rsModifica.next()) {
                int idUtente = rsModifica.getInt("utentep");
                String queryUtente = "SELECT * FROM Utente WHERE idutente = ?";
                PreparedStatement preparedStatementUtente = connection.prepareStatement(queryUtente);
                preparedStatementUtente.setInt(1, idUtente);
                ResultSet rsUtente = preparedStatementUtente.executeQuery();
                rsUtente.next();
                LocalDate dataValutazione = rsModifica.getDate("dataValutazione") != null ?
                        rsModifica.getDate("dataValutazione").toLocalDate() : null;

                LocalTime oraValutazione = rsModifica.getTime("oraValutazione") != null ?
                        rsModifica.getTime("oraValutazione").toLocalTime() : null;

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
                datavalutazione.add(Optional.ofNullable(dataValutazione));
                orevalutazione.add(Optional.ofNullable(oraValutazione));
            }
        }
    }

    public boolean verificaLoggato(String login, String password) throws SQLException {
        boolean controllo = false;
        String query = "SELECT * FROM Utente WHERE LOWER(login) = ? AND password = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, login.toLowerCase());
        preparedStatement.setString(2, password);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            controllo = true;
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
        preparedStatement.executeUpdate();
        connection.close();
    }

    public boolean inviaProposta(int numerazione, String fraseSelezionata, String fraseProposta, String loginUtente, String loginAutore, String titolo, LocalDateTime dataOraCreazione) throws SQLException {
        boolean controllo = false;
        String query = "SELECT idutente FROM utente WHERE LOWER(login) = ? LIMIT 1";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, loginAutore.toLowerCase());
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        int idAutore = rs.getInt("idutente");

        preparedStatement.setString(1, loginUtente);
        rs = preparedStatement.executeQuery();
        rs.next();
        int idUtente = rs.getInt("idUtente");

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
        preparedStatement.executeUpdate();

        connection.close();
        return controllo;
    }


    public void creazionePagina(String titolo, ArrayList<String> frasi, String login, LocalDateTime dataCreazione) throws SQLException{
        String queryPagina = "INSERT INTO Pagina (titolo, idAutore, dataoracreazione) VALUES (?,?,?)";
        PreparedStatement preparedStatementPagina = null;
        preparedStatementPagina = connection.prepareStatement(queryPagina);
        preparedStatementPagina.setString(1, titolo);

        String query = "SELECT idutente, login FROM utente WHERE login = ? LIMIT 1";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, login);
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        int idAutore = rs.getInt("idutente");
        preparedStatementPagina.setInt(2,idAutore);
        preparedStatementPagina.setTimestamp(3, Timestamp.valueOf(dataCreazione));
        preparedStatementPagina.executeUpdate();

        query = "SELECT idPagina FROM pagina WHERE idAutore = ? AND titolo = ? LIMIT 1";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, idAutore);
        preparedStatement.setString(2, titolo);
        rs = preparedStatement.executeQuery();
        rs.next();
        int idPagina = rs.getInt("idPagina");

        int num = 0;
        for (String f : frasi){
            String queryFrase = "INSERT INTO fraseCorrente (stringaInserita, numerazione, idPagina) VALUES (?,?,?)";
            PreparedStatement preparedStatementFrase = null;
            preparedStatementFrase = connection.prepareStatement(queryFrase);
            preparedStatementFrase.setString(1, f);
            preparedStatementFrase.setInt(2, num);
            preparedStatementFrase.setInt(3, idPagina);
            preparedStatementFrase.executeUpdate();
            num++;
        }
        connection.close();
    }

    public void addPaginaVisualizzata(String titolo, LocalDateTime DataOraCreazione, String loginAutorePagina, String loginUtenteViusalizzatore) throws SQLException{
        String quesryPagina = "SELECT * FROM PAGINA WHERE titolo = ? AND dataOraCreazione = ? AND idAutore = ?";
        PreparedStatement preparedStatementPagina = connection.prepareStatement(quesryPagina);
        preparedStatementPagina.setString(1, titolo);
        preparedStatementPagina.setTimestamp(2, Timestamp.valueOf(DataOraCreazione));

        String queryAutore = "SELECT idutente FROM utente WHERE LOWER(login) = ? LIMIT 1";
        PreparedStatement preparedStatementAutore = connection.prepareStatement(queryAutore);
        preparedStatementAutore.setString(1,loginAutorePagina.toLowerCase());
        ResultSet rsAutore = preparedStatementAutore.executeQuery();
        rsAutore.next();
        int idAutore = rsAutore.getInt("idutente");

        preparedStatementPagina.setInt(3, idAutore);
        ResultSet rsPagina = preparedStatementPagina.executeQuery();
        rsPagina.next();
        int idPagina = rsPagina.getInt("idPagina");

        String queryUtente = "SELECT idutente FROM utente WHERE LOWER(login) = ? LIMIT 1";
        PreparedStatement preparedStatementUtente = connection.prepareStatement(queryUtente);
        preparedStatementUtente.setString(1, loginUtenteViusalizzatore.toLowerCase());
        ResultSet rsUtente = preparedStatementUtente.executeQuery();
        rsUtente.next();
        int idUtente = rsUtente.getInt("idutente");

        String queryVisona = "INSERT INTO VISIONA(idPagina, idUtente) VALUES (?,?)";
        PreparedStatement preparedStatementVisona = connection.prepareStatement(queryVisona);
        preparedStatementVisona.setInt(1, idPagina);
        preparedStatementVisona.setInt(2,idUtente);
        preparedStatementVisona.executeUpdate();
        connection.close();
    }


    public void storicoPagineVisualizzate(String loginUtente, ArrayList<String> titoli, ArrayList<LocalDateTime> dateOreCreazioni, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> nomiUtente, ArrayList<String> password, ArrayList<String> email, ArrayList<Date> dataNascita, ArrayList<LocalDate> dateVisioni, ArrayList<LocalTime> oreVisioni) throws SQLException{
        try {
            String queryUtente = "SELECT idutente FROM utente WHERE LOWER(login) = ? LIMIT 1";
            PreparedStatement preparedStatementUtente = connection.prepareStatement(queryUtente);
            preparedStatementUtente.setString(1, loginUtente.toLowerCase());
            ResultSet rsUtente = preparedStatementUtente.executeQuery();
            rsUtente.next();
            int idUtente = rsUtente.getInt("idutente");

            String query = "SELECT * FROM visiona WHERE idUtente = ? ORDER BY dataVisone ASC, oraVisione ASC";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idUtente);
            ResultSet rs = preparedStatement.executeQuery();
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
                email.add(rsAutore.getString("email"));
                dataNascita.add(rsAutore.getDate("dataNascita"));
                dateVisioni.add(rs.getDate("dataVisone").toLocalDate());
                oreVisioni.add(rs.getTime("oraVisione").toLocalTime());
            }
        } catch (Exception e) {
            System.out.println("Errore durante l'esecuzione della query: " + e.getMessage());
            e.printStackTrace();
        }
        connection.close();
    }

    @Override
    public void getModificate(String login, ArrayList<String> titolo, ArrayList<LocalDateTime> dataOraCreazione, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> nomiUtente, ArrayList<String> password, ArrayList<String> email,  ArrayList<Date> dataNascita, ArrayList<LocalDate> dataProposta, ArrayList<LocalTime> oraProposta , ArrayList<String> stringaInserita, ArrayList<Integer> numerazione, ArrayList<Integer> stato, ArrayList<String> stringaProposta, ArrayList<Optional<LocalDate>> datavalutazione, ArrayList<Optional<LocalTime>> oraValutazione, ArrayList<LocalDate>  dataInserimento, ArrayList<Time> oraInseriento) throws SQLException {
        try{
            String query = "SELECT * FROM Pagina p JOIN ModificaProposta m ON p.idPagina = m.idPagina WHERE m.utentep = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            String queryUtente = "SELECT idutente FROM utente WHERE LOWER(login) = ? LIMIT 1";
            PreparedStatement preparedStatementUtente = connection.prepareStatement(queryUtente);
            preparedStatementUtente.setString(1, login.toLowerCase());
            ResultSet rsUtente = preparedStatementUtente.executeQuery();
            rsUtente.next();
            int idUtente = rsUtente.getInt("idutente");

            preparedStatement.setInt(1,idUtente);
            ResultSet rsPagina = preparedStatement.executeQuery();
            while (rsPagina.next()){
                String queryAutore = "SELECT * FROM utente WHERE idUtente = ? GROUP BY idUtente LIMIT 1";
                PreparedStatement preparedStatementAutore = connection.prepareStatement(queryAutore);
                preparedStatementAutore.setInt(1, rsPagina.getInt("autorev"));
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

                LocalDate dataV = rsPagina.getDate("dataValutazione") != null ?
                        rsPagina.getDate("dataValutazione").toLocalDate() : null;

                LocalTime oraV = rsPagina.getTime("oraValutazione") != null ?
                        rsPagina.getTime("oraValutazione").toLocalTime() : null;

                datavalutazione.add(Optional.ofNullable(dataV));
                oraValutazione.add(Optional.ofNullable(oraV));

                stato.add(rsPagina.getInt("stato"));
                String queryFraseCorrente = "SELECT * FROM Frasecorrente WHERE idPagina = ? AND stringaInserita = ? AND numerazione = ?";
                PreparedStatement preparedStatementFraseCorrente = connection.prepareStatement(queryFraseCorrente);
                preparedStatementFraseCorrente.setInt(1, rsPagina.getInt("idPagina"));
                preparedStatementFraseCorrente.setString(2, rsPagina.getString("stringaInserita"));
                preparedStatementFraseCorrente.setInt(3, rsPagina.getInt("numerazione"));
                ResultSet rsFraseCorrente = preparedStatementFraseCorrente.executeQuery();
                rsFraseCorrente.next();
                stringaInserita.add(rsFraseCorrente.getString("stringaInserita"));
                numerazione.add(rsFraseCorrente.getInt("numerazione"));
                dataInserimento.add(rsFraseCorrente.getDate("dataInserimento").toLocalDate());
                oraInseriento.add(rsFraseCorrente.getTime("orainserimento"));

            }
        }catch (Exception e){
            System.out.println("Errore durante l'esecuzione della query: " + e.getMessage());
            e.printStackTrace();
        }
        connection.close();
    }

    public boolean controllaNotifiche(String login) throws SQLException{
        boolean notificheRicevute = false;
        String queryNotifiche = "SELECT * FROM modificaProposta M WHERE M.autorev = ?  AND M.stato = 0 LIMIT 1";
        PreparedStatement preparedStatementNotifiche = connection.prepareStatement(queryNotifiche);

        String queryAutore = "SELECT idutente FROM utente WHERE LOWER(login) = ? LIMIT 1";
        PreparedStatement preparedStatementAutore = connection.prepareStatement(queryAutore);
        preparedStatementAutore.setString(1,login.toLowerCase());
        ResultSet rsAutore = preparedStatementAutore.executeQuery();
        rsAutore.next();
        int idAutore = rsAutore.getInt("idutente");

        preparedStatementNotifiche.setInt(1, idAutore);
        ResultSet rsNotifiche = preparedStatementNotifiche.executeQuery();
        while (rsNotifiche.next()){
            notificheRicevute = true;
        }
        return notificheRicevute;
    }

    public void getModifichePagina(String login, String titolo, LocalDateTime dataOraCreazione, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> nomiUtente, ArrayList<String> password, ArrayList<String> email,  ArrayList<Date> dataNascita, ArrayList<LocalDate> dataProposta, ArrayList<LocalTime> oraProposta , ArrayList<String> stringaInserita, ArrayList<Integer> numerazione, ArrayList<Integer> stato, ArrayList<String> stringaProposta, ArrayList<Optional<LocalDate>> datavalutazione, ArrayList<Optional<LocalTime>> oraValutazione, ArrayList<LocalDate>  dataInserimento, ArrayList<Time> oraInseriento) throws SQLException {
        try{
            String query = "SELECT * FROM (Pagina p JOIN ModificaProposta m ON p.idPagina = m.idPagina) JOIN utente u ON m.autoreV = u.idUtente WHERE u.login = ? AND p.titolo = ? AND p.dataOraCreazione = ? AND stato = 0";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, titolo);
            preparedStatement.setTimestamp(3, Timestamp.valueOf(dataOraCreazione));
            ResultSet rsPagina = preparedStatement.executeQuery();
            while (rsPagina.next()){
                String queryAutore = "SELECT * FROM utente WHERE idUtente = ? GROUP BY idUtente LIMIT 1";
                PreparedStatement preparedStatementAutore = connection.prepareStatement(queryAutore);
                preparedStatementAutore.setInt(1, rsPagina.getInt("utentep"));
                ResultSet rsAutore = preparedStatementAutore.executeQuery();
                rsAutore.next();
                nomi.add(rsAutore.getString("nome"));
                cognomi.add(rsAutore.getString("cognome"));
                nomiUtente.add(rsAutore.getString("login"));
                password.add(rsAutore.getString("password"));
                email.add(rsAutore.getString("email"));
                dataNascita.add(rsAutore.getDate("dataNascita"));
                stringaProposta.add(rsPagina.getString("stringaProposta"));
                dataProposta.add(rsPagina.getDate("dataProposta").toLocalDate());
                String queryOraProposta = "SELECT oraProposta::time(0) AS oraProposta FROM ModificaProposta WHERE idModifica = ?";
                PreparedStatement preparedStatementOraProposta = connection.prepareStatement(queryOraProposta);
                preparedStatementOraProposta.setInt(1, rsPagina.getInt("idModifica"));
                ResultSet rsOraProposta = preparedStatementOraProposta.executeQuery();
                rsOraProposta.next();
                oraProposta.add(rsOraProposta.getTime("oraProposta").toLocalTime());


                LocalDate dataV = rsPagina.getDate("dataValutazione") != null ?
                        rsPagina.getDate("dataValutazione").toLocalDate() : null;

                LocalTime oraV = rsPagina.getTime("oraValutazione") != null ?
                        rsPagina.getTime("oraValutazione").toLocalTime() : null;

                datavalutazione.add(Optional.ofNullable(dataV));
                oraValutazione.add(Optional.ofNullable(oraV));
                String queryFraseCorrente = "SELECT * FROM Frasecorrente WHERE idPagina = ? AND stringaInserita = ? AND numerazione = ?";
                PreparedStatement preparedStatementFraseCorrente = connection.prepareStatement(queryFraseCorrente);
                preparedStatementFraseCorrente.setInt(1, rsPagina.getInt("idPagina"));
                preparedStatementFraseCorrente.setString(2, rsPagina.getString("stringaInserita"));
                preparedStatementFraseCorrente.setInt(3, rsPagina.getInt("numerazione"));
                ResultSet rsFraseCorrente = preparedStatementFraseCorrente.executeQuery();
                rsFraseCorrente.next();
                stringaInserita.add(rsFraseCorrente.getString("stringaInserita"));
                numerazione.add(rsFraseCorrente.getInt("numerazione"));
                dataInserimento.add(rsFraseCorrente.getDate("datainserimento").toLocalDate());
                oraInseriento.add(rsFraseCorrente.getTime("oraInserimento"));
                stato.add(rsPagina.getInt("stato"));
            }
        }catch (Exception e){
            System.out.println("Errore durante l'esecuzione della query: " + e.getMessage());
            e.printStackTrace();
        }
        connection.close();
    }

    public boolean aggiornaStato(String loginAutore, String loginUtente, String stringaInserita, String stringaproposta, LocalDate dataProposta, LocalTime oraProposta, int cambiaStato) throws SQLException{
        boolean controllo = false;

        String queryAutore = "SELECT idutente FROM utente WHERE LOWER(login) = ? LIMIT 1";
        PreparedStatement preparedStatementAutore = connection.prepareStatement(queryAutore);
        preparedStatementAutore.setString(1, loginAutore.toLowerCase());
        ResultSet rsAutore = preparedStatementAutore.executeQuery();
        rsAutore.next();
        int idAutore = rsAutore.getInt("idutente");

        String queryUtente = "SELECT idutente FROM utente WHERE LOWER(login) = ? LIMIT 1";
        PreparedStatement preparedStatementUtente = connection.prepareStatement(queryUtente);
        preparedStatementUtente.setString(1, loginUtente.toLowerCase());
        ResultSet rsUtente = preparedStatementUtente.executeQuery();
        rsUtente.next();
        int idUtente = rsUtente.getInt("idutente");

        String queryModifica = "SELECT * FROM modificaProposta WHERE autorev = ? AND utentep = ? AND stringaInserita = ? AND stringaProposta = ? AND dataproposta = ? AND oraProposta::TIME(0) = ? AND stato = 0";
        PreparedStatement preparedStatementModifica = connection.prepareStatement(queryModifica);
        preparedStatementModifica.setInt(1, idAutore);
        preparedStatementModifica.setInt(2, idUtente);
        preparedStatementModifica.setString(3, stringaInserita);
        preparedStatementModifica.setString(4, stringaproposta);
        preparedStatementModifica.setDate(5, java.sql.Date.valueOf(dataProposta));
        preparedStatementModifica.setTime(6, Time.valueOf(oraProposta));
        ResultSet rsModifica = preparedStatementModifica.executeQuery();
        while(rsModifica.next()){
            int idModifica = rsModifica.getInt("idModifica");
            String queryUpdateModifica = "UPDATE modificaProposta SET stato = ? WHERE idmodifica = ?";
            PreparedStatement preparedStatementUpdateModifica = connection.prepareStatement(queryUpdateModifica);
            preparedStatementUpdateModifica.setInt(1, cambiaStato);
            preparedStatementUpdateModifica.setInt(2, idModifica);
            preparedStatementUpdateModifica.executeUpdate();
        }

        connection.close();
        return controllo;
    }

    public void addPaginacollegata(String stringaInserita, LocalDate dataInserimento, Time oraInserimento, int indiceFrase, String titoloCollegata, LocalDateTime dataCreazioneCollegata) throws SQLException{
        String queryFrase = "SELECT idPagina From fraseCorrente WHERE stringaInserita = ? AND numerazione = ? AND dataInserimento = ?";
        PreparedStatement preparedStatementFrase = connection.prepareStatement(queryFrase);
        preparedStatementFrase.setString(1, stringaInserita);
        preparedStatementFrase.setInt(2, indiceFrase);
        preparedStatementFrase.setDate(3, java.sql.Date.valueOf(dataInserimento));

        ResultSet rsFrase = preparedStatementFrase.executeQuery();

        if(rsFrase.next()){
            String queryPagina = "SELECT idPagina From Pagina WHERE titolo LIKE ? AND dataOraCreazione = ?";
            PreparedStatement preparedStatementPagina = connection.prepareStatement(queryPagina);
            preparedStatementPagina.setString(1, titoloCollegata);
            preparedStatementPagina.setTimestamp(2, Timestamp.valueOf(dataCreazioneCollegata));
            ResultSet rsPagina = preparedStatementPagina.executeQuery();
            if(rsPagina.next()){
                String queryCollegamento = "SELECT * From Collegamento WHERE idPagina = ? AND stringaInserita = ? AND numerazione = ?";
                PreparedStatement preparedStatementCollegamento = connection.prepareStatement(queryCollegamento);
                preparedStatementCollegamento.setInt(1, rsFrase.getInt("idPagina"));
                preparedStatementCollegamento.setString(2, stringaInserita);
                preparedStatementCollegamento.setInt(3, indiceFrase);


                ResultSet rsCollegamento= preparedStatementCollegamento.executeQuery();

                if(rsCollegamento.next()){
                    String queryInserimento = "UPDATE Collegamento SET paginaCollegata = ? WHERE idPagina = ? AND stringaInserita = ? AND numerazione = ?";
                    PreparedStatement preparedStatementInserimento = connection.prepareStatement(queryInserimento);
                    preparedStatementInserimento.setInt(1, rsPagina.getInt("idPagina"));
                    preparedStatementInserimento.setInt(2, rsFrase.getInt("idPagina"));
                    preparedStatementInserimento.setString(3, stringaInserita);
                    preparedStatementInserimento.setInt(4, indiceFrase);
                    preparedStatementInserimento.executeUpdate();
                }else {
                    String queryInserimento = "INSERT INTO Collegamento (idPagina, stringaInserita, numerazione, paginaCollegata) VALUES (?,?,?,?)";
                    PreparedStatement preparedStatementInserimento = connection.prepareStatement(queryInserimento);
                    preparedStatementInserimento.setInt(1, rsFrase.getInt("idPagina"));
                    preparedStatementInserimento.setString(2, stringaInserita);
                    preparedStatementInserimento.setInt(3, indiceFrase);
                    preparedStatementInserimento.setInt(4, rsPagina.getInt("idPagina"));
                    preparedStatementInserimento.executeUpdate();
                }
            }
        }
    }

    public boolean controllaNomeUtente(String nomeUtente) throws SQLException{
        String query = "SELECT * From utente WHERE LOWER(login) = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, nomeUtente.toLowerCase());
        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next()){
            return true;
        }
        return false;
    }
}