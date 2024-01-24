package implementazionePostgresDAO;


import MODEL.*;
import dao.WikiDAO;
import database.ConnessioneDatabase;

import java.sql.*;
import java.time.*;
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
            if(rs.getString("ruolo").toLowerCase().equals("utente"))
                utenteLoggato= new Utente(rs.getString("nome"), rs.getString("cognome"), login.toLowerCase(), password, rs.getString("email"), rs.getDate("dataNascita"));
            else {
                System.out.println("Il mio ruolo è" + rs.getString("ruolo"));
                String queryAutore = "SELECT * FROM Pagina WHERE idAutore = ?";
                PreparedStatement preparedStatementAutore = connection.prepareStatement(queryAutore);
                preparedStatementAutore.setInt(1, rs.getInt("idUtente"));
                ResultSet rsAutore = preparedStatementAutore.executeQuery();
                rsAutore.next();
                utenteLoggato = new Autore(rs.getString("nome"), rs.getString("cognome"), login, password, rs.getString("email"), rs.getDate("dataNascita"), rsAutore.getString("titolo"), rsAutore.getTimestamp("dataOraCreazione").toLocalDateTime());

                while (rsAutore.next()) {
                        Pagina pagina = new Pagina(rsAutore.getString("titolo"), rsAutore.getTimestamp("dataOraCreazione").toLocalDateTime(), utenteLoggato);
                }
            }
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
    }

    public void addPaginaVisualizzata(Pagina paginaSelezionata, Utente utenteLoggato) throws SQLException{
        String quesryPagina = "SELECT * FROM PAGINA WHERE titolo = ? AND dataOraCreazione = ? AND idAutore = ?";
        PreparedStatement preparedStatementPagina = connection.prepareStatement(quesryPagina);
        preparedStatementPagina.setString(1, paginaSelezionata.getTitolo());
        preparedStatementPagina.setTimestamp(2, Timestamp.valueOf(paginaSelezionata.getDataCreazione()));

        String queryAutore = "SELECT idutente FROM UTENTE WHERE login = ? LIMIT 1";
        PreparedStatement preparedStatementAutore = connection.prepareStatement(queryAutore);
        preparedStatementAutore.setString(1, paginaSelezionata.getAutore().getLogin());
        ResultSet rsAutore = preparedStatementAutore.executeQuery();
        rsAutore.next();
        int idAutore = rsAutore.getInt("idutente");

        preparedStatementPagina.setInt(3, idAutore);
        ResultSet rsPagina = preparedStatementPagina.executeQuery();
        rsPagina.next();
        int idPagina = rsPagina.getInt("idPagina");

        String queryUtente = "SELECT idutente FROM UTENTE WHERE login = ? LIMIT 1";
        PreparedStatement preparedStatementUtente = connection.prepareStatement(queryUtente);
        preparedStatementUtente.setString(1, utenteLoggato.getLogin());
        ResultSet rsUtente = preparedStatementUtente.executeQuery();
        rsUtente.next();
        int idUtente = rsUtente.getInt("idutente");

        String queryVisona = "INSERT INTO VISIONA(idPagina, idUtente) VALUES (?,?)";
        PreparedStatement preparedStatementVisona = connection.prepareStatement(queryVisona);
        preparedStatementVisona.setInt(1, idPagina);
        preparedStatementVisona.setInt(2,idUtente);
        Visiona visiona = new Visiona(java.sql.Date.valueOf(LocalDate.now()), Time.valueOf(LocalTime.now()), paginaSelezionata, utenteLoggato);

        int rowsAffectedFrase = preparedStatementVisona.executeUpdate();

        if (rowsAffectedFrase > 0) {
            System.out.println("Inserimento riuscito!");
        } else {
            System.out.println("Nessuna riga inserita.");
        }
    }


    public ArrayList<Pagina> storicoPagineVisualizzate(Utente utente) throws SQLException{
        ArrayList<Pagina> pagineVisualizzate = new ArrayList<>();

        try {
            String queryUtente = "SELECT idutente FROM utente WHERE login = ? LIMIT 1";
            PreparedStatement preparedStatementUtente = connection.prepareStatement(queryUtente);
            preparedStatementUtente.setString(1, utente.getLogin());
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

                Pagina pagina = new Pagina(rsPagina.getString("titolo"), (rsPagina.getTimestamp("dataOraCreazione")).toLocalDateTime(), rsAutore.getString("nome"), rsAutore.getString("cognome"), rsAutore.getString("login"), rsAutore.getString("password"), rsAutore.getString("email"), rsAutore.getDate("dataNascita"));
                pagineVisualizzate.add(pagina);

            }
        } catch (Exception e) {
            System.out.println("Errore durante l'esecuzione della query: " + e.getMessage());
            e.printStackTrace();
        }
        connection.close();
        return pagineVisualizzate;
    }

}
