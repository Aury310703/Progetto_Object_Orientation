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
 * L'interface WikiDao contiene tutti i metodi che hanno al loro interno query sul database per la persistenza dei dati.
 */
public interface WikiDAO {
    /**
     * prende dal database tutte le pagine che hanno all'interno del loro titolo la parola o la frase indicata dall'utente.
     *
     * @param titoloInserito   parola o frase inserita dall'utente
     * @param titoli           ArrayList contenente tutti i titoli delle pagien trovate
     * @param dateOreCreazioni ArrayList contenente tutte le date delle pagien trovate
     * @param nomi             ArrayList contenente tutti i nomi degli autori delle pagine trovate
     * @param cognomi          ArrayList contenente tutti i cognomi degli autori delle pagine trovate
     * @param login            ArrayList contenente tutti i nomi utenti degli autori delle pagine trovate
     * @param password         ArrayList contenente tutte le password degli autori delle pagine trovate
     * @param email            ArrayList contenente tutte le email degli autori delle pagine trovate
     * @param date             ArrayList contenente tutte le date di nascita degli autori delle pagine trovate
     * @throws SQLException    eccezione SQL
     */
    public void ricercaTitoli(String titoloInserito, ArrayList<String> titoli, ArrayList<LocalDateTime> dateOreCreazioni, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> login, ArrayList<String> password, ArrayList<String> email, ArrayList<Date> date) throws SQLException;

    /**
     * Prende dal database tutte le frasi originali della pagina indicata nei parametri. Il metodo oltre a trovare le frasi, cerca se le frasi hanno dei collegamenti ad altre pagine.
     *
     * @param login                     nome utente dell'autore della pagina selezionata
     * @param titolo                    titolo della pagina selezionata
     * @param dataOraCreazione          data e ora della creazione della pagina
     * @param frasiInserite             ArrayList contenente tutte le FrasiCorrenti della pagina selezionata
     * @param dateInserimento           ArrayList contenente tutte le date di inserimento delle frasiCorrenti della pagina selezionata
     * @param oreInserimento            ArrayList contenente tutte le ore di inserimento delle frasiCorrenti della pagina selezionata
     * @param titoloCollegata           ArrayList contenente tutti i titoli delle pagine che hanno un collegamento a una delle frasi della pagina collegata
     * @param dataOraCreazioneCollegata ArrayList contenente tutte le date e le ore di creazione delle pagine che hanno un collegamento a una delle frasi della pagina collegata
     * @param nomiCollegata             ArrayList contenente tutte i nomi degli autori delle pagine che hanno un collegamento a una delle frasi della pagina collegata
     * @param cognomiCollegata          ArrayList contenente tutte i cognomi degli autori delle pagine che hanno un collegamento a una delle frasi della pagina collegata
     * @param loginCollegata            ArrayList contenente tutte i nomi utenti degli autori delle pagine che hanno un collegamento a una delle frasi della pagina collegata
     * @param passwordCollegata         ArrayList contenente tutte le password degli autori delle pagine che hanno un collegamento a una delle frasi della pagina collegata
     * @param emailCollegata            ArrayList contenente tutte le email degli autori delle pagine che hanno un collegamento a una delle frasi della pagina collegata
     * @param dateCollegata             ArrayList contenente tutte le date di nascita degli autori delle pagine che hanno un collegamento a una delle frasi della pagina collegata
     * @param numerazioneCollegata      ArrayList contenente tutte le posizioni delle frasiCorrenti che hanno un collegamento ad un'altra pagina
     * @throws SQLException the sql exception
     */
    public void getFrasiCorrenti(String login, String titolo, LocalDateTime dataOraCreazione, ArrayList<String> frasiInserite, ArrayList<LocalDate> dateInserimento, ArrayList<Time> oreInserimento, ArrayList<String> titoloCollegata, ArrayList<LocalDateTime> dataOraCreazioneCollegata, ArrayList<String> nomiCollegata, ArrayList<String> cognomiCollegata, ArrayList<String> loginCollegata, ArrayList<String> passwordCollegata, ArrayList<String> emailCollegata, ArrayList<Date> dateCollegata, ArrayList<Integer> numerazioneCollegata) throws SQLException;

