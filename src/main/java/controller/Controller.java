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
import java.util.Optional;

public class Controller {

    private Utente utenteLoggato = null;
    private Autore autoreloggato = null;
    private ArrayList<Pagina> pagineTrovate = new ArrayList<>();
    private Controller controller ;
    private Pagina paginaSelezionata = null;
    private ArrayList <ModificaProposta> modificheRicevute = new ArrayList<>();
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
                ArrayList<Optional<LocalDate>> datevalutazione = new ArrayList<>();
                ArrayList<Optional<LocalTime>> orevalutazione = new ArrayList<>();
                ArrayList<Integer> stati = new ArrayList<>();
                ArrayList<String> nomi = new ArrayList<>();
                ArrayList<String> cognomi = new ArrayList<>();
                ArrayList<String> logins = new ArrayList<>();
                ArrayList<String> password = new ArrayList<>();
                ArrayList<String> email = new ArrayList<>();
                ArrayList<Date> date = new ArrayList<>();
                WikiDAO w2 = new WikiimplementazionePostgresDAO();

                try {
                    w2.getModificheModificate(paginaSelezionata.getAutore().getLogin(), paginaSelezionata.getTitolo(), paginaSelezionata.getDataCreazione(), fraseCorrente.getStringa_inserita(), fraseCorrente.getNumerazione() ,frasiProposte, dateProposte, oreProposte, datevalutazione, orevalutazione, stati, nomi, cognomi, logins, password, email, date);
                    for(int i = 0; i < frasiProposte.size(); i++){
                        Utente utente = new Utente(nomi.get(i), cognomi.get(i), logins.get(i), password.get(i), email.get(i), date.get(i));
                        ModificaProposta modificaProposta = new ModificaProposta(dateProposte.get(i), oreProposte.get(i), paginaSelezionata.getAutore(), utente, fraseCorrente, frasiProposte.get(i), numerazione, stati.get(i));
                        System.out.println("statooo = " + stati.get(i));
                        if(datevalutazione.get(i).isPresent()) {
                            modificaProposta.setDataValutazione(datevalutazione.get(i).get());
                            modificaProposta.setOraValutazione(orevalutazione.get(i).get());
                        }
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
                System.out.println(fc.getStato() + "   " + fc.getStringa_inserita());
                if(fc.getStato() == 1) {
                    System.out.println("000000000000000000000000000000");
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
                frasiTesto.add(fr_salvata.getStringa_inserita());
                System.out.println("frase scelta:" + fr_salvata.getStringa_inserita());
            }
            controllo = 0;

        }
        return frasiTesto;
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
                    WikiDAO w2 = new WikiimplementazionePostgresDAO();
                    w2.getPagineCreate(login, titoli, dataOraCreazione);
                    autoreloggato = new Autore(nome, cognome,login, password, email, dataNascita, titoli.get(0), dataOraCreazione.get(0));
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
        ArrayList<String> frasi = new ArrayList<>();
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
                frasi.add(sottoStringa);
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
            frasi.add(sottoStringa);
            System.out.println(sottoStringa);
        }

