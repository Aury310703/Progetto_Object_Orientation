package dao;

import GUI.Notifiche;


import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public interface WikiDAO {
    public void ricercaTitoli(String titoloInserito, ArrayList<String> titoli, ArrayList<LocalDateTime> dateOreCreazioni, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> login, ArrayList<String> password, ArrayList<String> email, ArrayList<Date> date) throws SQLException;
public void getFrasiCorrenti(String login, String titolo, LocalDateTime dataOraCreazione, ArrayList<String> frasiInserite, ArrayList<LocalDate> dateInserimento, ArrayList<Time> oreInserimento) throws SQLException;
public void getModificheModificate(String login, String titolo, LocalDateTime dataOraCreazione, ArrayList<String> frasiProposte, ArrayList<LocalDate> dateProposte, ArrayList<LocalTime> oreProposte, ArrayList<LocalDate> datevalutazione, ArrayList<LocalTime> orevalutazione, ArrayList<Integer> stati, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> logins, ArrayList<String> password, ArrayList<String> email, ArrayList<Date> date) throws SQLException;
public boolean verificaLoggato(String nome, String cognome, String login, String password, String email, Date datNascita, String ruolo)throws SQLException;
public void registrazione(String nome, String cognome, String nomeUtente, String password, String email, Date dataNascita) throws SQLException;
public boolean inviaProposta(int numerazione, String fraseSelezionata, String fraseProposta, String loginUtente, String loginAutore, String titolo, LocalDateTime dataOraCreazione) throws SQLException;
public void creazionePagina(String titolo, ArrayList <String> frasi, String login) throws SQLException;
public void storicoPagineVisualizzate(String loginUtente, ArrayList<String> titoli, ArrayList<LocalDateTime> dateOreCreazioni, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> nomiUtente, ArrayList<String> password, ArrayList<String> email, ArrayList<Date> dataNascita, ArrayList<LocalDate> dateVisioni, ArrayList<LocalTime> oreVisioni) throws SQLException;
public void addPaginaVisualizzata(String titolo,LocalDateTime DataOraCreazione,String loginAutorePagina, String loginUtenteVisualizzatore) throws SQLException;
public void getModificate(String login, ArrayList<String> titolo,ArrayList<LocalDateTime> dataOraCreazione, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> nomiUtente, ArrayList<String> password, ArrayList<String> email,  ArrayList<Date> dataNascita, ArrayList<LocalDate> dataProposta, ArrayList<LocalTime> oraProposta , ArrayList<String> stringaInserita, ArrayList<Integer> numerazione, ArrayList<Integer> stato, ArrayList<String> stringaProposta, ArrayList<LocalDate> dataValutazione, ArrayList<LocalTime> oraValutazione, ArrayList<LocalDate>  dataInserimento, ArrayList<Time> oraInseriento) throws SQLException;
public ArrayList<ModificaProposta> getProposte(Pagina paginaSelezionata, Utente utenteLoggato) throws SQLException;
public void storicoPagineCreate(String login, ArrayList<String> titoli, ArrayList<LocalDateTime> dataOraCreazione) throws SQLException;
public boolean controllaNotifiche(String login) throws SQLException;
public boolean aggiornaStato(ArrayList<Notifica> notifiche, int cambiaStato) throws SQLException;
public String getNomeUtente(String login, String password);

public String getCognomeUtente(String login, String password);

    String getEmailUtente(String login, String password);

    Date getDataNascitaUtente(String login, String password);

    String getRuolotente(String login, String password);

    void getNotifiche(ArrayList<String> fraseSelezionata, ArrayList<Integer> stati, ArrayList<String> frasiProposte);
}