    /**
     * Prende dal dataBase tutte le informazionmi relative alle proposte di modifica di una determinata FraseCorrente.
     *
     * @param login            nome utente dell'autore della pagina selezionata
     * @param titolo           titolo della pagina selezionata
     * @param dataOraCreazione data e ora della creazione della pagina selezionata
     * @param stringaInserita  frase di cui si vuole sapere le modifiche proposte
     * @param numerazione      posizione all'interno della pagina della frase di cui si vuole sapere le modifiche proposte
     * @param frasiProposte    ArrayList contenente tutte le stringhe proposte della frase di cui si vuole sapere le modifiche proposte
     * @param dateProposte     ArrayList contenente tutte le date di quando si é proposta la modifica delle frasi di cui si vuole sapere le modifiche proposte
     * @param oreProposte      ArrayList contenente tutte le ore di quando si é proposta la modifica delle frasi di cui si vuole sapere le modifiche proposte
     * @param datavalutazione  ArrayList contenente tutte le date di quando l'autore ha valutato le modifiche delle frasi di cui si vuole sapere le modifiche proposte
     * @param orevalutazione   ArrayList contenente tutte le ore di quando l'autore ha valutato le modifiche delle frasi di cui si vuole sapere le modifiche proposte
     * @param stati            ArrayList contenente tutte gli stati delle modifiche delle frasi di cui si vuole sapere le modifiche proposte
     * @param nomi             ArrayList contenente tutte i nomi degli utenti che hanno proposto le modifiche alle frasi di cui si vuole sapere le modifiche proposte
     * @param cognomi          ArrayList contenente tutte i cognomi degli utenti che hanno proposto le modifiche alle frasi di cui si vuole sapere le modifiche proposte
     * @param logins           ArrayList contenente tutte i nomi utenti degli utenti che hanno proposto le modifiche alle frasi di cui si vuole sapere le modifiche proposte
     * @param password         ArrayList contenente tutte le password degli utenti che hanno proposto le modifiche alle frasi di cui si vuole sapere le modifiche proposte
     * @param email            ArrayList contenente tutte le email degli utenti che hanno proposto le modifiche alle frasi di cui si vuole sapere le modifiche proposte
     * @param date             ArrayList contenente tutte le date degli utenti che hanno proposto le modifiche alle frasi di cui si vuole sapere le modifiche proposte
     * @throws SQLException the sql exception
     */
    public void getModificheModificate(String login, String titolo, LocalDateTime dataOraCreazione, String stringaInserita, int numerazione, ArrayList<String> frasiProposte, ArrayList<LocalDate> dateProposte, ArrayList<LocalTime> oreProposte, ArrayList<Optional<LocalDate>> datavalutazione, ArrayList<Optional<LocalTime>> orevalutazione, ArrayList<Integer> stati, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> logins, ArrayList<String> password, ArrayList<String> email, ArrayList<Date> date) throws SQLException;

    /**
     * il metodo controlla se il nome utente e la password sono presenti all'interno del database. Se sono presenti restiituisce TRUE altrimenti FALSE.
     *

     * @param login      nome utente dell'utente che sta provando ad accedere
     * @param password   password dell'utente che sta provando ad accedere
     * @return boolean, vero utente trovato, falso utente non trovato
     * @throws SQLException eccezione SQL
     */
    public boolean verificaLoggato(String login, String password) throws SQLException;

    /**
     * Passate tutte le informazioni prensenti tra i parametri del metordo, il metdodo fa un INSERT del nuovo utente all'interno del database.
     *
     * @param nome          nome del nuovo utente
     * @param cognome       cognome del nuovo utente
     * @param nomeUtente    nome utente del nuovo utente
     * @param password      password del nuovo utente
     * @param email         email del nuovo utente
     * @param dataNascita   data nascita del nuovo utente
     * @throws SQLException eccezione SQL
     */
    public void registrazione(String nome, String cognome, String nomeUtente, String password, String email, Date dataNascita) throws SQLException;

    /**
     * Il metodo si occupa di salvare nel database la proposta di modifica fatta dall'utente
     *
     * @param numerazione      posizione della frase selezionata all'interno della pagina
     * @param fraseSelezionata frase selezionata all'interno della pagina
     * @param fraseProposta    strinag proposta come modifica alla frase selezionata
     * @param loginUtente      nome utente dell'utente che propone al modifica
     * @param loginAutore      nome utente dell'autore che valuta la modifica
     * @param titolo           titolo della pagina nella quale é stata proposta la modifica
     * @param dataOraCreazione data e ora di creazione della pagina nella quale é stata proposta la modifica
     * @return TRUE se la proposta é stata inviata, FALSE se non é stata inviata
     * @throws SQLException the sql exception
     */
    public boolean inviaProposta(int numerazione, String fraseSelezionata, String fraseProposta, String loginUtente, String loginAutore, String titolo, LocalDateTime dataOraCreazione) throws SQLException;