        WikiDAO w = new WikiimplementazionePostgresDAO();
        try {
            if(utenteLoggato != null){
                w.creazionePagina(titolo, frasi, utenteLoggato.getLogin());
            }else{
                w.creazionePagina(titolo, frasi, autoreloggato.getLogin());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
                System.out.println(titoli.get(i));
                System.out.println(dateOreCreazioni.get(i));
                System.out.println(nomi.get(i));
                System.out.println(cognomi.get(i));
                System.out.println(nomiUtente.get(i));
                System.out.println(password.get(i));
                System.out.println(email.get(i));
                System.out.println(dataNascita.get(i));
                System.out.println(dateVisioni.get(i));
                System.out.println(oreVisioni.get(i));

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
                    modifiche.add(titolo.get(i));
                }
            }else{
                w.getModificate(autoreloggato.getLogin(), titolo, dataOraCreazione, nomi, cognomi, nomiUtente, password, email, dataNascita, dataProposta, oraProposta, stringaInserita, numerazione, stato, stringaProposta, dataValutazione, oraValutazione, dataInserimento, oraInseriento);
                for(int i = 0; i < nomi.size(); i++) {
                    Pagina pagina = new Pagina(titolo.get(i), dataOraCreazione.get(i), nomi.get(i), cognomi.get(i), nomiUtente.get(i), password.get(i), email.get(i), dataNascita.get(i));
                    Frase_Corrente fraseCorrente = new Frase_Corrente(stringaInserita.get(i), numerazione.get(i), pagina, dataInserimento.get(i), oraInseriento.get(i));
                    ModificaProposta modificaProposta = new ModificaProposta(dataProposta.get(i), oraProposta.get(i), pagina.getAutore(), autoreloggato, fraseCorrente, stringaProposta.get(i), numerazione.get(i), stato.get(i));
                    modifiche.add(titolo.get(i));
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

    public ArrayList<String> storicoPagineCreate() {
        WikiDAO w = new WikiimplementazionePostgresDAO();
        ArrayList<String> titoli = new ArrayList<>();
        ArrayList<LocalDateTime> dataOraCreazione = new ArrayList<>();
        try {
            w.storicoPagineCreate(autoreloggato.getLogin(), titoli, dataOraCreazione);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for(int i = 0; i < titoli.size(); i++){
            new Pagina(titoli.get(i), dataOraCreazione.get(i), autoreloggato);
        }

        return titoli;
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

    public boolean aggiornaStato(int cambiaStato) {
        boolean controllo = false;

        ModificaProposta modificaProposta = autoreloggato.getNotificheRicevute().get(0).getModifica();
        WikiDAO w = new WikiimplementazionePostgresDAO();
        try {
            controllo = w.aggiornaStato(modificaProposta.getAutore().getLogin(), modificaProposta.getUtente().getLogin(), modificaProposta.getFraseCorrente().getStringa_inserita(), modificaProposta.getStringa_inserita(), modificaProposta.getDataProposta(), modificaProposta.getOraProposta(), cambiaStato);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        autoreloggato.getNotificheRicevute().get(0).getModifica().setStato(cambiaStato);
        autoreloggato.getNotificheRicevute().remove(0);
        return controllo;
    }

    public int contaNotifiche(){
        return autoreloggato.getNotificheRicevute().size();
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
        }else {
            return autoreloggato.getLogin();
        }
    }

    public void setPaginaVisualizzata(int paginaVisualizzata){
        if(utenteLoggato != null){
            paginaSelezionata = utenteLoggato.getPagineVisualizzate().get(paginaVisualizzata).getPagina();
        }else{
            paginaSelezionata = autoreloggato.getPagineVisualizzate().get(paginaVisualizzata).getPagina();
        }
    }

    public void setPaginaCreata(int paginaCreata){
        paginaSelezionata = autoreloggato.getCreazioni().get(paginaCreata);
    }

    public void getNotifche() {
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
        if(utenteLoggato != null) {
            try {
                w.getModifichePagina(utenteLoggato.getLogin(), paginaSelezionata.getTitolo(), paginaSelezionata.getDataCreazione(), nomi, cognomi, nomiUtente, password, email, dataNascita, dataProposta, oraProposta, stringaInserita, numerazione, stato, stringaProposta, dataValutazione, oraValutazione, dataInserimento, oraInseriento);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            for (int i = 0; i < nomi.size(); i++) {
                Utente utente = new Utente(nomi.get(i), cognomi.get(i), nomiUtente.get(i), password.get(i), email.get(i), dataNascita.get(i));
                Frase_Corrente fraseCorrente = new Frase_Corrente(stringaInserita.get(i), numerazione.get(i), paginaSelezionata, dataInserimento.get(i), oraInseriento.get(i));
                ModificaProposta modificaProposta = new ModificaProposta(dataProposta.get(i), oraProposta.get(i), autoreloggato, utente, fraseCorrente, stringaProposta.get(i), numerazione.get(i), stato.get(i));

            }
        }
    }


    public void getModifica() {
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
        if(utenteLoggato != null) {
            try {
                w.getModificheUtente(utenteLoggato.getLogin(), titolo, dataOraCreazione, nomi, cognomi, nomiUtente, password, email, dataNascita, dataProposta, oraProposta, stringaInserita, numerazione, stato, stringaProposta, dataValutazione, oraValutazione, dataInserimento, oraInseriento);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            for (int i = 0; i < nomi.size(); i++) {
                Autore autore = new Autore(nomi.get(i), cognomi.get(i), nomiUtente.get(i), password.get(i), email.get(i), dataNascita.get(i), titolo.get(i), dataOraCreazione.get(i));
                Frase_Corrente fraseCorrente = new Frase_Corrente(stringaInserita.get(i), numerazione.get(i), autore.getCreazioni().getLast(), dataInserimento.get(i), oraInseriento.get(i));
                ModificaProposta modificaProposta = new ModificaProposta(dataProposta.get(i), oraProposta.get(i), autore, utenteLoggato, fraseCorrente, stringaProposta.get(i), numerazione.get(i), stato.get(i));

            }
        }else{
            try {
                w.getModificheUtente(autoreloggato.getLogin(), titolo, dataOraCreazione, nomi, cognomi, nomiUtente, password, email, dataNascita, dataProposta, oraProposta, stringaInserita, numerazione, stato, stringaProposta, dataValutazione, oraValutazione, dataInserimento, oraInseriento);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            for (int i = 0; i < nomi.size(); i++) {
                System.out.println(nomi.get(i));
                System.out.println(cognomi.get(i));
                System.out.println(nomiUtente.get(i));
                System.out.println(password.get(i));
                System.out.println(email.get(i));
                System.out.println(dataNascita.get(i));
                System.out.println(titolo.get(i));
                System.out.println(dataOraCreazione.get(i));
                System.out.println(stringaInserita.get(i));
                System.out.println(numerazione.get(i));
                System.out.println(dataInserimento.get(i));
                System.out.println(oraInseriento.get(i));

                Autore autore = new Autore(nomi.get(i), cognomi.get(i), nomiUtente.get(i), password.get(i), email.get(i), dataNascita.get(i), titolo.get(i), dataOraCreazione.get(i));
                Frase_Corrente fraseCorrente = new Frase_Corrente(stringaInserita.get(i), numerazione.get(i), autore.getCreazioni().getLast(), dataInserimento.get(i), oraInseriento.get(i));
                ModificaProposta modificaProposta = new ModificaProposta(dataProposta.get(i), oraProposta.get(i), autore, autoreloggato, fraseCorrente, stringaProposta.get(i), numerazione.get(i), stato.get(i));

            }
        }
    }

    public void frasiModificheTesto(Pagina pagina){
        WikiDAO w = new WikiimplementazionePostgresDAO();
        ArrayList<String> stringaInserita = new ArrayList<>();
        ArrayList<LocalDate>  dataInserimento = new ArrayList<>();
        ArrayList<Time> oraInseriento = new ArrayList<>();
        try {
            w.getFrasiCorrenti(autoreloggato.getLogin(), pagina.getTitolo(), pagina.getDataCreazione(), stringaInserita, dataInserimento, oraInseriento);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for(int i = 0; i < stringaInserita.size(); i++){
            pagina.addFrasi(new Frase_Corrente(stringaInserita.get(i), i, pagina, dataInserimento.get(i), oraInseriento.get(i)));
            ArrayList<String> nomi = new ArrayList<>();
            ArrayList<String> cognomi = new ArrayList<>();
            ArrayList<String> nomiUtente = new ArrayList<>();
            ArrayList<String> password = new ArrayList<>();
            ArrayList<String> email = new ArrayList<>();
            ArrayList<Date> dataNascita = new ArrayList<>();
            ArrayList<LocalDate> dataProposta = new ArrayList<>();
            ArrayList<LocalTime> oraProposta = new ArrayList<>();
            ArrayList<String> titolo = new ArrayList<>();
            ArrayList<LocalDateTime> dataOraCreazione = new ArrayList<>();
            ArrayList<String> stringaProposta = new ArrayList<>();
            ArrayList<Integer> stato = new ArrayList<>();
            ArrayList<LocalDate> dataValutazione = new ArrayList<>();
            ArrayList<LocalTime> oraValutazione = new ArrayList<>();
            try {
                w.getModificheFrase(pagina.getTitolo(), pagina.getDataCreazione(), stringaInserita.get(i), i, nomi, cognomi, nomiUtente, password, email, dataNascita, dataProposta, oraProposta, stato, stringaProposta, dataValutazione, oraValutazione);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            for(int j = 0; j < nomi.size(); j++){
                Utente utente = new Utente(nomi.get(i), cognomi.get(i), nomiUtente.get(i), password.get(i), email.get(i), dataNascita.get(i));
                ModificaProposta modificaProposta = new ModificaProposta(dataProposta.get(i), oraProposta.get(i), pagina.getAutore(), utente, pagina.getFrasi().get(i), stringaProposta.get(i), i, stato.get(i));
            }
        }

    }

    public String getTitoloNotifica() {
        return autoreloggato.getNotificheRicevute().get(0).getTitolo();
    }

    public String getStringaSelezionata() {
        WikiDAO w = new WikiimplementazionePostgresDAO();
        paginaSelezionata = autoreloggato.getNotificheRicevute().get(0).getModifica().getPagina();
        try {
            return w.getFraseSelezionata(autoreloggato.getLogin(), autoreloggato.getNotificheRicevute().get(0).getModifica().getUtente().getLogin(), autoreloggato.getNotificheRicevute().get(0).getModifica().getStringa_inserita(),  autoreloggato.getNotificheRicevute().get(0).getModifica().getFraseCorrente().getStringa_inserita(),  autoreloggato.getNotificheRicevute().get(0).getModifica().getDataProposta(),  autoreloggato.getNotificheRicevute().get(0).getModifica().getOraProposta());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getFraseproposta() {
        return autoreloggato.getNotificheRicevute().get(0).getModifica().getStringa_inserita();
    }

    public ArrayList<String> getFrasiSelezionate() {
        ArrayList<String> frasiSelezionate = new ArrayList<>();
        getModifica();

        if (utenteLoggato != null) {
            for (ModificaProposta f : utenteLoggato.getFrasiProposte()) {
                WikiDAO w = new WikiimplementazionePostgresDAO();
                try {
                    frasiSelezionate.add(w.getFraseSelezionata(f.getAutore().getLogin(), utenteLoggato.getLogin(), f.getStringa_inserita(), f.getFraseCorrente().getStringa_inserita(), f.getDataProposta(), f.getOraProposta()));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            for (ModificaProposta f : autoreloggato.getFrasiProposte()) {
                WikiDAO w = new WikiimplementazionePostgresDAO();
                try {
                    frasiSelezionate.add(w.getFraseSelezionata(f.getAutore().getLogin(), autoreloggato.getLogin(), f.getStringa_inserita(), f.getFraseCorrente().getStringa_inserita(), f.getDataProposta(), f.getOraProposta()));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        return frasiSelezionate;
    }

    public ArrayList<String> getFrasiproposte() {
        ArrayList<String> frasi = new ArrayList<>();
        if(utenteLoggato != null){
            for(ModificaProposta f : utenteLoggato.getFrasiProposte()) {
                frasi.add(f.getStringa_inserita());
            }
        }else {
            for (ModificaProposta f : autoreloggato.getFrasiProposte()) {
                frasi.add(f.getStringa_inserita());
            }
        }
        return frasi;
    }

    public ArrayList<Integer> getstati() {
        ArrayList<Integer> stati = new ArrayList<>();
        if(utenteLoggato != null){
            for(ModificaProposta f : utenteLoggato.getFrasiProposte()) {
                stati.add(f.getStato());
            }
        }else {
            for (ModificaProposta f : autoreloggato.getFrasiProposte()) {
                stati.add(f.getStato());
            }
        }
        return stati;
    }
}
