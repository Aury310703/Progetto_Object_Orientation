package controller;

import GUI.Notifiche;
import MODEL.*;
import dao.WikiDAO;
import implementazionePostgresDAO.WikiimplementazionePostgresDAO;

import java.sql.SQLException;
import java.sql.Time;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

/**
 * Controller responsabile della realizzazione delle funzionalità del sistema e viene usato per far comunicare la GUI con il Model.
 */
public class Controller {

    private Utente utenteLoggato = null;
    private Autore autoreloggato = null;
    private String titoloSelezionato  = null;;
    private ArrayList<Pagina> pagineTrovate;
    private Pagina paginaSelezionata  = null;
    private Pagina SalvaVecchiaPaginaSelezionata  = null;
    private ArrayList<Pagina> pagineModificateUtente;
    private  boolean controlloVersione = false;
    private LocalDate dataVersioneSelezionata = null;

    /**
     *Istanzia un nuovo Controller.
     */
    public Controller(){

    }

    /**
     * Questo metodo serve a verificare con quale ruolo sta accedeno l'utente al sistema (utente o autore).
     *
     * @return ruolo
     */
    public int verificaRuoloUtente(){
        if(utenteLoggato != null){
            return 1;
        }else if(autoreloggato != null){
            return 2;
        }
        return 0;
    }

    /**
     * Setta il titolo selezionato dall'utente nel momento della ricerca.
     *
     * @param titoloSelezionato indica il titolo selezionato dall'utente
     */
    public void setTitoloSelezionato(String titoloSelezionato) {
        this.titoloSelezionato = titoloSelezionato;
    }

    /**
     * Restituisce il titolo selezionato dall'utente al momento della ricerca.
     *
     * @return titoloSelezionato
     */
    public String getTitoloSelezionato() {
        return titoloSelezionato;
    }

    /**
     * Restituisce il nome dell'utente loggato.

     * @return nome
     */
    public String getNomeUtenteLoggato(){
        return utenteLoggato.getNome();
    }

    /**
     * Restituisce il cognome dell'utente loggato.
     *
     * @return cognome
     */
    public String getCognomeUtenteLoggato(){
        return utenteLoggato.getCognome();
    }