    /**
     * il metodo si occupa di crare all'interno del database la nuova pagina e le frasi del testo scritte dall'autore.
     *
     * @param titolo        titolo della pagina creata
     * @param frasi         ArrayList contenete tutte le frasi del testo scritto
     * @param login         nome utente dell'autore della nuova pagina
     * @param dataCreazione data e ora di creazione della nuova pagina creata
     * @throws SQLException eccezione SQL
     */
    public void creazionePagina(String titolo, ArrayList<String> frasi, String login, LocalDateTime dataCreazione) throws SQLException;

    /**
     * il metodo prende dal database tutte le pagine visualizzate dall'utente indicato.
     *
     * @param loginUtente      nome utente dell'utente di cui si vuole sapere le pagine visualizzate
     * @param titoli           ArrayList contentente tutti i titoli delle pagine visualizzate
     * @param dateOreCreazioni ArrayList contentente tutte le date e ore di creazioni delle pagine visualizzate
     * @param nomi             ArrayList contenente tutti i nomi degli autori delle pogine visualizzate dall'utente indicato
     * @param cognomi          ArrayList contenente tutti i cognomi degli autori delle pogine visualizzate dall'utente indicato
     * @param nomiUtente       ArrayList contenente tutti i nomi utenti degli autori delle pogine visualizzate dall'utente indicato
     * @param password         ArrayList contenente tutti le password degli autori delle pogine visualizzate dall'utente indicato
     * @param email            ArrayList contenente tutti le email degli autori delle pogine visualizzate dall'utente indicato
     * @param dataNascita      ArrayList contenente tutti le date nascita degli autori delle pogine visualizzate dall'utente indicato
     * @param dateVisioni      ArrayList contenente tutte le date di quando l'utente ha visualizzato le pagine
     * @param oreVisioni       ArrayList contenente tutte le ore di quando l'utente ha visualizzato le pagine
     * @throws SQLException the sql exception
     */
    public void storicoPagineVisualizzate(String loginUtente, ArrayList<String> titoli, ArrayList<LocalDateTime> dateOreCreazioni, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> nomiUtente, ArrayList<String> password, ArrayList<String> email, ArrayList<Date> dataNascita, ArrayList<LocalDate> dateVisioni, ArrayList<LocalTime> oreVisioni) throws SQLException;

    /**
     * il metodo memorizza all'interno del database la pagina visualizzata dall'utente.
     *
     * @param titolo                    titolo della pagina che ha visualizzato l'utente
     * @param DataOraCreazione          data e ora creazione della pagina che ha visualizzato l'utente
     * @param loginAutorePagina         nome utente dell'autore della pagina che ha visualizzato l'utente
     * @param loginUtenteVisualizzatore nome utente dell'utente che ha visualizzato la pagina
     * @throws SQLException the sql exception
     */
    public void addPaginaVisualizzata(String titolo, LocalDateTime DataOraCreazione, String loginAutorePagina, String loginUtenteVisualizzatore) throws SQLException;

