package controller;

import GUI.Notifiche;
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

    private Utente utenteloggato;
    private Autore autoreloggato;
    private ArrayList<Pagina> pagineTrovate;

    public Controller(){

    }

    public void ricercaTitoli(String titoloInserito){
        ArrayList<String> titoli = new ArrayList<>();
        ArrayList<LocalDateTime> dateOreCreazioni = new ArrayList<>();
        ArrayList<String> nomi = new ArrayList<>();
        ArrayList<String> cognom = new ArrayList<>();
        ArrayList<String> login = new ArrayList<>();
        ArrayList<String> password = new ArrayList<>();
        ArrayList<String> email = new ArrayList<>();
        ArrayList<Date> date = new ArrayList<>();

            WikiDAO w = new WikiimplementazionePostgresDAO();
        try {
           w.ricercaTitoli(titoloInserito, titoli, dateOreCreazioni, nomi, cognom, login, password, email, date);
            ArrayList<Autore> autori = new ArrayList<>();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    public void creaUtente(String nome, String cognome, String login, String password, String email, Date datNascita){
        utenteloggato = new Utente(nome,cognome,login,password,email,datNascita);
    }


    public int verificaLoggato(String login, String password) {
        int controllo = 0;
        WikiDAO w = new WikiimplementazionePostgresDAO();
        String nome = null;
        String cognome = null;
        String email = null;
        Date dataNascita = null;
        String ruolo = null;
        try {
            if(w.verificaLoggato(nome, cognome, login, password, email, dataNascita, ruolo)){
                if(ruolo.equals("utente")){
                    controllo = 1;
                    utenteloggato = new Utente(nome, cognome, login, password, email, dataNascita);
                }else{
                    controllo = 2;
                    ArrayList <String> titoli = new ArrayList<>();
                    ArrayList <LocalDateTime> dataOraCreazione = new ArrayList<>();
                    autoreloggato = new Autore(nome, cognome,login, password, email, dataNascita,titoli.get(0),dataOraCreazione.get(0));
                    for(int i = 0; i < titoli.size(); i++){
                        Pagina pagina = new Pagina(titoli.get(i), dataOraCreazione.get(i), autoreloggato);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return controllo;
    }

    public void registrazioneUtente(String nome, String cognome, String nomeUtente, String password, String email, Date dataNascita) {
        WikiDAO w = new WikiimplementazionePostgresDAO();
        try {
            w.registrazione(nome,cognome,nomeUtente,password,email,dataNascita);
            utenteloggato = new Utente(nome,cognome,nomeUtente,password,email,dataNascita);
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
        ArrayList <Pagina> modifiche = new ArrayList<>();
        WikiDAO w = new WikiimplementazionePostgresDAO();
        try {
            modifiche = w.getModificate(utenteloggato);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return modifiche;
    }

    public ArrayList<ModificaProposta> getProposte(Pagina paginaSelezionata, Utente utenteLoggato) {
        ArrayList<ModificaProposta> modifiche = new ArrayList<>();
        WikiDAO w = new WikiimplementazionePostgresDAO();
        try {
            modifiche = w.getProposte(paginaSelezionata, utenteLoggato);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return modifiche;
    }

    public ArrayList<Pagina> storicoPagineCreate(Autore autoreLoggato) {
        ArrayList<Pagina> pagineCreate = new ArrayList<>();
        WikiDAO w = new WikiimplementazionePostgresDAO();
        try {
            pagineCreate = w.storicoPagineCreate(autoreLoggato);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pagineCreate;
    }

    public boolean controllaNotifiche(){
        boolean notifiche = false;
        WikiDAO w = new WikiimplementazionePostgresDAO();
        try {
            notifiche = w.controllaNotifiche(autoreloggato.getLogin());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return notifiche;
    }

    public boolean aggiornaStato(ArrayList<Notifica> notifiche, int cambiaStato) {
        boolean controllo = false;
        WikiDAO w = new WikiimplementazionePostgresDAO();
        try {
            controllo = w.aggiornaStato(notifiche, cambiaStato);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return controllo;
    }
}