    /**
     * Restituisce un ArrayList di frasi che corrisponde all'insieme delle frasi del testo della pagina wiki selezionata dall'utente. Le frasi verranno "scelte" in base a
     * se si sta componendo la versione piú recente o se si sta componendo una versione precedente del testo. In base alla scelta verrá verificato se una frase ha delle
     * modifiche accettate, a quel punto se ci sono verrá presa l'ultima modifica.
     *
     * @return frasiTesto
     */
    public ArrayList<String> getTestoPagina() {
        WikiDAO w = new WikiimplementazionePostgresDAO();
        ArrayList<String> frasiInserite = new ArrayList<>();
        ArrayList<LocalDate> dateInserimento = new ArrayList<>();
        ArrayList<Time> oreInserimento = new ArrayList<>();

        ArrayList<String> titoloCollegata = new ArrayList<>();
        ArrayList<LocalDateTime> dataOraCereazioneCollegata = new ArrayList<>();
        ArrayList<String> nomiCollegata = new ArrayList<>();
        ArrayList<String> cognomiCollegata = new ArrayList<>();
        ArrayList<String> loginCollegata = new ArrayList<>();
        ArrayList<String> passwordCollegata = new ArrayList<>();
        ArrayList<String> emailCollegata = new ArrayList<>();
        ArrayList<Date> dateCollegata = new ArrayList<>();
        ArrayList<Integer> numCollegata = new ArrayList<>();

        try {
            if (paginaSelezionata.getFrasi().isEmpty()) {
                w.getFrasiCorrenti(paginaSelezionata.getAutore().getLogin(), paginaSelezionata.getTitolo(), paginaSelezionata.getDataCreazione(), frasiInserite, dateInserimento, oreInserimento, titoloCollegata, dataOraCereazioneCollegata, nomiCollegata, cognomiCollegata, loginCollegata, passwordCollegata, emailCollegata, dateCollegata, numCollegata);
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

                    if(!nomiCollegata.isEmpty()){
                        for(int i = 0; i < nomiCollegata.size(); i++){
                            if(numCollegata.get(i) == numerazione){
                                Pagina paginaCollegata = new Pagina(titoloCollegata.get(i), dataOraCereazioneCollegata.get(i), nomiCollegata.get(i), cognomiCollegata.get(i), loginCollegata.get(i), passwordCollegata.get(i), emailCollegata.get(i), dateCollegata.get(i));
                                fraseCorrente.setPaginaCollegata(paginaCollegata);
                            }
                        }

                    }

                    WikiDAO w2 = new WikiimplementazionePostgresDAO();
                    try {
                        w2.getModificheModificate(paginaSelezionata.getAutore().getLogin(), paginaSelezionata.getTitolo(), paginaSelezionata.getDataCreazione(), fraseCorrente.getStringa_inserita(), fraseCorrente.getNumerazione(), frasiProposte, dateProposte, oreProposte, datevalutazione, orevalutazione, stati, nomi, cognomi, logins, password, email, date);
                        for (int i = 0; i < frasiProposte.size(); i++) {
                            Utente utente = new Utente(nomi.get(i), cognomi.get(i), logins.get(i), password.get(i), email.get(i), date.get(i));
                            ModificaProposta modificaProposta = new ModificaProposta(dateProposte.get(i), oreProposte.get(i), paginaSelezionata.getAutore(), utente, fraseCorrente, frasiProposte.get(i), stati.get(i));
                            if(autoreloggato != null) {
                                for (int j = 0; j < autoreloggato.getNotificheRicevute().size(); j++) {
                                    if (modificaProposta.equals(autoreloggato.getNotificheRicevute().get(j).getModifica())) {
                                        autoreloggato.getNotificheRicevute().remove(j);
                                    }
                                }
                            }

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
        } catch(SQLException e){
            throw new RuntimeException(e);
        }

        ArrayList<String> frasiTesto= new ArrayList<>();
        int controllo = 0;
        Frase fr_salvata = null;
        boolean dataPrecedente = false;
        if(controlloVersionePrecedente()) {
            if (dataVersioneSelezionata.isBefore(paginaSelezionata.getDataCreazione().toLocalDate())) {
                dataPrecedente = true;
            }
            if (dataVersioneSelezionata.isBefore(paginaSelezionata.getDataCreazione().toLocalDate())) {
                dataPrecedente = true;
            }
        }
            for (Frase_Corrente f : paginaSelezionata.getFrasi()) {
                if (dataPrecedente) {
                    frasiTesto.add(f.getStringa_inserita());
                }else{
                    LocalDate data_max = f.getDataInserimento();
                    LocalTime oraMax = f.getOraInserimento().toLocalTime();
                    for (ModificaProposta fc : f.getProposte()) {
                        if (fc.getStato() == 1) {
                            LocalDate dataModifica = null;
                            LocalTime oraModifica = null;
                            if(controlloVersionePrecedente()){
                                if(fc.getDataValutazione().isBefore(dataVersioneSelezionata) || fc.getDataValutazione().equals(dataVersioneSelezionata)){
                                    controllo = 1;
                                    dataModifica = fc.getDataValutazione();
                                    oraModifica = fc.getOraValutazione();
                                    if (data_max.isAfter(dataModifica) || (data_max.equals(dataModifica) && oraMax.isAfter((oraModifica)))) {
                                        fr_salvata = f;
                                    } else {
                                        fr_salvata = fc;
                                    }
                                }
                            }else {
                                controllo = 1;
                                dataModifica = fc.getDataValutazione();
                                oraModifica = fc.getOraValutazione();
                                if (data_max.isAfter(dataModifica) || (data_max.equals(dataModifica) && oraMax.isAfter((oraModifica)))) {
                                    fr_salvata = f;
                                } else {
                                    fr_salvata = fc;
                                }
                            }
                        }
                    }
                    if (controllo == 0) {
                        frasiTesto.add(f.getStringa_inserita());
                    } else {
                        frasiTesto.add(fr_salvata.getStringa_inserita());
                    }
                    controllo = 0;
                }
            }
        return frasiTesto;
    }

    /**
     * Restituisce un valore booleano, vero se l'utente o autore che accede alla wiki è loggato, falso se l'utente non è loggato.
     *
     * @return boolean
     */
    public boolean loggato(){
        if(utenteLoggato != null || autoreloggato != null)
            return true;
        return false;
    }

    /**
     * Questo metodo verifica se le credenziali immesse al momento del login sono valide. Se sono valide la funzione restituisce vero. Inoltre se il ruolo di chi accede
     * é "autore", verranno caricate nel sistema le pagine da lui create.
     *
     * @param login    indica il login di accesso dell'utente loggato
     * @param password indica la password di accesso dell'utente loggato
     * @return ruolo
     */
    public int verificaLoggato(String login, String password) {
        this.pagineModificateUtente = new ArrayList<>();
        int controllo = 0;
        WikiDAO w = new WikiimplementazionePostgresDAO();
        try {
            if(w.verificaLoggato(login, password)){
                w = new WikiimplementazionePostgresDAO();
                String nome = w.getNomeUtente(login);
                String cognome = w.getCognomeUtente(login);
                String email = w.getEmailUtente(login);
                Date dataNascita = w.getDataNascitaUtente(login);
                String ruolo = w.getRuolotente(login);
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

    /**
     * Metodo che viene utilizzato nel momento della registrazione di un nuovo utente alla pagina wiki.
     *
     * @param nome        indica il nome dell'utente che si sta registrando
     * @param cognome     indica il cognome dell'utente che si sta registrando
     * @param nomeUtente  indica il login di accesso dell'utente che si sta registrando
     * @param password    indica la password di accesso dell'utente che si sta registrando
     * @param email       indica l'indirizzo email dell'utente che si sta registrando
     * @param dataNascita indica la data di nascita dell'utente che si sta registrando
     */
    public void registrazioneUtente(String nome, String cognome, String nomeUtente, String password, String email, Date dataNascita) {
        WikiDAO w = new WikiimplementazionePostgresDAO();
        try {
            w.registrazione(nome,cognome,nomeUtente,password,email,dataNascita);
            utenteLoggato = new Utente(nome,cognome,nomeUtente,password,email,dataNascita);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Restituisce le frasi selezionate dall'utente nel momento di una modifica o di un aggiunta di un collegamento ad esse.
     *
     * @return frasiTesto
     */
    public ArrayList<String> getFrasiTestoSelezionato(){
        ArrayList<String> frasiTesto = new ArrayList<>();
        for(Frase_Corrente f : paginaSelezionata.getFrasi()){
            LocalDate dataMax = f.getDataInserimento();
            LocalTime oraMax = f.getOraInserimento().toLocalTime();
            String frase = f.getStringa_inserita();
            for(ModificaProposta mp : f.getProposte()){
                if (mp.getStato() == 1){
                    if(mp.getDataValutazione().isAfter(dataMax) || (mp.getDataValutazione().isEqual(dataMax) && mp.getOraValutazione().isAfter(oraMax))){
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

    /**
     * Metodo che permette all'utente di poter effettuare modifiche al testo di una pagina wiki.
     *
     * @param fraseSelezionata indica la frase che l'utente vuole modificare dal testo
     * @param fraseProposta    indica la modifica proposta dall'utente sulla frase selezionata
     * @param numerazione      indica la numerazione della frase selezionata dall'utente
     * @return boolean
     */
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

        if (fraseProposta.charAt(fraseProposta.length()-1) != ',' && fraseProposta.charAt(fraseProposta.length()-1) != '.' && fraseProposta.charAt(fraseProposta.length()-1) != ';' && fraseProposta.charAt(fraseProposta.length()-1) != '?' && fraseProposta.charAt(fraseProposta.length()-1) != '!') {
            fraseProposta = fraseProposta + fraseSelezionata.charAt(fraseSelezionata.length()-1);
        }


        try {
            if(utenteLoggato != null) {
                w.inviaProposta(numerazione, fraseSelezionata, fraseProposta, utenteLoggato.getLogin(), paginaSelezionata.getAutore().getLogin(), paginaSelezionata.getTitolo(), paginaSelezionata.getDataCreazione());
                modifica = new ModificaProposta(LocalDate.now(), LocalTime.now(), paginaSelezionata.getAutore(), utenteLoggato, paginaSelezionata.getFrasi().get(numerazione), fraseProposta);
                if(paginaSelezionata.getAutore().getLogin().equals(utenteLoggato.getLogin())){
                    modifica.setStato(1);
                    modifica.setDataValutazione(LocalDate.now());
                    modifica.setOraValutazione(LocalTime.now());
                }
            }else {
                w.inviaProposta(numerazione, fraseSelezionata, fraseProposta, autoreloggato.getLogin(), paginaSelezionata.getAutore().getLogin(), paginaSelezionata.getTitolo(), paginaSelezionata.getDataCreazione());
                modifica = new ModificaProposta(LocalDate.now(), LocalTime.now(), paginaSelezionata.getAutore(), autoreloggato, paginaSelezionata.getFrasi().get(numerazione), fraseProposta);
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

    /**
     * Metodo che permette all'utente di creare una pagina wiki. La funzione si occupa anche della suddivisione del testo in frasi.
     *
     * @param titolo indica il titolo scelto dall'utente per la creazione della nuova pagina
     * @param testo  indica il testo scelto dall'utente per la creazine della nuova pagina
     */
    public void creazionePagina(String titolo, String testo) {
        ArrayList<String> frasi = new ArrayList<>();
        Pagina paginaCreata = null;
        LocalDateTime dataCreazione = LocalDateTime.now();
        if(autoreloggato != null){
            paginaCreata = new Pagina(titolo, dataCreazione, autoreloggato);
        }else{
            paginaCreata = new Pagina(titolo,dataCreazione, utenteLoggato);
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

    /**
     * Metodo usato per memorizzare la pagina selezionata dall'utente.
     *
     * @param numeroPaginaSelezionata indica la pagina selezionata dall'utente
     */
    public void addPaginaSelezionata(int numeroPaginaSelezionata){
        paginaSelezionata = pagineTrovate.get(numeroPaginaSelezionata);
    }


    /**
     * Restituisce un ArrayList contenente tutte le pagine viusalizzate dall'utente.
     *
     * @return titoli
     */
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
            if(utenteLoggato != null) {
                if (utenteLoggato.getPagineVisualizzate().size() == 0)
                    w.storicoPagineVisualizzate(utenteLoggato.getLogin(), titoli, dateOreCreazioni, nomi, cognomi, nomiUtente, password, email, dataNascita, dateVisioni, oreVisioni);
            }else if(autoreloggato != null) {
                if (autoreloggato.getPagineVisualizzate().size() == 0)
                    w.storicoPagineVisualizzate(autoreloggato.getLogin(), titoli, dateOreCreazioni, nomi, cognomi, nomiUtente, password, email, dataNascita, dateVisioni, oreVisioni);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

            if(titoli.size() > 0) {
                for (int i = 0; i < titoli.size(); i++) {
                    Pagina pagina = new Pagina(titoli.get(i), dateOreCreazioni.get(i), nomi.get(i), cognomi.get(i), nomiUtente.get(i), password.get(i), email.get(i), dataNascita.get(i));
                    if (utenteLoggato != null) {
                        Visiona visiona = new Visiona(dateVisioni.get(i), oreVisioni.get(i), pagina, utenteLoggato);
                    } else if (autoreloggato != null) {
                        Visiona visiona = new Visiona(dateVisioni.get(i), oreVisioni.get(i), pagina, autoreloggato);
                    }
                }
            }else {
                if (utenteLoggato != null) {
                    for (int i = 0; i < utenteLoggato.getPagineVisualizzate().size(); i++) {
                        titoli.add(utenteLoggato.getPagineVisualizzate().get(i).getPagina().getTitolo());
                    }
                } else if (autoreloggato != null) {
                    for (int i = 0; i < autoreloggato.getPagineVisualizzate().size(); i++) {
                        titoli.add(autoreloggato.getPagineVisualizzate().get(i).getPagina().getTitolo());
                    }
                }
            }
        return titoli;
    }

    /**
     * Aggiunge la pagina viusalizzata allo storico al momento della viusalizzazione.
     */
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

    /**
     * Restituisce un ArrayList contenente i titoli delle pagine modificate dall'utente.
     *
     * @return modificate
     */
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
        ArrayList<Optional<LocalDate>> datevalutazione = new ArrayList<>();
        ArrayList<Optional<LocalTime>> orevalutazione = new ArrayList<>();
        ArrayList<LocalDate>  dataInserimento = new ArrayList<>();
        ArrayList<Time> oraInseriento = new ArrayList<>();

        WikiDAO w = new WikiimplementazionePostgresDAO();
        try {
            if(utenteLoggato != null) {
                if (pagineModificateUtente.isEmpty()) {
                    w.getModificate(utenteLoggato.getLogin(), titolo, dataOraCreazione, nomi, cognomi, nomiUtente, password, email, dataNascita, dataProposta, oraProposta, stringaInserita, numerazione, stato, stringaProposta, datevalutazione, orevalutazione, dataInserimento, oraInseriento);
                    for (int i = 0; i < nomi.size(); i++) {
                        Pagina pagina = new Pagina(titolo.get(i), dataOraCreazione.get(i), nomi.get(i), cognomi.get(i), nomiUtente.get(i), password.get(i), email.get(i), dataNascita.get(i));
                        Frase_Corrente fraseCorrente = new Frase_Corrente(stringaInserita.get(i), numerazione.get(i), pagina, dataInserimento.get(i), oraInseriento.get(i));
                        ModificaProposta modificaProposta = new ModificaProposta(dataProposta.get(i), oraProposta.get(i), pagina.getAutore(), utenteLoggato, fraseCorrente, stringaProposta.get(i), stato.get(i));
                        if (datevalutazione.get(i).isPresent()) {
                            modificaProposta.setDataValutazione(datevalutazione.get(i).get());
                            modificaProposta.setOraValutazione(orevalutazione.get(i).get());
                        }
                    }
                    int controllo;
                    for (int i = 0; i < utenteLoggato.getFrasiProposte().size(); i++) {
                        controllo = 0;
                        for (int j = 0; j < pagineModificateUtente.size(); j++) {
                            if (pagineModificateUtente.get(j).getTitolo().equals(utenteLoggato.getFrasiProposte().get(i).getFraseCorrente().getPagina().getTitolo()) && pagineModificateUtente.get(j).getDataCreazione().equals(utenteLoggato.getFrasiProposte().get(i).getFraseCorrente().getPagina().getDataCreazione())) {
                                controllo = 1;
                            }
                        }
                        if (controllo == 0) {
                            modifiche.add(utenteLoggato.getFrasiProposte().get(i).getFraseCorrente().getPagina().getTitolo());
                            pagineModificateUtente.add(utenteLoggato.getFrasiProposte().get(i).getFraseCorrente().getPagina());
                        }
                    }
                }else{
                    for(Pagina p : pagineModificateUtente){
                        modifiche.add(p.getTitolo());
                    }
                }
            }else {
                if (pagineModificateUtente.isEmpty()) {
                    w.getModificate(autoreloggato.getLogin(), titolo, dataOraCreazione, nomi, cognomi, nomiUtente, password, email, dataNascita, dataProposta, oraProposta, stringaInserita, numerazione, stato, stringaProposta, datevalutazione, orevalutazione, dataInserimento, oraInseriento);
                    for (int i = 0; i < nomi.size(); i++) {
                        Pagina pagina = new Pagina(titolo.get(i), dataOraCreazione.get(i), nomi.get(i), cognomi.get(i), nomiUtente.get(i), password.get(i), email.get(i), dataNascita.get(i));
                        Frase_Corrente fraseCorrente = new Frase_Corrente(stringaInserita.get(i), numerazione.get(i), pagina, dataInserimento.get(i), oraInseriento.get(i));
                        ModificaProposta modificaProposta = new ModificaProposta(dataProposta.get(i), oraProposta.get(i), pagina.getAutore(), autoreloggato, fraseCorrente, stringaProposta.get(i), stato.get(i));
                        if (datevalutazione.get(i).isPresent()) {
                            modificaProposta.setDataValutazione(datevalutazione.get(i).get());
                            modificaProposta.setOraValutazione(orevalutazione.get(i).get());
                        }
                    }
                    int controllo;
                    for (int i = 0; i < autoreloggato.getFrasiProposte().size(); i++) {
                        controllo = 0;
                        for (int j = 0; j < pagineModificateUtente.size(); j++) {
                            if (pagineModificateUtente.get(j).getTitolo().equals(autoreloggato.getFrasiProposte().get(i).getFraseCorrente().getPagina().getTitolo()) && pagineModificateUtente.get(j).getDataCreazione().equals(autoreloggato.getFrasiProposte().get(i).getFraseCorrente().getPagina().getDataCreazione())) {
                                controllo = 1;
                            }
                        }
                        if (controllo == 0) {
                            modifiche.add(autoreloggato.getFrasiProposte().get(i).getFraseCorrente().getPagina().getTitolo());
                            pagineModificateUtente.add(autoreloggato.getFrasiProposte().get(i).getFraseCorrente().getPagina());
                        }
                    }
                }else{
                    for(Pagina p : pagineModificateUtente){
                        modifiche.add(p.getTitolo());
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return modifiche;
    }

    /**
     * Restituisce un ArrayList di pagine create dall'autore
     *
     * @return titoli
     */
    public ArrayList<String> storicoPagineCreate() {
        ArrayList<String> titoli = new ArrayList<>();
        for(int i = 0; i < autoreloggato.getCreazioni().size(); i++){
            titoli.add(autoreloggato.getCreazioni().get(i).getTitolo());
        }

        return titoli;
    }

    /**
     * restituisce un valore boooleano che indica se l'autore ha delle notifiche da visualizzare al momento del login.
     *
     * @return notifiche
     */
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

    /**
     * Restituisce un valore booleano che indica se l'autore una volta visionata la mofifica, accetta o rifiuta una modifica proposta a una sua pagina.
     *
     * @param cambiaStato indica lo stato di accettazione della modifica (-1 rifiutata, 1 accetta)
     * @return the boolean
     */
    public boolean aggiornaStato(int cambiaStato) {
        boolean controllo = false;
        ModificaProposta modificaProposta = null;
        if(!autoreloggato.getNotificheRicevute().isEmpty()) {
            modificaProposta = autoreloggato.getNotificheRicevute().get(0).getModifica();
            WikiDAO w = new WikiimplementazionePostgresDAO();
            try {
                controllo = w.aggiornaStato(modificaProposta.getAutore().getLogin(), modificaProposta.getUtente().getLogin(), modificaProposta.getFraseCorrente().getStringa_inserita(), modificaProposta.getStringa_inserita(), modificaProposta.getDataProposta(), modificaProposta.getOraProposta(), cambiaStato);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            autoreloggato.getNotificheRicevute().get(0).getModifica().setStato(cambiaStato);
            autoreloggato.getNotificheRicevute().remove(0);
        }
        return controllo;
    }

    /**
     * Restituisce il numero di notifiche da visualizzare.
     *
     * @return int
     */
    public int contaNotifiche(){
        return autoreloggato.getNotificheRicevute().size();
    }

    /**
     * Restituisce un ArrayList di titoli di pagine in base alla stringa inserita dall'utente nella barra di ricerca.
     *
     * @param titoloInserito stringa inserita dall'utente nella barra di ricerca che indica un possibile tsto da cercare nella wiki
     * @return titoliCercati
     */
    public ArrayList<String> getTitoliCercati(String titoloInserito){
        this.pagineTrovate = new ArrayList<>();
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

    /**
     * Restituisce il nome dell'autore.
     *
     * @return nome
     */
    public String getNomeAutore() {
        return paginaSelezionata.getAutore().getNome();
    }

    /**
     * Restituisce il cognome dell'autore.
     *
     * @return cognome
     */
    public String getCognomeAutore() {
        return paginaSelezionata.getAutore().getCognome();
    }

    /**
     * Restituisce il login di accesso dell'autore.
     *
     * @return login
     */
    public String getLoginAutorePaginaSelezionata(){
        return paginaSelezionata.getAutore().getLogin();
    }

    /**
     * Restituisce il titolo della pagina selezionata dall'utente.
     *
     * @return titolo
     */
    public String getTitoloPaginaSelezionata() {
        return paginaSelezionata.getTitolo();
    }

    /**
     * Restituisce la data e l'ora di creazione della pagina selezionata dall'utente.
     *
     * @return DataCreazione
     */
    public LocalDateTime getDataOraCreazionepaginaSelezionata() {
        return paginaSelezionata.getDataCreazione();
    }

    /**
     * restituisce il login di accesso dell'utente o autore loggato.
     *
     * @return login
     */
    public String getLoginLoggato() {
        if(utenteLoggato != null){
            return utenteLoggato.getLogin();
        }else {
            return  autoreloggato.getLogin();
        }
    }

    /**
     * Setta la pagina da visualizzaere uguale alla pagina selezionata nell'elenco delle pagine visualizzate dall'utente o dall'autore.
     *
     * @param paginaVisualizzata indica la pagina visualizzata dall'utente o dall'autore.
     */
    public void setPaginaVisualizzata(int paginaVisualizzata){
        if(utenteLoggato != null){
            paginaSelezionata = utenteLoggato.getPagineVisualizzate().get(paginaVisualizzata).getPagina();
        }else{
            paginaSelezionata = autoreloggato.getPagineVisualizzate().get(paginaVisualizzata).getPagina();
        }
    }

    /**
     * Setta la pagina da visualizzaere uguale alla pagina selezionata nell'elenco delle pagine create dall'utente o dall'autore.
     *
     * @param paginaCreata indica la pagina creata.
     */
    public void setPaginaCreata(int paginaCreata){
        for (Frase_Corrente f : autoreloggato.getCreazioni().get(paginaCreata).getFrasi()){
            f.setProposte(new ArrayList<>());
        }
        autoreloggato.getCreazioni().get(paginaCreata).setFrasi(new ArrayList<>());

        paginaSelezionata = autoreloggato.getCreazioni().get(paginaCreata);
    }

    /**
     * Metodo utilizzato per prendere dal database le notifiche dell'autore.
     */
    public void getNotifche() {
        if (autoreloggato != null) {
            for (int j = 0; j < autoreloggato.getCreazioni().size(); j++) {
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
                ArrayList<String> stringaProposta = new ArrayList<>();
                ArrayList<Integer> stato = new ArrayList<>();
                ArrayList<Optional<LocalDate>> datavalutazione = new ArrayList<>();
                ArrayList<Optional<LocalTime>> oraValutazione = new ArrayList<>();
                ArrayList<LocalDate> dataInserimento = new ArrayList<>();
                ArrayList<Time> oraInseriento = new ArrayList<>();

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
                        Frase_Corrente fraseCorrente = new Frase_Corrente(stringaInserita.get(i), numerazione.get(i), autoreloggato.getCreazioni().get(j), dataInserimento.get(i), oraInseriento.get(i));
                        ModificaProposta modificaProposta = new ModificaProposta(dataProposta.get(i), oraProposta.get(i), autoreloggato, utente, fraseCorrente, stringaProposta.get(i), stato.get(i));
                    }
                }
            }
        }
    }

    /**
     * Restituisce il titolo della pagina che è stata notificata all'autore.
     *
     * @return Titolo
     */
    public String getTitoloNotifica() {
        return autoreloggato.getNotificheRicevute().get(0).getTitolo();
    }

    /**
     * Restituisce la stringa selezionata dall'utente da modificare.
     *
     * @return stringaSelezionata
     */
    public String getStringaSelezionata() {
        ModificaProposta modificaProposta = autoreloggato.getNotificheRicevute().getFirst().getModifica();
        LocalDate dataMax = modificaProposta.getFraseCorrente().getDataInserimento();
        LocalTime oraMax = modificaProposta.getFraseCorrente().getOraInserimento().toLocalTime();
        String frase = modificaProposta.getFraseCorrente().getStringa_inserita();
        int num = modificaProposta.getFraseCorrente().getNumerazione();
        for (ModificaProposta mp : paginaSelezionata.getFrasi().get(num).getProposte()) {
            if (mp.getStato() == 1) {
                if(mp.getDataValutazione().isAfter(dataMax) || (mp.getDataValutazione().equals(dataMax) && mp.getOraValutazione().isAfter(oraMax))){
                    dataMax = mp.getDataValutazione();
                    oraMax = mp.getOraValutazione();
                    frase = mp.getStringa_inserita();
                }
            }
        }
        return frase;
    }

    /**
     * Restituuisce la frase proposta dall'utente al momento della modifica.
     *
     * @return fraseproposta
     */
    public String getFraseproposta() {
        return autoreloggato.getNotificheRicevute().get(0).getModifica().getStringa_inserita();
    }

    /**
     * Restituisce un ArrayList contenente le frasi selezionate dall'utente al momento della modifica.
     *
     * @return frasiSelezionate
     */
    public ArrayList<String> getFrasiSelezionate() {
        ArrayList<String> frasiSelezionate = new ArrayList<>();
        ArrayList<ModificaProposta> modifichePagina = new ArrayList<>();
        if (utenteLoggato != null) {
            for(ModificaProposta f : utenteLoggato.getFrasiProposte()){
                if(f.getFraseCorrente().getPagina().getTitolo().equals(paginaSelezionata.getTitolo()) && f.getFraseCorrente().getPagina().getDataCreazione().equals(paginaSelezionata.getDataCreazione())){
                    modifichePagina.add(f);
                }
            }
            for (ModificaProposta modificheUtente : modifichePagina) {
                Pagina pagina = paginaSelezionata;
                for(int i = 0; i < pagina.getFrasi().size(); i++){
                    Frase_Corrente f = pagina.getFrasi().get(i);
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
                                if(m.getDataValutazione().isAfter(dataMax) || (m.getDataValutazione().equals(dataMax) && m.getOraValutazione().isAfter(oraMax.toLocalTime()))) {
                                    dataMax = m.getDataValutazione();
                                    oraMax = Time.valueOf(m.getOraValutazione());
                                    fraseTemp = m.getStringa_inserita();
                                }
                            }
                        }
                        frasiSelezionate.add(fraseTemp);
                    }
                }
            }
        } else {
            for(ModificaProposta f : autoreloggato.getFrasiProposte()){
                if(f.getFraseCorrente().getPagina().getTitolo().equals(paginaSelezionata.getTitolo()) && f.getFraseCorrente().getPagina().getDataCreazione().equals(paginaSelezionata.getDataCreazione())){
                    modifichePagina.add(f);
                }
            }

            for (ModificaProposta modificheUtente : modifichePagina) {
                Pagina pagina = paginaSelezionata;
                for(int i = 0; i < pagina.getFrasi().size(); i++){
                    Frase_Corrente f = pagina.getFrasi().get(i);

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
                                if(m.getDataValutazione().isAfter(dataMax) || (m.getDataValutazione().equals(dataMax) && m.getOraValutazione().isAfter(oraMax.toLocalTime()))) {
                                    dataMax = m.getDataValutazione();
                                    oraMax = Time.valueOf(m.getOraValutazione());
                                    fraseTemp = m.getStringa_inserita();
                                }
                            }
                        }
                        frasiSelezionate.add(fraseTemp);
                    }
                }
            }
        }
        return frasiSelezionate;
    }

    /**
     * Restituisce un ArrayList di frasi proposte dall'Utente al momento della modifica.
     *
     * @return frasiproposte
     */
    public ArrayList<String> getFrasiproposte() {
        ArrayList<String> frasi = new ArrayList<>();
        if(utenteLoggato != null){
            for(ModificaProposta f : utenteLoggato.getFrasiProposte()) {
                if(f.getFraseCorrente().getPagina().getTitolo().equals(paginaSelezionata.getTitolo()) && f.getFraseCorrente().getPagina().getDataCreazione().equals(paginaSelezionata.getDataCreazione())){
                    frasi.add(f.getStringa_inserita());
                }
            }
        }else {
            for (ModificaProposta f : autoreloggato.getFrasiProposte()) {
                if(f.getFraseCorrente().getPagina().getTitolo().equals(paginaSelezionata.getTitolo()) && f.getFraseCorrente().getPagina().getDataCreazione().equals(paginaSelezionata.getDataCreazione())){
                    frasi.add(f.getStringa_inserita());
                }
            }
        }
        return frasi;
    }

    /**
     * restituisce un ArrayList di interi che indica gli stati delle modifiche (1 accetata, -1 rifiutata, 0 in attesa) .
     *
     * @return stati
     */
    public ArrayList<Integer> getstati() {
        ArrayList<Integer> stati = new ArrayList<>();
        if(utenteLoggato != null){
            for(ModificaProposta f : utenteLoggato.getFrasiProposte()) {
                if(f.getFraseCorrente().getPagina().getTitolo().equals(paginaSelezionata.getTitolo()) && f.getFraseCorrente().getPagina().getDataCreazione().equals(paginaSelezionata.getDataCreazione())){
                    stati.add(f.getStato());
                }

            }
        }else {
            for (ModificaProposta f : autoreloggato.getFrasiProposte()) {
                if(f.getFraseCorrente().getPagina().getTitolo().equals(paginaSelezionata.getTitolo()) && f.getFraseCorrente().getPagina().getDataCreazione().equals(paginaSelezionata.getDataCreazione())){
                    stati.add(f.getStato());
                }
            }
        }
        return stati;
    }


    /**
     * Metodo utilizzato nel momento in cui l'utente o l'autore ha intenzione di effettuare il logout dalla pagina wiki.
     */
    public void logout() {
        if(utenteLoggato != null)
            utenteLoggato = null;
        else
            autoreloggato = null;
    }

    /**
     * Setta la pagina da visualizzare uguale alla pagina selezionata nell'elenco delle pagine modificate dall'utente o dall'autore.
     *
     * @param numeroPaginaModificata indica l'id della pagina da modificare
     */
    public void setPaginaModificata(int numeroPaginaModificata) {
        for (Frase_Corrente f : pagineModificateUtente.get(numeroPaginaModificata).getFrasi()) {
            f.setProposte(new ArrayList<>());
        }
        pagineModificateUtente.get(numeroPaginaModificata).setFrasi(new ArrayList<>());
        paginaSelezionata = pagineModificateUtente.get(numeroPaginaModificata);
    }

    /**
     * Setta la pagina a cui é collegata la notifica come paginaSelezionata
     */
    public void setPaginaNotificata() {
        paginaSelezionata = autoreloggato.getNotificheRicevute().get(0).getModifica().getFraseCorrente().getPagina();
        for(Frase_Corrente f : paginaSelezionata.getFrasi()){
            f.setProposte(new ArrayList<>());
        }
        paginaSelezionata.setFrasi(new ArrayList<>());
    }

    /**
     * Metodo che viene utilizzato per controllare se una frase possiede un collegamento ad un altra pagina. Restituisce vero se la frase ha un collegamento falso altrimenti.
     *
     * @return boolean
     */
    public boolean controllaCollegamenti(){
        int conteggio = 0;
        for (Frase_Corrente f : paginaSelezionata.getFrasi()){
            if(f.getPaginaCollegata() != null)
                conteggio++;
        }
        if(conteggio > 0)
            return true;
        return false;
    }

    /**
     * Setta la pagina da visualizzaere uguale alla pagina selezionata nell'elenco delle pagine a cui si vuole collegare la frase selezionata dall'autore.
     *
     * @param clickedSentence indica la frase cliccata che ha il collegamento
     */
    public void setPaginaCollegata(String clickedSentence) {
        SalvaVecchiaPaginaSelezionata = paginaSelezionata;
        boolean controllo = false;
        for(Frase_Corrente f : paginaSelezionata.getFrasi()){
            if(f.getPaginaCollegata() != null && !controllo) {
                if (f.getStringa_inserita().equals(clickedSentence)) {
                    controllo = true;
                    paginaSelezionata = f.getPaginaCollegata();
                }
            }
        }
        if(!controllo){
            for(Frase_Corrente f : paginaSelezionata.getFrasi()){
                for(ModificaProposta m : f.getProposte()){
                    if(m.getFraseCorrente().getPaginaCollegata() != null && !controllo) {
                        if (m.getStringa_inserita().equals(clickedSentence)) {
                            controllo = true;
                            paginaSelezionata = f.getPaginaCollegata();
                        }
                    }
                }
            }
        }
    }

    /**
     * Restituisce un valore booleano che indica se la vecchia pagina é anora valorizzata.
     *
     * @return boolean
     */
    public boolean getSalvaVecchiaPaginaSelezionata(){
        if (SalvaVecchiaPaginaSelezionata == null){
            return false;
        }else{
            return true;
        }
    }

    /**
     * Restituisce un ArrayList contenente le frasi che hanno un collegamento ad altre pagine.
     *
     * @return frasi
     */
    public ArrayList<String> getFrasiCollegamento() {
        ArrayList<String> frasi = new ArrayList<>();
        String fraseTemp = null;

        for(Frase_Corrente f : paginaSelezionata.getFrasi()){
            if(f.getPaginaCollegata() != null){
                fraseTemp = f.getStringa_inserita();
                LocalDate dataMax = f.getDataInserimento();
                Time oraMax = f.getOraInserimento();
                for(ModificaProposta m : f.getProposte()) {
                    if (m.getStato() == 1) {
                        if (m.getDataValutazione().isAfter(dataMax) || (m.getDataValutazione().isEqual(dataMax) && m.getOraValutazione().isAfter(oraMax.toLocalTime()))) {
                            dataMax = m.getDataValutazione();
                            oraMax = Time.valueOf(m.getOraValutazione());
                            fraseTemp = m.getStringa_inserita();
                        }
                    }
                }
                frasi.add(fraseTemp);
            }

        }
        return frasi;
    }

    /**
     * Ripristina la pagina selezionata.
     */
    public void ripristinaPaginaSelezionata(){
        paginaSelezionata = SalvaVecchiaPaginaSelezionata;
    }

    /**
     * Controlla se pagina precedente salvata é ancora valorizzata, se lo é viene 1ripristinata.
     */
    public void controllaPaginaPrecedenteSalvata() {
        if(SalvaVecchiaPaginaSelezionata != null){
            ripristinaPaginaSelezionata();
        }
    }

    /**
     * Restituisce l'anno di creazione di una pagina.
     *
     * @return anno inzio
     */
    public int getAnnoInzio() {
        return paginaSelezionata.getDataCreazione().getYear();

    }

    /**
     * Setta la versione precdente della pagina.
     *
     * @param dataSelezionata indica la data di quale versione del testo si vuole visualizzare
     */
    public void setVersionePrecedenteTrue(LocalDate dataSelezionata) {
        controlloVersione = true;
        dataVersioneSelezionata = dataSelezionata;
    }

    /**
     * Setta la versione precedente a false.
     */
    public void setVersionePrecedenteFalse() {
        controlloVersione = false;
        dataVersioneSelezionata = null;
    }

    /**
     * Ritorna un valore booleano che corrisponde al valore di controlloVersione, che indica se il testo ha una versione precedente.
     *
     * @return the boolean
     */
    public boolean controlloVersionePrecedente() {
        return controlloVersione;
    }

    /**
     * Aggiunge alla frase selezionata il collegamento indicato. Se la frase ha giá un collegamento questo viene modificato.
     *
     * @param indiceFrase the indice frase
     * @param clickedRow  the clicked row
     */
    public void addPaginaCollegata(int indiceFrase, int clickedRow) {
        if( paginaSelezionata.getFrasi().get(indiceFrase).getPaginaCollegata() == null) {
            paginaSelezionata.getFrasi().get(indiceFrase).setPaginaCollegata(pagineTrovate.get(clickedRow));
            pagineTrovate.get(clickedRow).addFrasi_collegate(paginaSelezionata.getFrasi().get(indiceFrase));
        }else{
            paginaSelezionata.getFrasi().get(indiceFrase).setPaginaCollegata(pagineTrovate.get(clickedRow));
            for(Frase_Corrente f : pagineTrovate.get(clickedRow).getFrasi_collegate()){
                if(f.equals(paginaSelezionata.getFrasi().get(indiceFrase))){
                    pagineTrovate.get(clickedRow).removeFrasi_collegate(f);
                    pagineTrovate.get(clickedRow).addFrasi_collegate(paginaSelezionata.getFrasi().get(indiceFrase));
                }
            }

        }

        WikiDAO w = new WikiimplementazionePostgresDAO();
        String stringaInserita = paginaSelezionata.getFrasi().get(indiceFrase).getStringa_inserita();

        try {
            w.addPaginacollegata(stringaInserita, paginaSelezionata.getFrasi().get(indiceFrase).getDataInserimento(), paginaSelezionata.getFrasi().get(indiceFrase).getOraInserimento(), indiceFrase, pagineTrovate.get(clickedRow).getTitolo(), pagineTrovate.get(clickedRow).getDataCreazione());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Controlla se il nome utente inserito giá é esistente nel database. Restituisce il valore della funzione "w.controllaNomeUtente(nomeUtente)".
     *
     * @param nomeUtente the nome utente
     * @return the boolean
     */
    public boolean controllaNomeUtente(String nomeUtente) {
        WikiDAO w = new WikiimplementazionePostgresDAO();
        try {
            return w.controllaNomeUtente(nomeUtente);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
