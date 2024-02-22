package dao;

import GUI.Notifiche;
import MODEL.Pagina;


import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public interface WikiDAO {
    public void ricercaTitoli(String titoloInserito, ArrayList<String> titoli, ArrayList<LocalDateTime> dateOreCreazioni, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> login, ArrayList<String> password, ArrayList<String> email, ArrayList<Date> date) throws SQLException;

    public void getFrasiCorrenti(String login, String titolo, LocalDateTime dataOraCreazione, ArrayList<String> frasiInserite, ArrayList<LocalDate> dateInserimento, ArrayList<Time> oreInserimento, ArrayList<String> titoloCollegata, ArrayList<LocalDateTime> dataOraCreazioneCollegata, ArrayList<String> nomiCollegata, ArrayList<String> cognomiCollegata, ArrayList<String> loginCollegata, ArrayList<String> passwordCollegata, ArrayList<String> emailCollegata, ArrayList<Date> dateCollegata, ArrayList<Integer> numerazioneCollegata) throws SQLException;

    public void getModificheModificate(String login, String titolo, LocalDateTime dataOraCreazione, String stringaInserita, int numerazione, ArrayList<String> frasiProposte, ArrayList<LocalDate> dateProposte, ArrayList<LocalTime> oreProposte, ArrayList<Optional<LocalDate>> datavalutazione, ArrayList<Optional<LocalTime>> orevalutazione, ArrayList<Integer> stati, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> logins, ArrayList<String> password, ArrayList<String> email, ArrayList<Date> date) throws SQLException;

    public boolean verificaLoggato(String nome, String cognome, String login, String password, String email, Date datNascita, String ruolo) throws SQLException;

    public void registrazione(String nome, String cognome, String nomeUtente, String password, String email, Date dataNascita) throws SQLException;

    public boolean inviaProposta(int numerazione, String fraseSelezionata, String fraseProposta, String loginUtente, String loginAutore, String titolo, LocalDateTime dataOraCreazione) throws SQLException;

    public void creazionePagina(String titolo, ArrayList<String> frasi, String login, LocalDateTime dataCreazione) throws SQLException;

    public void storicoPagineVisualizzate(String loginUtente, ArrayList<String> titoli, ArrayList<LocalDateTime> dateOreCreazioni, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> nomiUtente, ArrayList<String> password, ArrayList<String> email, ArrayList<Date> dataNascita, ArrayList<LocalDate> dateVisioni, ArrayList<LocalTime> oreVisioni) throws SQLException;

    public void addPaginaVisualizzata(String titolo, LocalDateTime DataOraCreazione, String loginAutorePagina, String loginUtenteVisualizzatore) throws SQLException;

    public void getModificate(String login, ArrayList<String> titolo, ArrayList<LocalDateTime> dataOraCreazione, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> nomiUtente, ArrayList<String> password, ArrayList<String> email, ArrayList<Date> dataNascita, ArrayList<LocalDate> dataProposta, ArrayList<LocalTime> oraProposta, ArrayList<String> stringaInserita, ArrayList<Integer> numerazione, ArrayList<Integer> stato, ArrayList<String> stringaProposta, ArrayList<Optional<LocalDate>> datavalutazione, ArrayList<Optional<LocalTime>> oraValutazione, ArrayList<LocalDate> dataInserimento, ArrayList<Time> oraInseriento) throws SQLException;

    public void storicoPagineCreate(String login, ArrayList<String> titoli, ArrayList<LocalDateTime> dataOraCreazione) throws SQLException;

    public boolean controllaNotifiche(String login) throws SQLException;

    public String getNomeUtente(String login, String password);

    public String getCognomeUtente(String login, String password);

    String getEmailUtente(String login, String password);

    Date getDataNascitaUtente(String login, String password);

    String getRuolotente(String login, String password);

    public String getFraseSelezionata(String loginAutore, String loginUtente, String stringaProposta, String stringaInserita, LocalDate dataProposta, LocalTime oraProposta) throws SQLException;

    public void getModificheFrase(String titolo, LocalDateTime dataOraCreazione, String stringaInserita, int numerazione, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> nomiUtente, ArrayList<String> password, ArrayList<String> email, ArrayList<Date> dataNascita, ArrayList<LocalDate> dataProposta, ArrayList<LocalTime> oraProposta, ArrayList<Integer> stato, ArrayList<String> stringaProposta, ArrayList<LocalDate> dataValutazione, ArrayList<LocalTime> oraValutazione) throws SQLException;

    public void getModifichePagina(String login, String titolo, LocalDateTime dataCreazione, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> nomiUtente, ArrayList<String> password, ArrayList<String> email, ArrayList<Date> dataNascita, ArrayList<LocalDate> dataProposta, ArrayList<LocalTime> oraProposta, ArrayList<String> stringaInserita, ArrayList<Integer> numerazione, ArrayList<Integer> stato, ArrayList<String> stringaProposta, ArrayList<Optional<LocalDate>> datavalutazione, ArrayList<Optional<LocalTime>> oraValutazione, ArrayList<LocalDate> dataInserimento, ArrayList<Time> oraInseriento) throws SQLException;

    public boolean aggiornaStato(String loginAutore, String loginUtente, String stringaInserita, String stringaproposta, LocalDate dataProposta, LocalTime oraProposta, int cambiaStato) throws SQLException;

    public void getModificheUtente(String login, ArrayList<String> titolo, ArrayList<LocalDateTime> dataOraCreazione, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> nomiUtente, ArrayList<String> password, ArrayList<String> email, ArrayList<Date> dataNascita, ArrayList<LocalDate> dataProposta, ArrayList<LocalTime> oraProposta, ArrayList<String> stringaInserita, ArrayList<Integer> numerazione, ArrayList<Integer> stato, ArrayList<String> stringaProposta, ArrayList<LocalDate> dataValutazione, ArrayList<LocalTime> oraValutazione, ArrayList<LocalDate> dataInserimento, ArrayList<Time> oraInseriento) throws SQLException;

    public void getPagineCreate(String login, ArrayList<String> titoli, ArrayList<LocalDateTime> dataOraCreazione) throws SQLException;

    public void addPaginacollegata(String titolo, LocalDateTime dataCreazione, String stringaInserita, LocalDate dataInserimento, Time oraInserimento, int indiceFrase, String titoloCollegata, LocalDateTime dataCreazioneCollegata) throws SQLException;

    boolean controllaNomeUtente(String nomeUtente) throws SQLException;
}

