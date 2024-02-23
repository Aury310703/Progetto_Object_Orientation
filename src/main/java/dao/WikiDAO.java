package dao;

import GUI.Notifiche;
import MODEL.Pagina;


import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

/**
 * The interface Wiki dao.
 */
public interface WikiDAO {
    /**
     * Ricerca titoli.
     *
     * @param titoloInserito   the titolo inserito
     * @param titoli           the titoli
     * @param dateOreCreazioni the date ore creazioni
     * @param nomi             the nomi
     * @param cognomi          the cognomi
     * @param login            the login
     * @param password         the password
     * @param email            the email
     * @param date             the date
     * @throws SQLException the sql exception
     */
    public void ricercaTitoli(String titoloInserito, ArrayList<String> titoli, ArrayList<LocalDateTime> dateOreCreazioni, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> login, ArrayList<String> password, ArrayList<String> email, ArrayList<Date> date) throws SQLException;

    /**
     * Gets frasi correnti.
     *
     * @param login                     the login
     * @param titolo                    the titolo
     * @param dataOraCreazione          the data ora creazione
     * @param frasiInserite             the frasi inserite
     * @param dateInserimento           the date inserimento
     * @param oreInserimento            the ore inserimento
     * @param titoloCollegata           the titolo collegata
     * @param dataOraCreazioneCollegata the data ora creazione collegata
     * @param nomiCollegata             the nomi collegata
     * @param cognomiCollegata          the cognomi collegata
     * @param loginCollegata            the login collegata
     * @param passwordCollegata         the password collegata
     * @param emailCollegata            the email collegata
     * @param dateCollegata             the date collegata
     * @param numerazioneCollegata      the numerazione collegata
     * @throws SQLException the sql exception
     */
    public void getFrasiCorrenti(String login, String titolo, LocalDateTime dataOraCreazione, ArrayList<String> frasiInserite, ArrayList<LocalDate> dateInserimento, ArrayList<Time> oreInserimento, ArrayList<String> titoloCollegata, ArrayList<LocalDateTime> dataOraCreazioneCollegata, ArrayList<String> nomiCollegata, ArrayList<String> cognomiCollegata, ArrayList<String> loginCollegata, ArrayList<String> passwordCollegata, ArrayList<String> emailCollegata, ArrayList<Date> dateCollegata, ArrayList<Integer> numerazioneCollegata) throws SQLException;

    /**
     * Gets modifiche modificate.
     *
     * @param login            the login
     * @param titolo           the titolo
     * @param dataOraCreazione the data ora creazione
     * @param stringaInserita  the stringa inserita
     * @param numerazione      the numerazione
     * @param frasiProposte    the frasi proposte
     * @param dateProposte     the date proposte
     * @param oreProposte      the ore proposte
     * @param datavalutazione  the datavalutazione
     * @param orevalutazione   the orevalutazione
     * @param stati            the stati
     * @param nomi             the nomi
     * @param cognomi          the cognomi
     * @param logins           the logins
     * @param password         the password
     * @param email            the email
     * @param date             the date
     * @throws SQLException the sql exception
     */
    public void getModificheModificate(String login, String titolo, LocalDateTime dataOraCreazione, String stringaInserita, int numerazione, ArrayList<String> frasiProposte, ArrayList<LocalDate> dateProposte, ArrayList<LocalTime> oreProposte, ArrayList<Optional<LocalDate>> datavalutazione, ArrayList<Optional<LocalTime>> orevalutazione, ArrayList<Integer> stati, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> logins, ArrayList<String> password, ArrayList<String> email, ArrayList<Date> date) throws SQLException;

    /**
     * Verifica loggato boolean.
     *
     * @param nome       the nome
     * @param cognome    the cognome
     * @param login      the login
     * @param password   the password
     * @param email      the email
     * @param datNascita the dat nascita
     * @param ruolo      the ruolo
     * @return the boolean
     * @throws SQLException the sql exception
     */
    public boolean verificaLoggato(String nome, String cognome, String login, String password, String email, Date datNascita, String ruolo) throws SQLException;

