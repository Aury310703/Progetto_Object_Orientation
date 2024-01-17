package dao;

import MODEL.Frase;
import MODEL.Pagina;
import MODEL.Utente;

import java.sql.SQLException;
import java.util.ArrayList;

public interface WikiDAO {
public ArrayList<Pagina> ricercaTitoli(String titoloInserito) throws SQLException;

public ArrayList<Frase> getTestoPagina(Pagina paginaSelezionata) throws SQLException;

public Utente verificaLoggato(String login, String password) throws SQLException;
}
