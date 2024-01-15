package dao;

import MODEL.Pagina;

import java.sql.SQLException;
import java.util.ArrayList;

public interface WikiDAO {
public ArrayList<Pagina> ricercaTitoli(String titoloInserito) throws SQLException;

public String getTestoPagina(Pagina paginaSelezionata) throws SQLException;
}
