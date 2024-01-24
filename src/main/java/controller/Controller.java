package controller;

import MODEL.*;
import dao.WikiDAO;
import implementazionePostgresDAO.WikiimplementazionePostgresDAO;

import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public Pagina creazionePagina(String titolo, String testo, Utente autore) {
        Pagina paginaCreata = null;
        if(autore instanceof Autore){
            System.out.println("sono un autore");
            paginaCreata = new Pagina(titolo, LocalDateTime.now(), autore);
        }else{
            System.out.println("sono un utente");
            paginaCreata = new Pagina(titolo,LocalDateTime.now(),autore.getNome(),autore.getCognome(), autore.getLogin(), autore.getPassword(), autore.getEmail(), autore.getDataNascita());
            autore = paginaCreata.getAutore();
        }

        int length = testo.length();
        int prec = 0;
        int num = 0;
        String sottoStringa;
        for (int i = 0; i < length; i++) {
            if (testo.charAt(i) == ',' || testo.charAt(i) == '.' || testo.charAt(i) == ';'|| testo.charAt(i) == '?' || testo.charAt(i) == '!') {
                System.out.println("carattere == " + testo.charAt(i));
                sottoStringa = testo.substring(prec, i);
                int contaSpaziVuoti = 0;
                while(sottoStringa.charAt(contaSpaziVuoti) == ' '){
                    contaSpaziVuoti++;
                }
                sottoStringa = sottoStringa.substring(contaSpaziVuoti);
                Frase_Corrente fraseCorrente = new Frase_Corrente(sottoStringa, num, paginaCreata, LocalDate.now(), Time.valueOf(LocalTime.now()));
                num++;
                prec = i+1;
                System.out.println(sottoStringa);
            }
        }

        if(testo.charAt(length-1) != '.'|| testo.charAt(length-1) == '?' || testo.charAt(length-1) == '!'){
            System.out.println("carattere == " + testo.charAt(length-1));
            sottoStringa = testo.substring(prec, length);
            int contaSpaziVuoti = 0;
            while(sottoStringa.charAt(contaSpaziVuoti) == ' '){
                contaSpaziVuoti++;
            }
            sottoStringa = sottoStringa.substring(contaSpaziVuoti);
            Frase_Corrente fraseCorrente = new Frase_Corrente(sottoStringa, num, paginaCreata, LocalDate.now(), Time.valueOf(LocalTime.now()));
            System.out.println(sottoStringa);
        }

        WikiDAO w = new WikiimplementazionePostgresDAO();
        try {
            w.creazionePagina(paginaCreata);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return paginaCreata;
    }


    public ArrayList<Pagina> storicoPagineVisualizzate(Utente utente) {
        ArrayList<Pagina> pagineVisualizzate = new ArrayList<>();
        WikiDAO w = new WikiimplementazionePostgresDAO();
        try {
            pagineVisualizzate = w.storicoPagineVisualizzate(utente);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pagineVisualizzate;
    }

    public void addPaginaVisualizzata(Pagina paginaSelezionata, Utente utenteLoggato) {
        WikiDAO w = new WikiimplementazionePostgresDAO();
        try {
            w.addPaginaVisualizzata(paginaSelezionata, utenteLoggato);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Pagina> getModificate(Utente utenteloggato) {
        WikiDAO w = new WikiimplementazionePostgresDAO();
        ArrayList <Pagina> modifiche = new ArrayList<>();
        try {
            modifiche = w.getModificate(utenteloggato);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return modifiche;
    }

    public ArrayList<ModificaProposta> getProposte(Pagina paginaSelezionata, Utente utenteloggato) {
        WikiDAO w = new WikiimplementazionePostgresDAO();

    }
}
