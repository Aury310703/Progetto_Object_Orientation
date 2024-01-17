package dao;

import MODEL.Frase;
import MODEL.Pagina;
import MODEL.Utente;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public interface WikiDAO {
public ArrayList<Pagina> ricercaTitoli(String titoloInserito) throws SQLException;

public ArrayList<Frase> getTestoPagina(Pagina paginaSelezionata) throws SQLException;

public Utente verificaLoggato(String login, String password) throws SQLException;

void registrazione(String nome, String cognome, String nomeUtente, String password, String email, Date dataNascita) throws SQLException;
}


