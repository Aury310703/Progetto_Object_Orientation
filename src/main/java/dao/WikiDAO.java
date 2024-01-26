package dao;

import GUI.Notifiche;
import MODEL.*;

import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public interface WikiDAO {
    public void ricercaTitoli(String titoloInserito, ArrayList<String> titoli, ArrayList<LocalDateTime> dateOreCreazioni, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> login, ArrayList<String> password, ArrayList<String> email, ArrayList<Date> date) throws SQLException;
public void getFrasiCorrenti(String login, String titolo, LocalDateTime dataOraCreazione, ArrayList<String> frasiInserite, ArrayList<LocalDate> dateInserimento, ArrayList<Time> oreInserimento, ArrayList<Integer> stati) throws SQLException;
public boolean verificaLoggato(String nome, String cognome, String login, String password, String email, Date datNascita, String ruolo)throws SQLException;
public void registrazione(String nome, String cognome, String nomeUtente, String password, String email, Date dataNascita) throws SQLException;
public boolean inviaProposta(Pagina paginaSelezionata, Frase_Corrente fraseSelezionata, String fraseProposta, Utente utenetLoggato) throws SQLException;
public void creazionePagina(Pagina paginaCreata) throws SQLException;
public ArrayList<Pagina> storicoPagineVisualizzate(Utente utente) throws SQLException;
public void addPaginaVisualizzata(Pagina paginaSelezionata, Utente utenteLoggato) throws SQLException;
public ArrayList<Pagina> getModificate(Utente utenteloggato) throws SQLException;
public ArrayList<ModificaProposta> getProposte(Pagina paginaSelezionata, Utente utenteLoggato) throws SQLException;
public ArrayList<Pagina> storicoPagineCreate(Autore autoreLoggato) throws SQLException;
public boolean controllaNotifiche(Autore utenteLoggato) throws SQLException;
public boolean aggiornaStato(ArrayList<Notifica> notifiche, int cambiaStato) throws SQLException;
public String getNomeUtente(String login, String password);

public String getCognomeUtente(String login, String password);

    String getEmailUtente(String login, String password);

    Date getDataNascitaUtente(String login, String password);

    String getRuolotente(String login, String password);

}


