package controller;

import MODEL.*;
import dao.WikiDAO;
import implementazionePostgresDAO.WikiimplementazionePostgresDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

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

    public Utente verificaLoggato(String login, String password) {
        WikiDAO w = new WikiimplementazionePostgresDAO();
        Utente utenteLoggato = null;
        try {
            utenteLoggato = w.verificaLoggato(login, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return utenteLoggato;
    }

    public void registrazioneUtente(String nome, String cognome, String nomeUtente, String password, String email, Date dataNascita) {
        WikiDAO w = new WikiimplementazionePostgresDAO();
        try {
            w.registrazione(nome,cognome,nomeUtente,password,email,dataNascita);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public boolean inviaProposta(Pagina paginaSelezionata, Frase_Corrente fraseSelezionata, String fraseProposta, Utente utenteLoggato) {
        boolean controllo = false;
        WikiDAO w = new WikiimplementazionePostgresDAO();
        try {
            if(w.inviaProposta(paginaSelezionata, fraseSelezionata, fraseProposta, utenteLoggato));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        controllo = true;
        return controllo;
    }

    public ArrayList<Frase> componiTesto(Pagina paginaSelezionata) {
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
}