    /**
     * il medodo, data un utente, restituisce dal database tutte le modifiche fatte dall'utente.
     *
     * @param login            login di accesso dell'utente
     * @param titolo           titolo della pagina di cui si vogliono sapere le modifiche
     * @param dataOraCreazione data e ora di creazione della pagina di cui si vogliono sapere le modifiche
     * @param nomi             ArrayList che contiene tutti i nomi degli autori delle pagine in cui l'utente ha proposto una modifica
     * @param cognomi          ArrayList che contiene tutti i cognomi degli autori delle pagine in cui l'utente ha proposto una modifica
     * @param nomiUtente       ArrayList che contiene tutti i nomi utenti degli autori delle pagine in cui l'utente ha proposto una modifica
     * @param password         ArrayList che contiene tutti le password degli autori delle pagine in cui l'utente ha proposto una modifica
     * @param email            ArrayList che contiene tutti le email degli autori delle pagine in cui l'utente ha proposto una modifica
     * @param dataNascita      ArrayList che contiene tutti le date di nascita degli autori delle pagine in cui l'utente ha proposto una modifica
     * @param dataProposta     ArrayList che contiene tutte le date di proposta delle modifiche effettuate dall'utente
     * @param oraProposta      ArrayList che contiene tutte le ore di proposta delle modifiche effettuate dall'utente
     * @param stringaInserita  ArrayList che contiene tutte le frasi originali sulle quali l'utente ha proposto una modifica
     * @param numerazione      ArrayList che contiene tutte le posizioni delle frasi originali sulle quali l'utente ha proposto una modifica
     * @param stato            ArrayList che contiene tutte gli stati di valutazione delle proposte fatte dall'utente
     * @param stringaProposta  ArrayList che contiene tutte le frasi proposte fatte dall'utente come modifica alle frasi originali
     * @param datavalutazione  ArrayList che contiene tutte le date di valutazione delle proposte fatte dall'utente
     * @param oraValutazione   ArrayList che contiene tutte le ore di valutazione delle proposte fatte dall'utente
     * @param dataInserimento  ArrayList che contiene tutte le date di inserimento delle frasi originali sulle quali l'autore propone modifiche
     * @param oraInseriento    ArrayList che contiene tutte le ore di inserimento delle frasi originali sulle quali l'autore propone modifiche
     * @throws SQLException the sql exception
     */
    public void getModificate(String login, ArrayList<String> titolo, ArrayList<LocalDateTime> dataOraCreazione, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> nomiUtente, ArrayList<String> password, ArrayList<String> email, ArrayList<Date> dataNascita, ArrayList<LocalDate> dataProposta, ArrayList<LocalTime> oraProposta, ArrayList<String> stringaInserita, ArrayList<Integer> numerazione, ArrayList<Integer> stato, ArrayList<String> stringaProposta, ArrayList<Optional<LocalDate>> datavalutazione, ArrayList<Optional<LocalTime>> oraValutazione, ArrayList<LocalDate> dataInserimento, ArrayList<Time> oraInseriento) throws SQLException;

    /**
     * data un login di accesso di un utente, il metodo verifica nel database se sono presenti delle notifiche all'autore.
     *
     * @param login login di accesso dell'utente
     * @return TRUE se ci sono notifiche; FALSE se non ci sono notifiche
     * @throws SQLException the sql exception
     */
    public boolean controllaNotifiche(String login) throws SQLException;

    /**
     * Restituisce il nome dell'utente.
     *
     * @param login    login di accesso dell'utente
     * @return nome dell'utente
     */
    public String getNomeUtente(String login);

    /**
     * Restituisce il cognome dell'utente.
     *
     * @param login    login di accesso dell'utente
     * @return il cognome dell'utente
     */
    public String getCognomeUtente(String login);

    /**
     * Restituisce l'email dell'utente.
     *
     * @param login    login di accesso dell'utente
     * @return email dell'utente
     */
    String getEmailUtente(String login);

    /**
     * Restituisce la data di nascita dell'utente.
     *
     * @param login    login di accesso dell'utente
     * @return data nascita dell'utente
     */
    Date getDataNascitaUtente(String login);

    /**
     * Restituisce il ruolo dell'utente.
     *
     * @param login    login di accesso dell'utente
     * @return ruolo dell'utente
     */
    String getRuolotente(String login);

