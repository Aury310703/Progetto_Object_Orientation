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

    private Utente utenteLoggato;
    private Autore autoreloggato;
    private ArrayList<Pagina> pagineTrovate;
    private Controller controller;
    private Pagina paginaSelezionata;

    public Controller(){

    }

    public int verificaRuoloUtente(){
        if(utenteLoggato != null){
            return 1;
        }else if(autoreloggato != null){
            return 2;
        }
        return 0;
    }

    public String getNomeUtenteLoggato(){
        return utenteLoggato.getNome();
    }

    public String getCognomeUtenteLoggato(){
        return utenteLoggato.getCognome();
    }

    public String getNomeAutoreLoggato(){
        return autoreloggato.getNome();
    }

    public String getCognomeAutoreLoggato(){
        return autoreloggato.getCognome();
    }


    public String getPaginaTitolo(Pagina i) {
        return i.getTitolo();
    }


    public ArrayList<String> getTestoPagina() {
        ArrayList<String> testoPagina = new ArrayList<>();
        WikiDAO w = new WikiimplementazionePostgresDAO();
        ArrayList<String> frasiInserite = new ArrayList<>();
        ArrayList<LocalDate> dateInserimento = new ArrayList<>();
        ArrayList<Time> oreInserimento = new ArrayList<>();
        try {
            w.getFrasiCorrenti(paginaSelezionata.getAutore().getLogin(), paginaSelezionata.getTitolo(), paginaSelezionata.getDataCreazione(), frasiInserite, dateInserimento, oreInserimento);
            int numerazione = 0;
            for(String frase : frasiInserite){
                Frase_Corrente fraseCorrente = new Frase_Corrente(frase, numerazione, paginaSelezionata, dateInserimento.get(numerazione), oreInserimento.get(numerazione));

                ArrayList<String> frasiProposte = new ArrayList<>();
                ArrayList<LocalDate> dateProposte = new ArrayList<>();
                ArrayList<LocalTime> oreProposte = new ArrayList<>();
                ArrayList<LocalDate> datevalutazione = new ArrayList<>();
                ArrayList<Time> orevalutazione = new ArrayList<>();
                ArrayList<Integer> stati = new ArrayList<>();
                ArrayList<String> nomi = new ArrayList<>();
                ArrayList<String> cognomi = new ArrayList<>();
                ArrayList<String> logins = new ArrayList<>();
                ArrayList<String> password = new ArrayList<>();
                ArrayList<String> email = new ArrayList<>();
                ArrayList<Date> date = new ArrayList<>();

                try {
                    w.getModificheModificate(paginaSelezionata.getAutore().getLogin(), paginaSelezionata.getTitolo(), paginaSelezionata.getDataCreazione(), frasiProposte, dateProposte, oreProposte, datevalutazione, orevalutazione, stati, nomi, cognomi, logins, password, email, date);
                    for(int i = 0; i < frasiProposte.size(); i++){
                        Utente utente = new Utente(nomi.get(i), cognomi.get(i), logins.get(i), password.get(i), email.get(i), date.get(i));
                        ModificaProposta modificaProposta = new ModificaProposta(dateProposte.get(i), oreProposte.get(i), paginaSelezionata.getAutore(), utente, fraseCorrente, frasiProposte.get(i), numerazione, stati.get(i));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                numerazione++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ArrayList<String> frasiTesto= new ArrayList<>();
        int controllo = 0;
        Frase fr_salvata = null;
        for(Frase_Corrente f : paginaSelezionata.getFrasi()){
            System.out.println("-------------------");
            System.out.println((f.getStringa_inserita()));
            LocalDate data_max = f.getDataInserimento();
            LocalTime oraMax = f.getOraInserimento().toLocalTime();
            for(ModificaProposta fc : f.getProposte()){
                System.out.println("++++++++++++++++");
                System.out.println(fc.getStringa_inserita());
                if(fc.getStato() == 1) {
                    controllo = 1;
                    LocalDate dataModifica = fc.getDataValutazione();
                    LocalTime oraModifica = fc.getOraValutazione();
                    if (data_max.isAfter(dataModifica) && oraMax.isAfter((oraModifica))) {
                        fr_salvata = f;
                    } else {
                        fr_salvata = fc;
                    }
                }
            }
            System.out.println("++++++++++++++++");
            if(controllo == 0){
                frasiTesto.add(f.getStringa_inserita());
                System.out.println("frase scelta:" + f.getStringa_inserita());
            }else{
                frasiTesto.add(f.getStringa_inserita());
                System.out.println("frase scelta:" + fr_salvata.getStringa_inserita());
            }
            controllo = 0;

        }
        return testoPagina;
    }

    public Utente creaUtente(String nome, String cognome, String login, String password, String email, Date datNascita){
        Utente utente = new Utente(nome,cognome,login,password,email,datNascita);
        return utente;
    }

    public boolean loggato(){
        if(utenteLoggato != null || autoreloggato != null)
            return true;
        return false;
    }


    public int verificaLoggato(String login, String password) {
        int controllo = 0;
        WikiDAO w = new WikiimplementazionePostgresDAO();
        String nome = w.getNomeUtente(login, password);
        String cognome = w.getCognomeUtente(login, password);
        String email = w.getEmailUtente(login, password);
        Date dataNascita = w.getDataNascitaUtente(login, password);
        String ruolo = w.getRuolotente(login, password);
        try {
            if(w.verificaLoggato(nome, cognome, login, password, email, dataNascita, ruolo)){
                if(ruolo.equals("utente")){
                    controllo = 1;
                    utenteLoggato = new Utente(nome, cognome, login, password, email, dataNascita);
                }else{
                    controllo = 2;
                    ArrayList <String> titoli = new ArrayList<>();
                    ArrayList <LocalDateTime> dataOraCreazione = new ArrayList<>();
                    autoreloggato = new Autore(nome, cognome,login, password, email, dataNascita,titoli.get(0), dataOraCreazione.get(0));
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
            utenteLoggato = new Utente(nome,cognome,nomeUtente,password,email,dataNascita);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> getFrasiTestoSelezionato(){
        ArrayList<String> frasiTesto = new ArrayList<>();
        for(Frase_Corrente f : paginaSelezionata.getFrasi()){
            LocalDate dataMax = f.getDataInserimento();
            LocalTime oraMax = f.getOraInserimento().toLocalTime();
            String frase = f.getStringa_inserita();
            for(ModificaProposta mp : f.getProposte()){
                if (mp.getStato() == 1){
                    if(mp.getDataValutazione().isAfter(dataMax) && mp.getOraValutazione().isAfter(oraMax)){
                       frase = mp.getStringa_inserita();
                       dataMax = mp.getDataValutazione();
                       oraMax = mp.getOraValutazione();
                    }
                }
            }
            frasiTesto.add(frase);
        }
        return frasiTesto;
    }

    public boolean inviaProposta(String fraseSelezionata, String fraseProposta, int numerazione) {
        boolean controllo = false;
        WikiDAO w = new WikiimplementazionePostgresDAO();
        try {
            ModificaProposta modifica = null;
            if(utenteLoggato.getLogin() != null) {
                w.inviaProposta(numerazione, fraseSelezionata, fraseProposta, utenteLoggato.getLogin(), paginaSelezionata.getAutore().getLogin(), paginaSelezionata.getTitolo(), paginaSelezionata.getDataCreazione());
                modifica = new ModificaProposta(LocalDate.now(), LocalTime.now(), paginaSelezionata.getAutore(), utenteLoggato, paginaSelezionata.getFrasi().get(numerazione), fraseProposta, numerazione);
            }else {
                w.inviaProposta(numerazione, fraseSelezionata, fraseProposta, autoreloggato.getLogin(), paginaSelezionata.getAutore().getLogin(), paginaSelezionata.getTitolo(), paginaSelezionata.getDataCreazione());
                modifica = new ModificaProposta(LocalDate.now(), LocalTime.now(), paginaSelezionata.getAutore(), autoreloggato, paginaSelezionata.getFrasi().get(numerazione), fraseProposta, numerazione);
            }
            paginaSelezionata.getFrasi().get(numerazione).addProposte(modifica);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        controllo = true;
        return controllo;
    }

    public ArrayList<String> componiTesto() {
        ArrayList<Frase> frasiTesto= new ArrayList<>();
        int controllo = 0;
        Frase fr_salvata = null;
        for(Frase_Corrente f : paginaSelezionata.getFrasi()){
            LocalDate data_max = f.getDataInserimento();
            LocalTime oraMax = f.getOraInserimento().toLocalTime();
            for(ModificaProposta fc : f.getProposte()){
                if(fc.getStato() == 1) {
                    controllo = 1;
                    LocalDate dataModifica = fc.getDataValutazione();

                    if (data_max.isAfter(fc.getDataValutazione()) && oraMax.isAfter(fc.getOraValutazione())) {
                        fr_salvata = f;
                    } else {
                        fr_salvata = fc;
                        data_max = fc.getDataValutazione();
                        oraMax = fc.getOraValutazione();
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
        ArrayList<String> frasi= new ArrayList<>();
        for(Frase f : frasiTesto){
            frasi.add(f.getStringa_inserita());
        }
        return frasi;
    }

    public void creazionePagina(String titolo, String testo) {
        Pagina paginaCreata = null;
        if(autoreloggato != null){
            System.out.println("sono un autore");
            paginaCreata = new Pagina(titolo, LocalDateTime.now(), autoreloggato);
        }else{
            System.out.println("sono un utente");
            paginaCreata = new Pagina(titolo,LocalDateTime.now(), utenteLoggato.getNome(), utenteLoggato.getCognome(), utenteLoggato.getLogin(), utenteLoggato.getPassword(), utenteLoggato.getEmail(), utenteLoggato.getDataNascita());
            autoreloggato = paginaCreata.getAutore();
            utenteLoggato = null;
            paginaSelezionata = paginaCreata;
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

    public void addPaginaSelezionata(int numeroPaginaSelezionata){
        paginaSelezionata = pagineTrovate.get(numeroPaginaSelezionata);
    }


    public ArrayList<String> storicoPagineVisualizzate() {
        ArrayList<String> titoli = new ArrayList<>();
        ArrayList<LocalDateTime> dateOreCreazioni = new ArrayList<>();
        ArrayList<String> nomi = new ArrayList<>();
        ArrayList<String> cognomi = new ArrayList<>();
        ArrayList<String> nomiUtente = new ArrayList<>();
        ArrayList<String> password = new ArrayList<>();
        ArrayList<String> email = new ArrayList<>();
        ArrayList<Date> dataNascita = new ArrayList<>();
        ArrayList<LocalDate> dateVisioni = new ArrayList<>();
        ArrayList<LocalTime> oreVisioni = new ArrayList<>();
        WikiDAO w = new WikiimplementazionePostgresDAO();
        try {
            if(utenteLoggato != null)
                w.storicoPagineVisualizzate(utenteLoggato.getLogin(), titoli, dateOreCreazioni, nomi, cognomi, nomiUtente, password, email, dataNascita, dateVisioni, oreVisioni);
            else
                w.storicoPagineVisualizzate(autoreloggato.getLogin(), titoli, dateOreCreazioni, nomi, cognomi, nomiUtente, password, email, dataNascita, dateVisioni, oreVisioni);
            for(int i = 0; i < titoli.size(); i++){
                Pagina pagina = new Pagina(titoli.get(i), dateOreCreazioni.get(i), nomi.get(i), cognomi.get(i), nomiUtente.get(i), password.get(i), email.get(i), dataNascita.get(i));
                if(utenteLoggato != null) {
                    Visiona visiona = new Visiona(dateVisioni.get(i), oreVisioni.get(i), pagina, utenteLoggato);
                }else {
                    Visiona visiona = new Visiona(dateVisioni.get(i), oreVisioni.get(i), pagina, autoreloggato);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return titoli;
    }

    public void addPaginaVisualizzata() {
        WikiDAO w = new WikiimplementazionePostgresDAO();
        try {
            if(utenteLoggato != null || autoreloggato != null) {
                Visiona paginaVisionata = null;
                if (utenteLoggato != null) {
                    paginaVisionata = new Visiona(LocalDate.now(), LocalTime.now(), paginaSelezionata, utenteLoggato);
                    w.addPaginaVisualizzata(paginaSelezionata.getTitolo(),paginaSelezionata.getDataCreazione(),paginaSelezionata.getAutore().getLogin(), utenteLoggato.getLogin());
                } else {
                    paginaVisionata = new Visiona(LocalDate.now(), LocalTime.now(), paginaSelezionata, autoreloggato);
                    w.addPaginaVisualizzata(paginaSelezionata.getTitolo(), paginaSelezionata.getDataCreazione(), paginaSelezionata.getAutore().getLogin(), autoreloggato.getLogin());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> getModificate() {
        ArrayList <String> modifiche = new ArrayList<>();
        ArrayList<String> nomi = new ArrayList<>();
        ArrayList<String> cognomi = new ArrayList<>();
        ArrayList<String> nomiUtente = new ArrayList<>();
        ArrayList<String> password = new ArrayList<>();
        ArrayList<String> email = new ArrayList<>();
        ArrayList<Date> dataNascita = new ArrayList<>();
        ArrayList<LocalDate> dataProposta = new ArrayList<>();
        ArrayList<LocalTime> oraProposta = new ArrayList<>();
        ArrayList<String> stringaInserita = new ArrayList<>();
        ArrayList<Integer> numerazione = new ArrayList<>();
        ArrayList<String> titolo = new ArrayList<>();
        ArrayList<LocalDateTime> dataOraCreazione = new ArrayList<>();
        ArrayList<String> stringaProposta = new ArrayList<>();
        ArrayList<Integer> stato = new ArrayList<>();
        ArrayList<LocalDate> dataValutazione = new ArrayList<>();
        ArrayList<LocalTime> oraValutazione = new ArrayList<>();
        ArrayList<LocalDate>  dataInserimento = new ArrayList<>();
        ArrayList<Time> oraInseriento = new ArrayList<>();

        WikiDAO w = new WikiimplementazionePostgresDAO();
        try {
            if(utenteLoggato != null) {
                w.getModificate(utenteLoggato.getLogin(), titolo, dataOraCreazione, nomi, cognomi, nomiUtente, password, email, dataNascita, dataProposta, oraProposta, stringaInserita, numerazione, stato, stringaProposta, dataValutazione, oraValutazione, dataInserimento, oraInseriento);
                for(int i = 0; i < nomi.size(); i++) {
                    Pagina pagina = new Pagina(titolo.get(i), dataOraCreazione.get(i), nomi.get(i), cognomi.get(i), nomiUtente.get(i), password.get(i), email.get(i), dataNascita.get(i));
                    Frase_Corrente fraseCorrente = new Frase_Corrente(stringaInserita.get(i), numerazione.get(i), pagina, dataInserimento.get(i), oraInseriento.get(i));
                    ModificaProposta modificaProposta = new ModificaProposta(dataProposta.get(i),oraProposta.get(i), pagina.getAutore(), utenteLoggato, fraseCorrente, stringaProposta.get(i), numerazione.get(i), stato.get(i));
                }
            }else{
                for(int i = 0; i < nomi.size(); i++) {
                    w.getModificate(autoreloggato.getLogin(), titolo, dataOraCreazione, nomi, cognomi, nomiUtente, password, email, dataNascita, dataProposta, oraProposta, stringaInserita, numerazione, stato, stringaProposta, dataValutazione, oraValutazione, dataInserimento, oraInseriento);
                    Pagina pagina = new Pagina(titolo.get(i), dataOraCreazione.get(i), nomi.get(i), cognomi.get(i), nomiUtente.get(i), password.get(i), email.get(i), dataNascita.get(i));
                    Frase_Corrente fraseCorrente = new Frase_Corrente(stringaInserita.get(i), numerazione.get(i), pagina, dataInserimento.get(i), oraInseriento.get(i));
                    ModificaProposta modificaProposta = new ModificaProposta(dataProposta.get(i), oraProposta.get(i), pagina.getAutore(), autoreloggato, fraseCorrente, stringaProposta.get(i), numerazione.get(i), stato.get(i));
                }
            }
            int controllo;
            for (int i = 0; i < titolo.size(); i++){
                controllo = 0;
                for(int j = 0; j < modifiche.size(); j++){
                    if(titolo.get(i).equals(modifiche.get(j))){
                        controllo = 1;
                    }
                }
                if(controllo == 0){
                    modifiche.add(titolo.get(i));
                }
            }
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
            notifiche = w.controllaNotifiche();
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

    public ArrayList<String> getTitoliCercati(String titoloInserito){
        ArrayList<String> titoliCercati = new ArrayList<>();
        WikiDAO w = new WikiimplementazionePostgresDAO();
        ArrayList<String> titoli = new ArrayList<>();
        ArrayList<LocalDateTime> dateOreCreazioni = new ArrayList<>();
        ArrayList<String> nomi = new ArrayList<>();
        ArrayList<String> cognomi = new ArrayList<>();
        ArrayList<String> login = new ArrayList<>();
        ArrayList<String> password = new ArrayList<>();
        ArrayList<String> email = new ArrayList<>();
        ArrayList<Date> date = new ArrayList<>();
        try {
            w.ricercaTitoli(titoloInserito, titoli, dateOreCreazioni, nomi, cognomi, login, password, email, date);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < titoli.size(); i++){
            Pagina pagina = new Pagina(titoli.get(i), dateOreCreazioni.get(i), nomi.get(i), cognomi.get(i), login.get(i), password.get(i), email.get(i), date.get(i));
            titoliCercati.add(pagina.getTitolo());
            pagineTrovate.add(pagina);
        }
        return titoliCercati;
    }

    public String getNomeAutore() {
        return paginaSelezionata.getAutore().getNome();
    }

    public String getCognomeAutore() {
        return paginaSelezionata.getAutore().getCognome();
    }

    public String getLoginAutorePaginaSelezionata(){
        return paginaSelezionata.getAutore().getLogin();
    }

    public String getTitoloPaginaSelezionata() {
        return paginaSelezionata.getTitolo();
    }

    public LocalDateTime getDataOraCreazionepaginaSelezionata() {
        return paginaSelezionata.getDataCreazione();
    }

    public String getLoginLoggato() {
        if(utenteLoggato != null){
            return utenteLoggato.getLogin();
        }else
            return autoreloggato.getLogin();
    }

    public void setPaginaVisualizzata(int paginaVisualizzata){
        if(utenteLoggato != null){
            paginaSelezionata = utenteLoggato.getPagineVisualizzate().get(paginaVisualizzata).getPagina();
        }else{
            paginaSelezionata = autoreloggato.getPagineVisualizzate().get(paginaVisualizzata).getPagina();
        }
    }
}
