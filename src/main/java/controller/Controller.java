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

    private String titoloSelezionato = null;
    private ArrayList<Pagina> pagineTrovate = new ArrayList<>();
    private Controller controller ;
    private Pagina paginaSelezionata = null;
    private ArrayList<Pagina> pagineModificateUtente = new ArrayList<>();
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

    public void setTitoloSelezionato(String titoloSelezionato) {
        this.titoloSelezionato = titoloSelezionato;
    }

    public String getTitoloSelezionato() {
        return titoloSelezionato;
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
        for(Frase_Corrente f : paginaSelezionata.getFrasi()){

        }
        try {
            if(paginaSelezionata.getFrasi().isEmpty()) {
                w.getFrasiCorrenti(paginaSelezionata.getAutore().getLogin(), paginaSelezionata.getTitolo(), paginaSelezionata.getDataCreazione(), frasiInserite, dateInserimento, oreInserimento);
                int numerazione = 0;
                for (String frase : frasiInserite) {
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
                        w2.getModificheModificate(paginaSelezionata.getAutore().getLogin(), paginaSelezionata.getTitolo(), paginaSelezionata.getDataCreazione(), fraseCorrente.getStringa_inserita(), fraseCorrente.getNumerazione(), frasiProposte, dateProposte, oreProposte, datevalutazione, orevalutazione, stati, nomi, cognomi, logins, password, email, date);
                        for (int i = 0; i < frasiProposte.size(); i++) {

                            Utente utente = new Utente(nomi.get(i), cognomi.get(i), logins.get(i), password.get(i), email.get(i), date.get(i));
                            ModificaProposta modificaProposta = new ModificaProposta(dateProposte.get(i), oreProposte.get(i), paginaSelezionata.getAutore(), utente, fraseCorrente, frasiProposte.get(i), numerazione, stati.get(i));
                            if (datevalutazione.get(i).isPresent()) {
                                modificaProposta.setDataValutazione(datevalutazione.get(i).get());
                                modificaProposta.setOraValutazione(orevalutazione.get(i).get());
                            }
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    numerazione++;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ArrayList<String> frasiTesto= new ArrayList<>();
        int controllo = 0;
        Frase fr_salvata = null;

        for(Frase_Corrente f : paginaSelezionata.getFrasi()){
            LocalDate data_max = f.getDataInserimento();
            LocalTime oraMax = f.getOraInserimento().toLocalTime();
            for(ModificaProposta fc : f.getProposte()){
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
            if(controllo == 0){
                frasiTesto.add(f.getStringa_inserita());
            }else{
                frasiTesto.add(fr_salvata.getStringa_inserita());
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
                    for(int i = 0; i < autoreloggato.getCreazioni().size(); i++){
                        System.out.println("][][][][][][" + autoreloggato.getCreazioni().get(i).getTitolo());
                    }
                    for(int i = 1; i < titoli.size(); i++){
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
                    if((mp.getDataValutazione().isAfter(dataMax) || (mp.getDataValutazione().isEqual(dataMax)) && mp.getOraValutazione().isAfter(oraMax))){
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
        ModificaProposta modifica = null;
        int presenzaFrase = 0;
        for(Frase_Corrente f : paginaSelezionata.getFrasi()){
            if(f.getStringa_inserita().equals(fraseSelezionata)){
                presenzaFrase = 1;
            }
        }

        if(presenzaFrase == 0) {
            for (Frase_Corrente f : paginaSelezionata.getFrasi()) {
                for (ModificaProposta m : f.getProposte()) {
                    if (m.getStringa_inserita().equals(fraseSelezionata)) {
                        fraseSelezionata = m.getFraseCorrente().getStringa_inserita();
                        break;
                    }
                }
            }
        }
        try {
            if(utenteLoggato != null) {
                w.inviaProposta(numerazione, fraseSelezionata, fraseProposta, utenteLoggato.getLogin(), paginaSelezionata.getAutore().getLogin(), paginaSelezionata.getTitolo(), paginaSelezionata.getDataCreazione());
                modifica = new ModificaProposta(LocalDate.now(), LocalTime.now(), paginaSelezionata.getAutore(), utenteLoggato, paginaSelezionata.getFrasi().get(numerazione), fraseProposta, numerazione);
                if(paginaSelezionata.getAutore().getLogin().equals(utenteLoggato.getLogin())){
                    modifica.setStato(1);
                    modifica.setDataValutazione(LocalDate.now());
                    modifica.setOraValutazione(LocalTime.now());
                }
            }else {
                w.inviaProposta(numerazione, fraseSelezionata, fraseProposta, autoreloggato.getLogin(), paginaSelezionata.getAutore().getLogin(), paginaSelezionata.getTitolo(), paginaSelezionata.getDataCreazione());
                modifica = new ModificaProposta(LocalDate.now(), LocalTime.now(), paginaSelezionata.getAutore(), autoreloggato, paginaSelezionata.getFrasi().get(numerazione), fraseProposta, numerazione);
                if(paginaSelezionata.getAutore().getLogin().equals(autoreloggato.getLogin())){
                    modifica.setStato(1);
                    modifica.setDataValutazione(LocalDate.now());
                    modifica.setOraValutazione(LocalTime.now());
                }
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
        LocalDateTime dataCreazione = LocalDateTime.now();
        if(autoreloggato != null){
            paginaCreata = new Pagina(titolo, dataCreazione, autoreloggato);
        }else{
            paginaCreata = new Pagina(titolo,dataCreazione, utenteLoggato.getNome(), utenteLoggato.getCognome(), utenteLoggato.getLogin(), utenteLoggato.getPassword(), utenteLoggato.getEmail(), utenteLoggato.getDataNascita());
            autoreloggato = paginaCreata.getAutore();
            utenteLoggato = null;
        }
        paginaSelezionata = paginaCreata;

        int length = testo.length();
        int prec = 0;
        int num = 0;
        String sottoStringa;
        for (int i = 0; i < length; i++) {
            if (testo.charAt(i) == ',' || testo.charAt(i) == '.' || testo.charAt(i) == ';'|| testo.charAt(i) == '?' || testo.charAt(i) == '!') {
                int j = i+1;
                while(j < length && testo.charAt(i) == testo.charAt(j)){
                    i++;
                    j++;
                }
                sottoStringa = testo.substring(prec, i+1);
                int contaSpaziVuoti = 0;
                if(!(sottoStringa.equals(""))) {
                    while (sottoStringa.charAt(contaSpaziVuoti) == ' ') {
                        contaSpaziVuoti++;
                    }
                    sottoStringa = sottoStringa.substring(contaSpaziVuoti);
                    Frase_Corrente fraseCorrente = new Frase_Corrente(sottoStringa, num, paginaCreata, LocalDate.now(), Time.valueOf(LocalTime.now()));
                    frasi.add(sottoStringa);
                    num++;
                }
                prec = i+1;
            }
        }
        if(!(testo.charAt(length-1) == '.'|| testo.charAt(length-1) == '?' || testo.charAt(length-1) == '!' || testo.charAt(length-1) == ';' || testo.charAt(length-1) == ',')){
            sottoStringa = testo.substring(prec, length);
            int contaSpaziVuoti = 0;
            while(sottoStringa.charAt(contaSpaziVuoti) == ' '){
                contaSpaziVuoti++;
            }
            sottoStringa = sottoStringa.substring(contaSpaziVuoti);
            Frase_Corrente fraseCorrente = new Frase_Corrente(sottoStringa, num, paginaCreata, LocalDate.now(), Time.valueOf(LocalTime.now()));
            frasi.add(sottoStringa);
        }

        WikiDAO w = new WikiimplementazionePostgresDAO();
        try {
            w.creazionePagina(titolo, frasi, autoreloggato.getLogin(), dataCreazione);
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
            else if(autoreloggato != null)
                w.storicoPagineVisualizzate(autoreloggato.getLogin(), titoli, dateOreCreazioni, nomi, cognomi, nomiUtente, password, email, dataNascita, dateVisioni, oreVisioni);

            for(int i = 0; i < titoli.size(); i++){
                Pagina pagina = new Pagina(titoli.get(i), dateOreCreazioni.get(i), nomi.get(i), cognomi.get(i), nomiUtente.get(i), password.get(i), email.get(i), dataNascita.get(i));
                if(utenteLoggato != null) {
                    Visiona visiona = new Visiona(dateVisioni.get(i), oreVisioni.get(i), pagina, utenteLoggato);
                }else if(autoreloggato != null) {
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
        pagineModificateUtente = new ArrayList<>();
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
        ArrayList<Optional<LocalDate>> datevalutazione = new ArrayList<>();
        ArrayList<Optional<LocalTime>> orevalutazione = new ArrayList<>();
        ArrayList<LocalDate>  dataInserimento = new ArrayList<>();
        ArrayList<Time> oraInseriento = new ArrayList<>();

        WikiDAO w = new WikiimplementazionePostgresDAO();
        try {
            if(utenteLoggato != null) {
                w.getModificate(utenteLoggato.getLogin(), titolo, dataOraCreazione, nomi, cognomi, nomiUtente, password, email, dataNascita, dataProposta, oraProposta, stringaInserita, numerazione, stato, stringaProposta, datevalutazione, orevalutazione, dataInserimento, oraInseriento);
                for(int i = 0; i < nomi.size(); i++) {
                    Pagina pagina = new Pagina(titolo.get(i), dataOraCreazione.get(i), nomi.get(i), cognomi.get(i), nomiUtente.get(i), password.get(i), email.get(i), dataNascita.get(i));
                    Frase_Corrente fraseCorrente = new Frase_Corrente(stringaInserita.get(i), numerazione.get(i), pagina, dataInserimento.get(i), oraInseriento.get(i));
                    ModificaProposta modificaProposta = new ModificaProposta(dataProposta.get(i),oraProposta.get(i), pagina.getAutore(), utenteLoggato, fraseCorrente, stringaProposta.get(i), numerazione.get(i), stato.get(i));
                    if(datevalutazione.get(i).isPresent()) {
                        modificaProposta.setDataValutazione(datevalutazione.get(i).get());
                        modificaProposta.setOraValutazione(orevalutazione.get(i).get());
                    }
                }
                int controllo;
                for (int i = 0; i < utenteLoggato.getFrasiProposte().size(); i++){
                    controllo = 0;
                    for(int j = 0; j < pagineModificateUtente.size(); j++){
                        if(pagineModificateUtente.get(j).getTitolo().equals(utenteLoggato.getFrasiProposte().get(i).getFraseCorrente().getPagina().getTitolo()) && pagineModificateUtente.get(j).getDataCreazione().equals(utenteLoggato.getFrasiProposte().get(i).getFraseCorrente().getPagina().getDataCreazione())){
                            controllo = 1;
                        }
                    }
                    if(controllo == 0){
                        modifiche.add(utenteLoggato.getFrasiProposte().get(i).getFraseCorrente().getPagina().getTitolo());
                        pagineModificateUtente.add(utenteLoggato.getFrasiProposte().get(i).getFraseCorrente().getPagina());
                    }
                }
            }else{
                w.getModificate(autoreloggato.getLogin(), titolo, dataOraCreazione, nomi, cognomi, nomiUtente, password, email, dataNascita, dataProposta, oraProposta, stringaInserita, numerazione, stato, stringaProposta, datevalutazione, orevalutazione, dataInserimento, oraInseriento);
                for(int i = 0; i < nomi.size(); i++) {
                    Pagina pagina = new Pagina(titolo.get(i), dataOraCreazione.get(i), nomi.get(i), cognomi.get(i), nomiUtente.get(i), password.get(i), email.get(i), dataNascita.get(i));
                    Frase_Corrente fraseCorrente = new Frase_Corrente(stringaInserita.get(i), numerazione.get(i), pagina, dataInserimento.get(i), oraInseriento.get(i));
                    ModificaProposta modificaProposta = new ModificaProposta(dataProposta.get(i), oraProposta.get(i), pagina.getAutore(), autoreloggato, fraseCorrente, stringaProposta.get(i), numerazione.get(i), stato.get(i));
                    if(datevalutazione.get(i).isPresent()) {
                        modificaProposta.setDataValutazione(datevalutazione.get(i).get());
                        modificaProposta.setOraValutazione(orevalutazione.get(i).get());
                    }
                }
                int controllo;
                for (int i = 0; i < autoreloggato.getFrasiProposte().size(); i++){
                    System.out.println("----------------**************" + pagineModificateUtente.size());
                    controllo = 0;
                    for(int j = 0; j < pagineModificateUtente.size(); j++){
                        if(pagineModificateUtente.get(j).getTitolo().equals(autoreloggato.getFrasiProposte().get(i).getFraseCorrente().getPagina().getTitolo()) && pagineModificateUtente.get(j).getDataCreazione().equals(autoreloggato.getFrasiProposte().get(i).getFraseCorrente().getPagina().getDataCreazione())){
                            controllo = 1;
                        }
                    }
                    if(controllo == 0){
                        modifiche.add(autoreloggato.getFrasiProposte().get(i).getFraseCorrente().getPagina().getTitolo());
                        pagineModificateUtente.add(autoreloggato.getFrasiProposte().get(i).getFraseCorrente().getPagina());
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return modifiche;
    }

    public ArrayList<String> storicoPagineCreate() {
        ArrayList<String> titoli = new ArrayList<>();
        System.out.println("==================================================================");
        for(int i = 0; i < autoreloggato.getCreazioni().size(); i++){
            titoli.add(autoreloggato.getCreazioni().get(i).getTitolo());
            System.out.println(titoli.get(i));
        }
        System.out.println("==================================================================");

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
        System.out.println("---------------------------- " + autoreloggato.getNotificheRicevute().size());
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
            return  autoreloggato.getLogin();
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
        ArrayList<Optional<LocalDate>> datavalutazione = new ArrayList<>();
        ArrayList<Optional<LocalTime>> oraValutazione = new ArrayList<>();
        ArrayList<LocalDate> dataInserimento = new ArrayList<>();
        ArrayList<Time> oraInseriento = new ArrayList<>();

        if (autoreloggato != null) {
            for (int j = 0; j < autoreloggato.getCreazioni().size(); j++) {
                String titoloPagina = autoreloggato.getCreazioni().get(j).getTitolo();
                LocalDateTime dataOraCreazionePagina = autoreloggato.getCreazioni().get(j).getDataCreazione();
                WikiDAO w = new WikiimplementazionePostgresDAO();
                try {
                    w.getModifichePagina(autoreloggato.getLogin(), titoloPagina, dataOraCreazionePagina, nomi, cognomi, nomiUtente, password, email, dataNascita, dataProposta, oraProposta, stringaInserita, numerazione, stato, stringaProposta, datavalutazione, oraValutazione, dataInserimento, oraInseriento);

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < nomi.size(); i++) {
                    if(stato.get(i) == 0) {
                        Utente utente = new Utente(nomi.get(i), cognomi.get(i), nomiUtente.get(i), password.get(i), email.get(i), dataNascita.get(i));
                        System.out.println(stringaInserita.get(i));
                        System.out.println(numerazione.get(i));
                        System.out.println(dataInserimento.get(i));
                        System.out.println(oraInseriento.get(i));
                        Frase_Corrente fraseCorrente = new Frase_Corrente(stringaInserita.get(i), numerazione.get(i), autoreloggato.getCreazioni().get(j), dataInserimento.get(i), oraInseriento.get(i));
                        ModificaProposta modificaProposta = new ModificaProposta(dataProposta.get(i), oraProposta.get(i), autoreloggato, utente, fraseCorrente, stringaProposta.get(i), numerazione.get(i), stato.get(i));
                        Notifica notifica = new Notifica(autoreloggato, modificaProposta, autoreloggato.getCreazioni().get(j).getTitolo());
                    }
                }
            }
        }
    }

    public void removeNotifica(){
        autoreloggato.getNotificheRicevute().remove(0);
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
                utenteLoggato.setFrasiProposte(new ArrayList<>());
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
                autoreloggato.setFrasiProposte(new ArrayList<>());
                w.getModificheUtente(autoreloggato.getLogin(), titolo, dataOraCreazione, nomi, cognomi, nomiUtente, password, email, dataNascita, dataProposta, oraProposta, stringaInserita, numerazione, stato, stringaProposta, dataValutazione, oraValutazione, dataInserimento, oraInseriento);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            for (int i = 0; i < nomi.size(); i++) {
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

    public ArrayList<String> getFrasiSelezionate(int numPaginaSelezionata) {
        ArrayList<String> frasiSelezionate = new ArrayList<>();

        ArrayList<ModificaProposta> modifichePagina = new ArrayList<>();

        if (utenteLoggato != null) {
            for(ModificaProposta f : utenteLoggato.getFrasiProposte()){
                System.out.println(f.getFraseCorrente().getPagina().getTitolo() + " vs " + pagineModificateUtente.get(numPaginaSelezionata).getTitolo());
                System.out.println(f.getFraseCorrente().getPagina().getDataCreazione() + " vs " + pagineModificateUtente.get(numPaginaSelezionata).getDataCreazione());
                if(f.getFraseCorrente().getPagina().getTitolo().equals(pagineModificateUtente.get(numPaginaSelezionata).getTitolo()) && f.getFraseCorrente().getPagina().getDataCreazione().equals(pagineModificateUtente.get(numPaginaSelezionata).getDataCreazione())){
                    modifichePagina.add(f);
                    System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + modifichePagina);
                }
            }
            
            for (ModificaProposta modificheUtente : modifichePagina) {
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>" + modificheUtente.getStringa_inserita());
                Pagina pagina = modificheUtente.getFraseCorrente().getPagina();
                for (Frase_Corrente f : pagina.getFrasi()) {
                    if (f.getNumerazione() == modificheUtente.getFraseCorrente().getNumerazione()) {
                        String fraseTemp = f.getStringa_inserita();
                        LocalDate dataProposta = modificheUtente.getDataProposta();
                        Time oraProposta = Time.valueOf(modificheUtente.getOraProposta());
                        LocalDate dataMax = null;
                        Time oraMax = null;
                        int primaModifica = 0;
                        for (ModificaProposta m : f.getProposte()) {
                            if(m.getStato() == 1 && primaModifica == 0){
                                primaModifica = 1;
                                dataMax = m.getDataValutazione();
                                oraMax = Time.valueOf(m.getOraValutazione());
                            }
                            if (m.getStato() == 1 && (m.getDataValutazione().isBefore(dataProposta) || m.getDataValutazione().equals(dataProposta)) && m.getOraValutazione().isBefore(oraProposta.toLocalTime())) {
                                if(m.getDataValutazione().isAfter(dataMax) || m.getDataValutazione().equals(dataMax) && m.getOraValutazione().isAfter(oraMax.toLocalTime())){
                                    dataMax = m.getDataValutazione();
                                    oraMax = Time.valueOf(m.getOraValutazione());
                                    fraseTemp = m.getStringa_inserita();
                                    System.out.println("3333333333333333333333333" + fraseTemp);
                                }
                            }
                        }
                        frasiSelezionate.add(fraseTemp);
                    }
                }
            }
        } else {
            for(ModificaProposta f : autoreloggato.getFrasiProposte()){
               if(f.getFraseCorrente().getPagina().getTitolo().equals(pagineModificateUtente.get(numPaginaSelezionata).getTitolo()) && f.getFraseCorrente().getPagina().getDataCreazione().equals(pagineModificateUtente.get(numPaginaSelezionata).getDataCreazione())){
                    modifichePagina.add(f);
                    System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + f.getStringa_inserita());
                }
            }
            int controllo = 0;
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
            for (ModificaProposta modificheUtente : modifichePagina) {
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>" + modificheUtente.getStringa_inserita());
                Pagina pagina = modificheUtente.getFraseCorrente().getPagina();
                for (Frase_Corrente f : pagina.getFrasi()) {
                    if (f.getNumerazione() == modificheUtente.getFraseCorrente().getNumerazione()) {
                        String fraseTemp = f.getStringa_inserita();
                        LocalDate dataProposta = modificheUtente.getDataProposta();
                        Time oraProposta = Time.valueOf(modificheUtente.getOraProposta());
                        LocalDate dataMax = null;
                        Time oraMax = null;
                        int primaModifica = 0;
                        for (ModificaProposta m : f.getProposte()) {
                            if (primaModifica == 0 && m.getStato() == 1 && (m.getDataValutazione().isBefore(dataProposta) || m.getDataValutazione().equals(dataProposta)) && m.getOraValutazione().isBefore(oraProposta.toLocalTime())) {
                                primaModifica = 1;
                                dataMax = m.getDataValutazione();
                                oraMax = Time.valueOf(m.getOraValutazione());
                                fraseTemp = m.getStringa_inserita();
                            }else if (m.getStato() == 1 && (m.getDataValutazione().isBefore(dataProposta) || m.getDataValutazione().equals(dataProposta)) && m.getOraValutazione().isBefore(oraProposta.toLocalTime())) {
                                if(m.getDataValutazione().isAfter(dataMax) || m.getDataValutazione().equals(dataMax) && m.getOraValutazione().isAfter(oraMax.toLocalTime())){
                                    dataMax = m.getDataValutazione();
                                    oraMax = Time.valueOf(m.getOraValutazione());
                                    fraseTemp = m.getStringa_inserita();
                                    System.out.println("3333333333333333333333333" + fraseTemp);
                                }
                            }
                        }
                        frasiSelezionate.add(fraseTemp);
                    }
                }
            }

//            for (ModificaProposta f : modifichePagina) {
//                WikiDAO w = new WikiimplementazionePostgresDAO();
//                try {
//                    frasiSelezionate.add(w.getFraseSelezionata(f.getAutore().getLogin(), autoreloggato.getLogin(), f.getStringa_inserita(), f.getFraseCorrente().getStringa_inserita(), f.getDataProposta(), f.getOraProposta()));
//                } catch (SQLException e) {
//                    throw new RuntimeException(e);
//                }
//
//            }
        }
        return frasiSelezionate;
    }

    public ArrayList<String> getFrasiproposte(int numPaginaSelezionata) {
        ArrayList<String> frasi = new ArrayList<>();
        if(utenteLoggato != null){
            for(ModificaProposta f : utenteLoggato.getFrasiProposte()) {
                if(f.getFraseCorrente().getPagina().getTitolo().equals(pagineModificateUtente.get(numPaginaSelezionata).getTitolo()) && f.getFraseCorrente().getPagina().getDataCreazione().equals(pagineModificateUtente.get(numPaginaSelezionata).getDataCreazione())){
                    frasi.add(f.getStringa_inserita());
                }


            }
        }else {
            for (ModificaProposta f : autoreloggato.getFrasiProposte()) {
                if(f.getFraseCorrente().getPagina().getTitolo().equals(pagineModificateUtente.get(numPaginaSelezionata).getTitolo()) && f.getFraseCorrente().getPagina().getDataCreazione().equals(pagineModificateUtente.get(numPaginaSelezionata).getDataCreazione())){
                    frasi.add(f.getStringa_inserita());
                }
            }
        }
        return frasi;
    }

    public ArrayList<Integer> getstati(int numPaginaSelezionata) {
        ArrayList<Integer> stati = new ArrayList<>();
        if(utenteLoggato != null){
            for(ModificaProposta f : utenteLoggato.getFrasiProposte()) {
                if(f.getFraseCorrente().getPagina().getTitolo().equals(pagineModificateUtente.get(numPaginaSelezionata).getTitolo()) && f.getFraseCorrente().getPagina().getDataCreazione().equals(pagineModificateUtente.get(numPaginaSelezionata).getDataCreazione())){
                    stati.add(f.getStato());
                }

            }
        }else {
            for (ModificaProposta f : autoreloggato.getFrasiProposte()) {
                if(f.getFraseCorrente().getPagina().getTitolo().equals(pagineModificateUtente.get(numPaginaSelezionata).getTitolo()) && f.getFraseCorrente().getPagina().getDataCreazione().equals(pagineModificateUtente.get(numPaginaSelezionata).getDataCreazione())){
                    stati.add(f.getStato());
                }
            }
        }
        return stati;
    }


    public void logout() {
        if(utenteLoggato != null)
            utenteLoggato = null;
        else
            autoreloggato = null;

    }
}