    /**
     * Registrazione.
     *
     * @param nome        the nome
     * @param cognome     the cognome
     * @param nomeUtente  the nome utente
     * @param password    the password
     * @param email       the email
     * @param dataNascita the data nascita
     * @throws SQLException the sql exception
     */
    public void registrazione(String nome, String cognome, String nomeUtente, String password, String email, Date dataNascita) throws SQLException;

    /**
     * Invia proposta boolean.
     *
     * @param numerazione      the numerazione
     * @param fraseSelezionata the frase selezionata
     * @param fraseProposta    the frase proposta
     * @param loginUtente      the login utente
     * @param loginAutore      the login autore
     * @param titolo           the titolo
     * @param dataOraCreazione the data ora creazione
     * @return the boolean
     * @throws SQLException the sql exception
     */
    public boolean inviaProposta(int numerazione, String fraseSelezionata, String fraseProposta, String loginUtente, String loginAutore, String titolo, LocalDateTime dataOraCreazione) throws SQLException;

    /**
     * Creazione pagina.
     *
     * @param titolo        the titolo
     * @param frasi         the frasi
     * @param login         the login
     * @param dataCreazione the data creazione
     * @throws SQLException the sql exception
     */
    public void creazionePagina(String titolo, ArrayList<String> frasi, String login, LocalDateTime dataCreazione) throws SQLException;

    /**
     * Storico pagine visualizzate.
     *
     * @param loginUtente      the login utente
     * @param titoli           the titoli
     * @param dateOreCreazioni the date ore creazioni
     * @param nomi             the nomi
     * @param cognomi          the cognomi
     * @param nomiUtente       the nomi utente
     * @param password         the password
     * @param email            the email
     * @param dataNascita      the data nascita
     * @param dateVisioni      the date visioni
     * @param oreVisioni       the ore visioni
     * @throws SQLException the sql exception
     */
    public void storicoPagineVisualizzate(String loginUtente, ArrayList<String> titoli, ArrayList<LocalDateTime> dateOreCreazioni, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> nomiUtente, ArrayList<String> password, ArrayList<String> email, ArrayList<Date> dataNascita, ArrayList<LocalDate> dateVisioni, ArrayList<LocalTime> oreVisioni) throws SQLException;

    /**
     * Add pagina visualizzata.
     *
     * @param titolo                    the titolo
     * @param DataOraCreazione          the data ora creazione
     * @param loginAutorePagina         the login autore pagina
     * @param loginUtenteVisualizzatore the login utente visualizzatore
     * @throws SQLException the sql exception
     */
    public void addPaginaVisualizzata(String titolo, LocalDateTime DataOraCreazione, String loginAutorePagina, String loginUtenteVisualizzatore) throws SQLException;

    /**
     * Gets modificate.
     *
     * @param login            the login
     * @param titolo           the titolo
     * @param dataOraCreazione the data ora creazione
     * @param nomi             the nomi
     * @param cognomi          the cognomi
     * @param nomiUtente       the nomi utente
     * @param password         the password
     * @param email            the email
     * @param dataNascita      the data nascita
     * @param dataProposta     the data proposta
     * @param oraProposta      the ora proposta
     * @param stringaInserita  the stringa inserita
     * @param numerazione      the numerazione
     * @param stato            the stato
     * @param stringaProposta  the stringa proposta
     * @param datavalutazione  the datavalutazione
     * @param oraValutazione   the ora valutazione
     * @param dataInserimento  the data inserimento
     * @param oraInseriento    the ora inseriento
     * @throws SQLException the sql exception
     */
    public void getModificate(String login, ArrayList<String> titolo, ArrayList<LocalDateTime> dataOraCreazione, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> nomiUtente, ArrayList<String> password, ArrayList<String> email, ArrayList<Date> dataNascita, ArrayList<LocalDate> dataProposta, ArrayList<LocalTime> oraProposta, ArrayList<String> stringaInserita, ArrayList<Integer> numerazione, ArrayList<Integer> stato, ArrayList<String> stringaProposta, ArrayList<Optional<LocalDate>> datavalutazione, ArrayList<Optional<LocalTime>> oraValutazione, ArrayList<LocalDate> dataInserimento, ArrayList<Time> oraInseriento) throws SQLException;

