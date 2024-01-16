package controller;

import MODEL.Autore;
import MODEL.Frase;
import MODEL.Pagina;
import MODEL.Utente;
import dao.WikiDAO;
import implementazionePostgresDAO.WikiimplementazionePostgresDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Controller {

    public ArrayList<Pagina> ricercaTitoli(String titoloInserito){
        ArrayList<Pagina> pagineTrovate = new ArrayList<>();
        WikiDAO w = new WikiimplementazionePostgresDAO();
        try {
           pagineTrovate = w.ricercaTitoli(titoloInserito);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pagineTrovate;
    }

    public String getPaginaTitolo(Pagina i) {
        return i.getTitolo();
    }

    public ArrayList<Frase> getTestoPagina(Pagina paginaSelezionata) {
        ArrayList<Frase> testoPagina;
        WikiDAO w = new WikiimplementazionePostgresDAO();
        try {
            testoPagina = w.getTestoPagina(paginaSelezionata);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return testoPagina;
    }
}
