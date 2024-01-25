package dao;

import MODEL.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public interface WikiDAO {
public ArrayList<Pagina> ricercaTitoli(String titoloInserito) throws SQLException;
public ArrayList<Frase> getTestoPagina(Pagina paginaSelezionata) throws SQLException;
public Utente verificaLoggato(String login, String password) throws SQLException;
void registrazione(String nome, String cognome, String nomeUtente, String password, String email, Date dataNascita) throws SQLException;
public boolean inviaProposta(Pagina paginaSelezionata, Frase_Corrente fraseSelezionata, String fraseProposta, Utente utenetLoggato) throws SQLException;
public void creazionePagina(Pagina paginaCreata) throws SQLException;
public ArrayList<Pagina> storicoPagineVisualizzate(Utente utente) throws SQLException;
public void addPaginaVisualizzata(Pagina paginaSelezionata, Utente utenteLoggato) throws SQLException;
public ArrayList<Pagina> getModificate(Utente utenteloggato) throws SQLException;
public ArrayList<ModificaProposta> getProposte(Pagina paginaSelezionata, Utente utenteLoggato) throws SQLException;
public ArrayList<Pagina> storicoPagineCreate(Autore autoreLoggato) throws SQLException;
public boolean controllaNotifiche(Autore utenteLoggato) throws SQLException;
}