    /**
     * Controlla notifiche boolean.
     *
     * @param login the login
     * @return the boolean
     * @throws SQLException the sql exception
     */
    public boolean controllaNotifiche(String login) throws SQLException;

    /**
     * Gets nome utente.
     *
     * @param login    the login
     * @param password the password
     * @return the nome utente
     */
    public String getNomeUtente(String login, String password);

    /**
     * Gets cognome utente.
     *
     * @param login    the login
     * @param password the password
     * @return the cognome utente
     */
    public String getCognomeUtente(String login, String password);

    /**
     * Gets email utente.
     *
     * @param login    the login
     * @param password the password
     * @return the email utente
     */
    String getEmailUtente(String login, String password);

    /**
     * Gets data nascita utente.
     *
     * @param login    the login
     * @param password the password
     * @return the data nascita utente
     */
    Date getDataNascitaUtente(String login, String password);

    /**
     * Gets ruolotente.
     *
     * @param login    the login
     * @param password the password
     * @return the ruolotente
     */
    String getRuolotente(String login, String password);

    /**
     * Gets modifiche pagina.
     *
     * @param login           the login
     * @param titolo          the titolo
     * @param dataCreazione   the data creazione
     * @param nomi            the nomi
     * @param cognomi         the cognomi
     * @param nomiUtente      the nomi utente
     * @param password        the password
     * @param email           the email
     * @param dataNascita     the data nascita
     * @param dataProposta    the data proposta
     * @param oraProposta     the ora proposta
     * @param stringaInserita the stringa inserita
     * @param numerazione     the numerazione
     * @param stato           the stato
     * @param stringaProposta the stringa proposta
     * @param datavalutazione the datavalutazione
     * @param oraValutazione  the ora valutazione
     * @param dataInserimento the data inserimento
     * @param oraInseriento   the ora inseriento
     * @throws SQLException the sql exception
     */
    public void getModifichePagina(String login, String titolo, LocalDateTime dataCreazione, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> nomiUtente, ArrayList<String> password, ArrayList<String> email, ArrayList<Date> dataNascita, ArrayList<LocalDate> dataProposta, ArrayList<LocalTime> oraProposta, ArrayList<String> stringaInserita, ArrayList<Integer> numerazione, ArrayList<Integer> stato, ArrayList<String> stringaProposta, ArrayList<Optional<LocalDate>> datavalutazione, ArrayList<Optional<LocalTime>> oraValutazione, ArrayList<LocalDate> dataInserimento, ArrayList<Time> oraInseriento) throws SQLException;

    /**
     * Aggiorna stato boolean.
     *
     * @param loginAutore     the login autore
     * @param loginUtente     the login utente
     * @param stringaInserita the stringa inserita
     * @param stringaproposta the stringaproposta
     * @param dataProposta    the data proposta
     * @param oraProposta     the ora proposta
     * @param cambiaStato     the cambia stato
     * @return the boolean
     * @throws SQLException the sql exception
     */
    public boolean aggiornaStato(String loginAutore, String loginUtente, String stringaInserita, String stringaproposta, LocalDate dataProposta, LocalTime oraProposta, int cambiaStato) throws SQLException;

    /**
     * Gets pagine create.
     *
     * @param login            the login
     * @param titoli           the titoli
     * @param dataOraCreazione the data ora creazione
     * @throws SQLException the sql exception
     */
    public void getPagineCreate(String login, ArrayList<String> titoli, ArrayList<LocalDateTime> dataOraCreazione) throws SQLException;

    public void addPaginacollegata(String stringaInserita, LocalDate dataInserimento, Time oraInserimento, int indiceFrase, String titoloCollegata, LocalDateTime dataCreazioneCollegata) throws SQLException;

    /**
     * Controlla nome utente boolean.
     *
     * @param nomeUtente the nome utente
     * @return the boolean
     * @throws SQLException the sql exception
     */
    boolean controllaNomeUtente(String nomeUtente) throws SQLException;
}