    /**
     * data una pagina e il suo autore, il metodo restituiscve tutte le modifiche fatte alle frasi.
     *
     * @param login           login di accesso dell'utente
     * @param titolo          titolo della pagina di cui si vuole conoscere le modifiche
     * @param dataCreazione   data e ora di creazione della pagina di cui si vuole conoscere le modifiche
     * @param nomi            ArrayList contenente tutti i nomi degli utenti che hanno proposto una modifica ad una frase della pagina indicata
     * @param cognomi         ArrayList contenente tutti i cognomi degli utenti che hanno proposto una modifica ad una frase della pagina indicata
     * @param nomiUtente      ArrayList contenente tutti i nomi utenti degli utenti che hanno proposto una modifica ad una frase della pagina indicata
     * @param password        ArrayList contenente tutti le password degli utenti che hanno proposto una modifica ad una frase della pagina indicata
     * @param email           ArrayList contenente tutti le email degli utenti che hanno proposto una modifica ad una frase della pagina indicata
     * @param dataNascita     ArrayList contenente tutti el date di nascita degli utenti che hanno proposto una modifica ad una frase della pagina indicata
     * @param dataProposta    ArrayList contenente tutte le date di proposta delle modifiche fatte alle frasi della pagina indicata
     * @param oraProposta     ArrayList contenente tutte le ore di proposta delle modifiche fatte alle frasi della pagina indicata
     * @param stringaInserita ArrayList contenente tutte le frasi originali che sono state modifiche nella pagina indicata
     * @param numerazione     ArrayList contenente tutte le posizioni delle frasi originali che sono state modifiche nella pagina indicata
     * @param stato           ArrayList contenente tutti gli stati di valutazione delle modifiche fatte alle frasi della pagina indicata
     * @param stringaProposta ArrayList contenente tutte le stringhe proposte come modifiche alle frasi della pagina indicata
     * @param datavalutazione ArrayList contenente tutte le date di valutazioni delle modifiche fatte alle frasi della pagina indicata
     * @param oraValutazione  ArrayList contenente tutte le ore di valutazioni delle modifiche fatte alle frasi della pagina indicata
     * @param dataInserimento ArrayList contenente tutte le date di inserimento delle frasi originali che sono state modificate nella pagina indicata
     * @param oraInseriento   ArrayList contenente tutte le ore di inserimento delle frasi originali che sono state modificate nella pagina indicata
     * @throws SQLException the sql exception
     */
    public void getModifichePagina(String login, String titolo, LocalDateTime dataCreazione, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> nomiUtente, ArrayList<String> password, ArrayList<String> email, ArrayList<Date> dataNascita, ArrayList<LocalDate> dataProposta, ArrayList<LocalTime> oraProposta, ArrayList<String> stringaInserita, ArrayList<Integer> numerazione, ArrayList<Integer> stato, ArrayList<String> stringaProposta, ArrayList<Optional<LocalDate>> datavalutazione, ArrayList<Optional<LocalTime>> oraValutazione, ArrayList<LocalDate> dataInserimento, ArrayList<Time> oraInseriento) throws SQLException;

    /**
     * Aggiorna lo stato delle modifiche all'interno del database.
     *
     * @param loginAutore       login di accesso dell'autore della pagina selezionata
     * @param loginUtente       login di accesso dell'utente che ha proposto la modifica
     * @param stringaInserita   frase selezionata che deve essere modificata
     * @param stringaproposta   frase proposta come modifica
     * @param dataProposta      data di quando é stata proposta la modifica
     * @param oraProposta       ora di quando é stata proposta la modifica
     * @param cambiaStato       indica la valutazione fatta dall'autore (-1 rifiutato, 1 accettato)
     * @return TRUE stato aggiornato; FALSE stato non aggiornato
     * @throws SQLException the sql exception
     */
    public boolean aggiornaStato(String loginAutore, String loginUtente, String stringaInserita, String stringaproposta, LocalDate dataProposta, LocalTime oraProposta, int cambiaStato) throws SQLException;

    /**
     * restituisce dal database tutte le pagine create dall'autore indicato nei parametri.
     *
     * @param login            nome utente dell'autore
     * @param titoli           ArrayList contenente tutti i titoli delle pagine da lui create
     * @param dataOraCreazione ArrayList contenente tutti le date e ore di creazione delle pagine da lui create
     * @throws SQLException the sql exception
     */
    public void getPagineCreate(String login, ArrayList<String> titoli, ArrayList<LocalDateTime> dataOraCreazione) throws SQLException;

    /**
     * memorizza nel database la pagina che e stata collegata alla frase selezionata.
     *
     * @param stringaInserita           indica la frase selezionata dall'autore
     * @param dataInserimento           data di inserimento della frase selezionata
     * @param oraInserimento            ora di inserimento della frase selezionata
     * @param indiceFrase               posizione all'interno del testo della frase selezionata
     * @param titoloCollegata           titolo della pagina che si vuole collegare alla frase selezionata
     * @param dataCreazioneCollegata    data e ora creazione della pagina che si vuole collegare alla frase selezionata
     * @throws SQLException the sql exception
     */
    public void addPaginacollegata(String stringaInserita, LocalDate dataInserimento, Time oraInserimento, int indiceFrase, String titoloCollegata, LocalDateTime dataCreazioneCollegata) throws SQLException;

    /**
     * Controlla se il nome utente passato per parametro é presente nel database.
     *
     * @param nomeUtente nome utente
     * @return TRUE se é nomeUtente e presente nel database; FALSE se non é presente
     * @throws SQLException the sql exception
     */
    boolean controllaNomeUtente(String nomeUtente) throws SQLException;
}

